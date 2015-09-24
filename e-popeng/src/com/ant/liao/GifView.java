package com.ant.liao;

import java.io.InputStream;

import com.hrtvbic.adpop.R;



import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class GifView extends View implements GifAction {

	private GifDecoder gifDecoder = null;
	private Bitmap currentImage = null;

	private boolean isRun = true;

	private boolean pause = false;
	private CallBack callBack = null;;
	private int showWidth = -1;
	private int showHeight = -1;
	private Rect rect = null;
	private int count=0;
	//private int currentBg = R.drawable.pop1;
	private DrawThread drawThread = null;

	private GifImageType animationType = GifImageType.SYNC_DECODER;

	public enum GifImageType {
		WAIT_FINISH(0),
		SYNC_DECODER(1),
		COVER(2);

		GifImageType(int i) {
			nativeInt = i;
		}

		final int nativeInt;
	}

	public void setCallBack(CallBack c) {
		this.callBack = c;
	}

	public GifView(Context context) {
		super(context);
		//th.start();

	}

	public GifView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		//bgHandler.sendEmptyMessage(0);
		//th.start();
	}

	public GifView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	private void setGifDecoderImage(byte[] gif) {
		if (gifDecoder != null) {
			gifDecoder.free();
			gifDecoder = null;
		}
		gifDecoder = new GifDecoder(gif, this);
		gifDecoder.start();
	}

	private void setGifDecoderImage(InputStream is) {
		if (gifDecoder != null) {
			gifDecoder.free();
			gifDecoder = null;
		}
		gifDecoder = new GifDecoder(is, this);
		gifDecoder.start();
	}

	public void setGifImage(byte[] gif) {
		setGifDecoderImage(gif);
	}

	public void setGifImage(InputStream is) {
		setGifDecoderImage(is);
	}

	public void setGifImage(int resId) {
		Resources r = this.getResources();
		InputStream is = r.openRawResource(resId);
		setGifDecoderImage(is);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//Bitmap bm=BitmapFactory.decodeResource(getResources(), currentBg);
//		if(bm==null){
//			System.out.println("bm==null");
//		}
//		else{
//			System.out.println("currentBg"+currentBg);
//		}
		
		canvas.drawColor(getResources().getColor(R.color.bg));
		//canvas.drawBitmap(bm, rect, null);
		//canvas.drawBitmap(bm, 0, 0, null);
		if (gifDecoder == null)
			return;
		if (currentImage == null) {
			currentImage = gifDecoder.getImage();
		}
		if (currentImage == null) {
			return;
		}
		int saveCount = canvas.getSaveCount();
		canvas.save();
		canvas.translate(getPaddingLeft(), getPaddingTop());
		
		
		
		if (showWidth == -1) {
			//canvas.drawBitmap(bm, 0, 0, null);
			canvas.drawBitmap(currentImage, 0, 0, null);
		} else {
			//canvas.drawBitmap(bm, 0, 0, null);
			canvas.drawBitmap(currentImage, null, rect, null);
		}
		canvas.restoreToCount(saveCount);
		if (callBack != null) {
			callBack.reflash();
		}
		
		System.gc();
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int pleft = getPaddingLeft();
		int pright = getPaddingRight();
		int ptop = getPaddingTop();
		int pbottom = getPaddingBottom();

		int widthSize;
		int heightSize;

		int w;
		int h;

		if (gifDecoder == null) {
			w = 1;
			h = 1;
		} else {
			w = gifDecoder.width;
			h = gifDecoder.height;
		}

		w += pleft + pright;
		h += ptop + pbottom;

		w = Math.max(w, getSuggestedMinimumWidth());
		h = Math.max(h, getSuggestedMinimumHeight());

		widthSize = resolveSize(w, widthMeasureSpec);
		heightSize = resolveSize(h, heightMeasureSpec);

		setMeasuredDimension(widthSize, heightSize);
	}

	/**
	 * 鍙樉绀虹涓�抚鍥剧墖<br>
	 * 璋冪敤鏈柟娉曞悗锛実if涓嶄細鏄剧ず鍔ㄧ敾锛屽彧浼氭樉绀篻if鐨勭涓�抚鍥�
	 */
	public void showCover() {
		if (gifDecoder == null)
			return;
		pause = true;
		currentImage = gifDecoder.getImage();
		invalidate();
	}

	/**
	 * 缁х画鏄剧ず鍔ㄧ敾<br>
	 * 鏈柟娉曞湪璋冪敤showCover鍚庯紝浼氳鍔ㄧ敾缁х画鏄剧ず锛屽鏋滄病鏈夎皟鐢╯howCover鏂规硶锛屽垯娌℃湁浠讳綍鏁堟灉
	 */
	public void showAnimation() {
		if (pause) {
			pause = false;
		}
	}

	/**
	 * 璁剧疆gif鍦ㄨВ鐮佽繃绋嬩腑鐨勬樉绀烘柟寮�br>
	 * <strong>鏈柟娉曞彧鑳藉湪setGifImage鏂规硶涔嬪墠璁剧疆锛屽惁鍒欒缃棤鏁�/strong>
	 * 
	 * @param type
	 *            鏄剧ず鏂瑰紡
	 */
	public void setGifImageType(GifImageType type) {
		if (gifDecoder == null)
			animationType = type;
	}

	/**
	 * 璁剧疆瑕佹樉绀虹殑鍥剧墖鐨勫ぇ灏�br>
	 * 褰撹缃簡鍥剧墖澶у皬 涔嬪悗锛屼細鎸夌収璁剧疆鐨勫ぇ灏忔潵鏄剧ずgif锛堟寜璁剧疆鍚庣殑澶у皬鏉ヨ繘琛屾媺浼告垨鍘嬬缉锛�
	 * 
	 * @param width
	 *            瑕佹樉绀虹殑鍥剧墖瀹�
	 * @param height
	 *            瑕佹樉绀虹殑鍥剧墖楂�
	 */
	public void setShowDimension(int width, int height) {
		if (width > 0 && height > 0) {
			showWidth = width;
			showHeight = height;
			rect = new Rect();
			rect.left = 0;
			rect.top = 0;
			rect.right = width;
			rect.bottom = height;
		}
	}

	public void parseOk(boolean parseStatus, int frameIndex) {
		if (parseStatus) {
			if (gifDecoder != null) {
				switch (animationType) {
				case WAIT_FINISH:
					if (frameIndex == -1) {
						if (gifDecoder.getFrameCount() > 1) { // 褰撳抚鏁板ぇ浜�鏃讹紝鍚姩鍔ㄧ敾绾跨▼
							DrawThread dt = new DrawThread();
							dt.start();
						} else {
							reDraw();
						}
					}
					break;
				case COVER:
					if (frameIndex == 1) {
						currentImage = gifDecoder.getImage();
						reDraw();
					} else if (frameIndex == -1) {
						if (gifDecoder.getFrameCount() > 1) {
							if (drawThread == null) {
								drawThread = new DrawThread();
								drawThread.start();
							}
						} else {
							reDraw();
						}
					}
					break;
				case SYNC_DECODER:
					if (frameIndex == 1) {
						currentImage = gifDecoder.getImage();
						reDraw();
					} else if (frameIndex == -1) {
						reDraw();
					} else {
						if (drawThread == null) {
							drawThread = new DrawThread();
							drawThread.start();
						}
					}
					break;
				}

			} else {
				Log.e("gif", "parse error");
			}

		}
	}

	private void reDraw() {
		if (redrawHandler != null) {
			Message msg = redrawHandler.obtainMessage();
			redrawHandler.sendMessage(msg);
		}
	}

	private Handler redrawHandler = new Handler() {
		public void handleMessage(Message msg) {
			invalidate();
		}
	};
//	private Handler bgHandler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			currentBg=R.drawable.bg1+(count%4);
//			count++;
//			//bgHandler.sendEmptyMessageDelayed(0, 3000);
//		}
//	};

	/**
	 * 鍔ㄧ敾绾跨▼
	 * 
	 * @author liao
	 * 
	 */
	private class DrawThread extends Thread {
		public void run() {
			if (gifDecoder == null) {
				return;
			}
			while (isRun) {
				if (pause == false) {
					// if(gifDecoder.parseOk()){
					GifFrame frame = gifDecoder.next();
					currentImage = frame.image;
					long sp = frame.delay;
					if (redrawHandler != null) {
						Message msg = redrawHandler.obtainMessage();
						redrawHandler.sendMessage(msg);
						SystemClock.sleep(sp);
					} else {
						break;
					}
					// }else{
					// currentImage = gifDecoder.getImage();
					// break;
					// }
				} else {
					SystemClock.sleep(10);
				}
			}
		}
	}

}
