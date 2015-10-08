package cn.yunzhisheng.vui.assistant.tv.view;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.yunzhisheng.common.util.LogUtil;
import cn.yunzhisheng.vui.assistant.Res;

@SuppressLint("NewApi")
public class GloblFloatView extends FloatView {
	private static final String TAG = "GloblFloatView";
	private View view;
	private ImageView mVoiceBg;
	private ImageView mVoiceLevel;
	private ImageView mVoiceRing;
	private ImageView mRatoteImage;
	private Animation operatingAnim;
	private LinearInterpolator lin;
	private Animation mRecognitionScaleAnim;
	private Animation mAlaphaHalfAnim;
	private Animation mAlaphaAllAnim;
	private Animation mAlaphaOutAnim;
	private LinearLayout voice_start;
	private String mProtocal;
	private int mVolume;
	private LinearLayout linearLayoutMainView;
	private TextView mTextResul,mTextRecord;
	
	private Handler handler = new Handler();

	private Timer timer;
	//private Context mContext;
	
	
	public GloblFloatView(Context context) {
		super(context);
		init();
		initViewStyle();
	}

	private void initViewStyle() {
		mWindowParams.type = android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		mWindowParams.format = PixelFormat.RGBA_8888;
		mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
		mWindowParams.gravity = Gravity.RIGHT;
		mWindowParams.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		mWindowParams.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
	}

	public void setProtocal(String protocal){
		mProtocal = protocal;
	}
	
	public void updateVoiceView(VoiceMode mode) {
		Log.d(TAG, "global record sound update view " + mode);
		switch (mode) {
		case MODE_RECORDING:
			setVisibility(View.VISIBLE);
			mTextResul.setText("大声说片名，快速搜索");
			mTextRecord.setVisibility(View.VISIBLE);
			mRatoteImage.clearAnimation();
			mRatoteImage.setVisibility(View.GONE);
			voice_start.startAnimation(mAlaphaAllAnim);
			voice_start.setVisibility(INVISIBLE);
			mVoiceRing.setVisibility(View.VISIBLE);
			mVoiceBg.setVisibility(View.VISIBLE);
			mVoiceBg.setImageResource(Res.drawable.ic_voice_speaking);
			mVoiceLevel.setVisibility(View.VISIBLE);
			break;
		case MODE_RECOGNISING:
			setVisibility(View.VISIBLE);
			mRatoteImage.setVisibility(View.VISIBLE);
			mRatoteImage.startAnimation(operatingAnim);
			mVoiceBg.setImageResource(Res.drawable.ic_voice_prasing);
			mVoiceRing.setVisibility(View.GONE);
			mVoiceLevel.setVisibility(View.GONE);
			break;
		case MODE_UNRECOGNISED:
			setVisibility(View.VISIBLE);
			mVoiceRing.setVisibility(View.INVISIBLE);
			mVoiceBg.setImageResource(Res.drawable.ic_voice_default);
			setRatoteImageGone();
			mVoiceLevel.setVisibility(View.GONE);
			break;
		case MODE_NO_VOICE:
			setVisibility(View.VISIBLE);
			mVoiceRing.setVisibility(View.INVISIBLE);
			mVoiceBg.setImageResource(Res.drawable.ic_voice_default);
			mVoiceLevel.setVisibility(View.GONE);
			break;
		case MODE_RECORDINGINIT:
			setVisibility(View.INVISIBLE);
			mTextResul.setText("正在获取结果中...");
			voice_start.startAnimation(mRecognitionScaleAnim);
			voice_start.startAnimation(mAlaphaHalfAnim);
//			linearLayoutMainView.setBackgroundColor(Color.RED);
			mVoiceRing.setVisibility(View.INVISIBLE);
			mVoiceBg.setImageResource(Res.drawable.ic_voice_prasing);
			mVoiceLevel.setVisibility(View.GONE);
			break;
		case MODE_UNAVILIABLE:
			setEnabled(false);
			break;
		case MODE_DEFAULT:
			setEnabled(true);
			mVoiceRing.setVisibility(View.INVISIBLE);
			mVoiceBg.setImageResource(Res.drawable.ic_voice_default_current);
			mVoiceLevel.setVisibility(View.GONE);
			setRatoteImageGone();
			break;
		case MODE_PROTOCAL:
			setEnabled(true);
			mTextResul.setText("正在识别...");
			mTextRecord.setVisibility(View.INVISIBLE);
			mVoiceRing.setVisibility(View.INVISIBLE);
			mVoiceBg.setVisibility(View.VISIBLE);
			//-----------------
			linearLayoutMainView.setVisibility(View.GONE);
			mVoiceBg.setImageResource(Res.drawable.ic_voice_speaking);
			mRatoteImage.clearAnimation();
			mRatoteImage.setVisibility(View.GONE);
			mVoiceLevel.setVisibility(View.GONE);
			break;
		case MODE_RECORDINGSHORT:
			setEnabled(true);
			voice_start.clearAnimation();
			mVoiceRing.setVisibility(View.INVISIBLE);
			mVoiceBg.setVisibility(View.INVISIBLE);
			mRatoteImage.clearAnimation();
			mRatoteImage.setVisibility(View.GONE);
			mVoiceLevel.setVisibility(View.GONE);
			break;
		}
	}
	
