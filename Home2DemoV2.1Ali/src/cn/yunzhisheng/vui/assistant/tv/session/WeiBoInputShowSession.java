package cn.yunzhisheng.vui.assistant.tv.session;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import cn.yunzhisheng.common.util.LogUtil;
import cn.yunzhisheng.vui.assistant.preference.PrivatePreference;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;
import cn.yunzhisheng.vui.assistant.tv.view.WeiboContentView;
import cn.yunzhisheng.vui.assistant.tv.view.WeiboContentView.IWeiboContentViewListener;

public class WeiBoInputShowSession extends CommBaseSession {

	public static final String TAG = "WeiboInputSession";
	private WeiboContentView mWeiboContentView = null;
	public static String mImagePath = "";
	public static String mWeiBoType = "SINA";
	private Uri mImageUri = null;

	private boolean mDeleteImageAfterUploading = false;
	private StringBuilder mContentBuilder = new StringBuilder();;

	private IWeiboContentViewListener mWeiboContentViewListener = new IWeiboContentViewListener() {
		public void onOk() {
			LogUtil.d(TAG, "onOk");
			cancelTTS();
			onUiProtocal(mOkProtocal);
		}

		@Override
		public void onImageOperation(int operation) {
			LogUtil.d(TAG, "onImageOperation " + operation + ", " + Environment.getExternalStorageState());
			switch (operation) {
			case SessionPreference.OPERATION_WEIBO_IMAGE_ADD_FROM_CAMERA:
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					cancelTTS();
					mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_CANCEL_TALK);

					mDeleteImageAfterUploading = true;

					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
					String photoFilename = simpleDateFormat.format(new Date());

					ContentValues values = new ContentValues();
					values.put(Media.TITLE, photoFilename);

					mImageUri = mContext.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);

					((Activity) mContext).startActivityForResult(
						intent,
						SessionPreference.REQUEST_CODE_WEIBO_TAKE_PHOTO);
				}
				break;
			case SessionPreference.OPERATION_WEIBO_IMAGE_ADD_FROM_ALBUM:
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					cancelTTS();
					mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_CANCEL_TALK);

					mDeleteImageAfterUploading = false;

					Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					((Activity) mContext).startActivityForResult(
						intent,
						SessionPreference.REQUEST_CODE_WEIBO_SELECT_IMAGE);
				}
				break;
			case SessionPreference.OPERATION_WEIBO_IMAGE_REMOVE:

				mImagePath = "";
				break;
			}
		}

		@Override
		public void onEndEdit(String msg) {
			LogUtil.d(TAG, "onEndEdit " + msg);
			mWeiboContentView.setMessage(msg);

			String protocolStr = "{\"service\":\"DOMAIN_HAND_INPUT_CONTENT\" , \"content\":\"" + msg + "\"}";
			onUiProtocal(protocolStr);
		}

		@Override
		public void onCancel() {
			LogUtil.d(TAG, "onCancel");
			onUiProtocal(mCancelProtocal);
		}

		@Override
		public void onBeginEdit() {
			LogUtil.d(TAG, "onBeginEdit");
			cancelTTS();
			mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_CANCEL_TALK);
		}

		@Override
		public void onClearMessage() {
			mContentBuilder.setLength(0);
			mWeiboContentView.setMessage("");
			String protocolStr = "{\"service\":\"DOMAIN_HAND_INPUT_CONTENT\" , \"content\":\"" + "" + "\"}";
			//onUiProtocal(protocolStr);
		}
	};

	WeiBoInputShowSession(Context context, Handler handle) {
		super(context, handle);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case SessionPreference.REQUEST_CODE_WEIBO_TAKE_PHOTO: {
			if (resultCode == Activity.RESULT_OK) {
				getImageAndThumbnail();
			}
		}
			break;
		case SessionPreference.REQUEST_CODE_WEIBO_SELECT_IMAGE: {
			if (resultCode == Activity.RESULT_OK && data != null) {
				mImageUri = data.getData();
				getImageAndThumbnail();
			}
		}
			break;
		}
	}

	private void getImageAndThumbnail() {
		if (mImageUri.getScheme().equals("content")) {
			String[] projection = { Images.Media.DATA };
			Cursor imageCursor = mContext.getContentResolver().query(mImageUri, projection, null, null, null);
			if (imageCursor.moveToFirst()) {
				int dataColumnIndex = imageCursor.getColumnIndex(Images.Media.DATA);
				mImagePath = imageCursor.getString(dataColumnIndex);
			}

			imageCursor.close();
		} else if (mImageUri.getScheme().equals("file")) {
			mImagePath = mImageUri.getPath();
		}

		Options decodeOptions = new BitmapFactory.Options();
		decodeOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mImagePath, decodeOptions);

		int sampleSize = (int) Math.ceil(Math.max(
			decodeOptions.outWidth / PrivatePreference.WEIBO_IMAGE_MAX_WIDTH,
			decodeOptions.outHeight / PrivatePreference.WEIBO_IMAGE_MAX_HEIGHT) + 0.5f);
		Bitmap scaledBitmap = null;

		if (sampleSize > 1) {
			decodeOptions.inSampleSize = sampleSize;
			decodeOptions.inJustDecodeBounds = false;

			scaledBitmap = BitmapFactory.decodeFile(mImagePath, decodeOptions);

			if (mDeleteImageAfterUploading) {
				mContext.getContentResolver().delete(mImageUri, null, null);

				File originImagePath = new File(mImagePath);
				if (originImagePath.exists()) {
					originImagePath.delete();
				}
				mImagePath = "";
			}

			File fileDirectory = mContext.getFilesDir();
			File scaledImagePath = new File(fileDirectory.getAbsolutePath() + File.separator
											+ "weibo_upload_image_scaled.jpg");
			if (scaledImagePath.exists()) {
				scaledImagePath.delete();
			}

			try {
				BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(scaledImagePath));
				scaledBitmap.compress(CompressFormat.JPEG, 80, outputStream);

				outputStream.flush();
				outputStream.close();

				mImagePath = scaledImagePath.getAbsolutePath();
			} catch (FileNotFoundException e) {
				e.printStackTrace();

				scaledBitmap = null;
			} catch (IOException e) {
				e.printStackTrace();

				scaledBitmap = null;
			}
		} else {
			scaledBitmap = BitmapFactory.decodeFile(mImagePath);
		}
	}

	public void putProtocol(JSONObject jsonProtocol) {
		super.putProtocol(jsonProtocol);
		String content = "";
		int type = 0;
		if (mJsonObject != null) {
			if (mJsonObject.has(SessionPreference.KEY_CONTENT)) {
				content = getJsonValue(mJsonObject, SessionPreference.KEY_CONTENT);
			}
			if (mJsonObject.has(SessionPreference.KEY_VENDOR)) {
				String vendor = getJsonValue(mJsonObject, SessionPreference.KEY_VENDOR);
				if (vendor == null || vendor.equals(SessionPreference.VALUE_WEIBO_VENDOR_SINA)) {
					type = 0;
				} else if (vendor.equals(SessionPreference.VALUE_WEIBO_VENDOR_TENCENT)) {
					type = 1;
				} else if (vendor.equals(SessionPreference.VALUE_WEIBO_VENDOR_RENREN)) {
					type = 2;
				}
				mWeiBoType = vendor;
			} else {

			}
		}
		if (mWeiboContentView == null) {
			mWeiboContentView = new WeiboContentView(mContext);
			mWeiboContentView.setListener(mWeiboContentViewListener);
		}
		mWeiboContentView.requestSuperFocus();
		mWeiboContentView.setMessage(content);
		addAnswerView(mWeiboContentView);

	}

	protected void editShowContent() {
	}

}