	@Override
	public void hide(){
		voice_start.startAnimation(mAlaphaOutAnim);
		setRatoteImageGone();
		if(timer !=null){
			timer.cancel();
		}
		super.hide();
	}
	
	@Override
	public void show(){
		if (timer == null) {
			timer = new Timer();
		}
		super.show();
	}
	
	private void setRatoteImageGone() {
		mRatoteImage.clearAnimation();
		mRatoteImage.setVisibility(View.GONE);
	}

	public void updateVolume(int volume) {
		mVolume = volume;
		/*if (mVolume > 10) {
			mVoiceRing.setVisibility(View.VISIBLE);
			mVoiceLevel.setVisibility(View.VISIBLE);
			setRatoteImageGone();
		}*/

		if (mVolume < 10) {
			setVoiceLevel(1);
		} else if (mVolume < 20) {
			setVoiceLevel(2);
		} else if (mVolume < 30) {
			setVoiceLevel(3);
		} else if (mVolume < 40) {
			setVoiceLevel(4);
		} else if (mVolume < 50) {
			setVoiceLevel(5);
		} else if (mVolume < 60) {
			setVoiceLevel(6);
		} else if (mVolume < 70) {
			setVoiceLevel(7);
		} else if (mVolume < 80) {
			setVoiceLevel(8);
		} else if (mVolume < 90) {
			setVoiceLevel(9);
		} else {
			setVoiceLevel(10);
		}
	}

	public void onMoveUp(){
		LogUtil.d(TAG, "onMoveUp");
	}
	
	public void onMoveDown(){
		LogUtil.d(TAG, "onMoveDown");
	}
	
	public void setVoiceLevel(int level) {
		mVoiceLevel.getDrawable().setLevel(level);
	}

	private void init() {
		setOrientation(VERTICAL);
		setGravity(Gravity.RIGHT);
		view = LayoutInflater.from(getContext()).inflate(
				Res.layout.global_float_view, this);

		voice_start = (LinearLayout) view.findViewById(Res.id.voice_start);
		
		mTextResul = (TextView) view.findViewById(Res.id.text_result);
		mTextRecord = (TextView) view.findViewById(Res.id.text_record);
		//--------------------xuhua
		linearLayoutMainView = (LinearLayout) view.findViewById(Res.id.linearLayoutMainView);
		mVoiceBg = (ImageView) view.findViewById(Res.id.voice_bg);
		mVoiceLevel = (ImageView) view.findViewById(Res.id.voice_level);
		mVoiceRing = (ImageView) view.findViewById(Res.id.voice_ring);
		
		
		mRatoteImage = (ImageView) view.findViewById(Res.id.rotate_animation);

		lin = new LinearInterpolator();
		operatingAnim = AnimationUtils.loadAnimation(getContext(),
				Res.anim.rotate_sacnner);
		operatingAnim.setInterpolator(lin);

		/** ---Dialog 进入Scale动画---modify by WLP at 2013-12-04--- */
		mRecognitionScaleAnim = AnimationUtils.loadAnimation(getContext(),
				Res.anim.global_voice_scale_in);
		mAlaphaHalfAnim = AnimationUtils.loadAnimation(getContext(),
				Res.anim.global_voice_alpha_half);
		mAlaphaAllAnim = AnimationUtils.loadAnimation(getContext(),
				Res.anim.global_voice_alpha_all);
		mAlaphaOutAnim = AnimationUtils.loadAnimation(getContext(),
				Res.anim.global_voice_alpha_out);
		/** ---Dialog 进入Scale动画---modify by WLP at 2013-12-04--- */
	}
	
	class RefreshTask extends TimerTask {
		@Override
		public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						if (mVolume > 10) {
							timer.cancel();
							voice_start.startAnimation(mAlaphaAllAnim);
							mVoiceRing.setVisibility(View.VISIBLE);
							mVoiceBg.setVisibility(View.VISIBLE);
							mVoiceBg.setImageResource(Res.drawable.ic_voice_speaking);
							mVoiceLevel.setVisibility(View.VISIBLE);
						}
					}
				});
		}
	}
}
