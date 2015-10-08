/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : SettingSession.java
 * @ProjectName : vui_assistant
 * @PakageName : cn.yunzhisheng.ishuoshuo.session
 * @Author : Dancindream
 * @CreateDate : 2013-9-6
 */
package cn.yunzhisheng.vui.assistant.tv.session;

import org.json.JSONObject;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import cn.yunzhisheng.vui.assistant.Res;
import cn.yunzhisheng.vui.assistant.model.KnowledgeMode;
import cn.yunzhisheng.vui.assistant.oem.RomControl;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;
import cn.yunzhisheng.vui.assistant.preference.SessionPreferenceOfIntent;
import cn.yunzhisheng.vui.assistant.util.MessageManager;
import cn.yunzhisheng.vui.assistant.util.Util;
import cn.yunzhisheng.common.util.LogUtil;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Dancindream
 * @CreateDate : 2013-9-6
 * @ModifiedBy : Dancindream
 * @ModifiedDate: 2013-9-6
 * @Modified:
 * 2013-9-6: 实现基本功能
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class SettingSession extends BaseSession {
	public static final String TAG = "SettingSession";

	/**
	 * @Author : Dancindream
	 * @CreateDate : 2013-9-6
	 * @param context
	 * @param sessionManagerHandler
	 */
	public SettingSession(Context context, Handler sessionManagerHandler) {
		super(context, sessionManagerHandler);
	}

	public void putProtocol(JSONObject jsonProtocol) {
		super.putProtocol(jsonProtocol);

		String operator = getJsonValue(mDataObject, "operator", "");
		String operands = getJsonValue(mDataObject, "operands", "");
		String mkeyQuestion = getJsonValue(mDataObject, "text", "");
		LogUtil.d(TAG, "operator: " + operator + " operands: " + operands);
		addQuestionViewText(mQuestion);
//		mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_HIDE_WINDOW);
		mAnswer = "很抱歉，暂不支持该操作";
		if (SessionPreference.VALUE_SETTING_OBJ_3G.equals(operands)) {
//			if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
//				mAnswer = "已为你打开移动网络";
//				RomControl.enterControl(mContext, RomControl.ROM_OPEN_3G);
//			} else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
//				mAnswer = "已为你关闭移动网络";
//				RomControl.enterControl(mContext, RomControl.ROM_CLOSE_3G);
//			}
		} else if (SessionPreference.VALUE_SETTING_OBJ_VOLUMN.equals(operands)) {
			/*if (SessionPreference.VALUE_SETTING_ACT_INCREASE.equals(operator)) {
				mAnswer = "已为你增大音量";
				RomControl.enterControl(mContext, RomControl.ROM_INCREASE_VOLUMNE, 5);
			} else if (SessionPreference.VALUE_SETTING_ACT_DECREASE.equals(operator)) {
				mAnswer = "已为你减小音量";
				RomControl.enterControl(mContext, RomControl.ROM_DECREASE_VOLUMNE, 5);
			}*/

		} else if (SessionPreference.VALUE_SETTING_OBJ_CHANNEL
				.equals(operands)) {
			if (SessionPreference.VALUE_SETTING_ACT_NEXT.equals(operator)) {
				mAnswer = "换到下一个频道";
				RomControl.enterControl(mContext,
						RomControl.ROM_INCREASE_CHANNEL, "");
			} else if (SessionPreference.VALUE_SETTING_ACT_PREV
					.equals(operator)) {
				mAnswer = "换到上一个频道";
				RomControl.enterControl(mContext,
						RomControl.ROM_DECREASE_CHANNEL, "");
			}
		} else if (SessionPreference.VALUE_SETTING_OBJ_SS_TV.equals(operands)) {
			if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
				mAnswer = mContext.getResources().getString(Res.string.tv_other_errorAnswer);
			}else if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
				mAnswer = mContext.getResources().getString(Res.string.tv_other_errorAnswer);
			}
			mAnswer = Util.appendAnswer(mAnswer, KnowledgeMode.getKnowledgeHelpAnswer(mContext, KnowledgeMode.KNOWLEDGE_NOSUPPORT));
		} else if (SessionPreference.VALUE_SETTING_OBJ_AUTOLIGHT.equals(operands)) {
			if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
			} else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
			}
			mAnswer = "已为你打开显示设置";
			RomControl.enterControl(mContext, RomControl.ROM_OPEN_DISPLAY_SETTINGS);
		} else if (SessionPreference.VALUE_SETTING_OBJ_BLUETOOTH.equals(operands)) {
			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			if(adapter == null){
				mAnswer = "该设备不支持蓝牙功能";
			}else{
				if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
					mAnswer = "已打开蓝牙";
					RomControl.enterControl(mContext, RomControl.ROM_OPEN_BLUETOOTH);
				} else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
					mAnswer = "已关闭蓝牙";
					RomControl.enterControl(mContext, RomControl.ROM_CLOSE_BLUETOOTH);
				}
			}
			
		} else if (SessionPreference.VALUE_SETTING_OBJ_FACE.equals(operands)) {
			if (SessionPreference.VALUE_SETTING_ACT_SET.equals(operator)) {
			}
		} else if (SessionPreference.VALUE_SETTING_OBJ_TIME.equals(operands)) {
			if (SessionPreference.VALUE_SETTING_ACT_SET.equals(operator)) {
				mAnswer = "已为你打开时间设置";
				RomControl.enterControl(mContext, RomControl.ROM_OPEN_TIME_SETTINGS);
			}
		} else if (SessionPreference.VALUE_SETTING_OBJ_FLOW.equals(operands)) {
			if (SessionPreference.VALUE_SETTING_ACT_LOOKUP.equals(operator)) {
			}
		} else if (SessionPreference.VALUE_SETTING_OBJ_GPS.equals(operands)) {
			if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
			} else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
			}
		} else if (SessionPreference.VALUE_SETTING_OBJ_LIGHT.equals(operands)) {
			if (SessionPreference.VALUE_SETTING_ACT_INCREASE.equals(operator)) {
				mAnswer = "已为你增大亮度";
				RomControl.enterControl(mContext, RomControl.ROM_INCREASE_LIGHT, 10);
			} else if (SessionPreference.VALUE_SETTING_ACT_DECREASE.equals(operator)) {
				mAnswer = "已为你减小亮度";
				RomControl.enterControl(mContext, RomControl.ROM_DECREASE_LIGHT, 10);
			} else if (SessionPreference.VALUE_SETTING_ACT_MAX.equals(operator)) {
			} else if (SessionPreference.VALUE_SETTING_ACT_MIN.equals(operator)) {
			}
			//mAnswer = "已为你打开显示设置";
			//RomControl.enterControl(mContext, RomControl.ROM_OPEN_DISPLAY_SETTINGS);
		} else if (SessionPreference.VALUE_SETTING_OBJ_MEMORY.equals(operands)) {
			if (SessionPreference.VALUE_SETTING_ACT_CLEAR.equals(operator)) {
			}
		} else if (SessionPreference.VALUE_SETTING_OBJ_VOLUMN.equals(operands)) {
			if (SessionPreference.VALUE_SETTING_ACT_INCREASE.equals(operator)) {
			} else if (SessionPreference.VALUE_SETTING_ACT_DECREASE.equals(operator)) {
			} else if (SessionPreference.VALUE_SETTING_ACT_MAX.equals(operator)) {
			} else if (SessionPreference.VALUE_SETTING_ACT_MIN.equals(operator)) {
			}
		} else if (SessionPreference.VALUE_SETTING_OBJ_MODEL_GUEST.equals(operands)) {
			if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
			} else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
			}
		} else if (SessionPreference.VALUE_SETTING_OBJ_MODEL_INAIR.equals(operands)) {
//			if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
//				mAnswer = "设置为飞行模式，需要一定的时间，请耐心等待";
//				RomControl.enterControl(mContext, RomControl.ROM_OPEN_MODEL_INAIR);
//			} else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
//				mAnswer = "关闭飞行模式，需要一定的时间，请耐心等待";
//				RomControl.enterControl(mContext, RomControl.ROM_CLOSE_MODEL_INAIR);
//			}
		} else if (SessionPreference.VALUE_SETTING_OBJ_MODEL_INCAR.equals(operands)) {
			if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
			} else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
			}
		} else if (SessionPreference.VALUE_SETTING_OBJ_MODEL_MUTE.equals(operands)) {
			/*if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
				mAnswer = "已设置为静音模式";
				RomControl.enterControl(mContext, RomControl.ROM_OPEN_MODEL_MUTE);
			} else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
				mAnswer = "已关闭静音模式";
				RomControl.enterControl(mContext, RomControl.ROM_CLOSE_MODEL_MUTE);
			}*/
		} else if (SessionPreference.VALUE_SETTING_OBJ_MODEL_OUTDOOR.equals(operands)) {
			if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
			} else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
			}
		} else if (SessionPreference.VALUE_SETTING_OBJ_MODEL_OUTDOOR.equals(operands)) {
			if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
			} else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
			}
		} else if (SessionPreference.VALUE_SETTING_OBJ_MODEL_SAVEFLOW.equals(operands)) {
			if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
			} else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
			}
		} else if (SessionPreference.VALUE_SETTING_OBJ_MODEL_STANDARD.equals(operands)) {
			if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
			} else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
			}
		} else if (SessionPreference.VALUE_SETTING_OBJ_MODEL_VIBRA.equals(operands)) {
//			if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
//				mAnswer = "已设置为振动模式";
//				RomControl.enterControl(mContext, RomControl.ROM_OPEN_MODEL_VIBRA);
//			} else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
//				mAnswer = "已关闭振动模式";
//				RomControl.enterControl(mContext, RomControl.ROM_CLOSE_MODEL_VIBRA);
//			}
		} else if (SessionPreference.VALUE_SETTING_OBJ_RINGTONE.equals(operands)) {
			if (SessionPreference.VALUE_SETTING_ACT_SET.equals(operator)) {
				mAnswer = "已为你打开声音设置";
				RomControl.enterControl(mContext, RomControl.ROM_OPEN_SOUND_SETTINGS);
			}
		} else if (SessionPreference.VALUE_SETTING_OBJ_ROTATION.equals(operands)) {
//			if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
//				mAnswer = "已打开自动旋转屏幕";
//				RomControl.enterControl(mContext, RomControl.ROM_OPEN_ROTATION);
//			} else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
//				mAnswer = "已关闭自动旋转屏幕";
//				RomControl.enterControl(mContext, RomControl.ROM_CLOSE_ROTATION);
//			}
		} else if (SessionPreference.VALUE_SETTING_OBJ_WALLPAPER.equals(operands)) {
//			if (SessionPreference.VALUE_SETTING_ACT_SET.equals(operator)) {
//				mAnswer = "已为你打开壁纸设置";
//				RomControl.enterControl(mContext, RomControl.ROM_OPEN_WALLPAPER_SETTINGS);
//			}
		} else if (SessionPreference.VALUE_SETTING_OBJ_WIFI.equals(operands)) {
			if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
				mAnswer = "已打开WIFI";
				RomControl.enterControl(mContext, RomControl.ROM_OPEN_WIFI);
			} else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
				mAnswer = "已关闭WIFI";
				RomControl.enterControl(mContext, RomControl.ROM_CLOSE_WIFI);
			}
		} else if (SessionPreference.VALUE_SETTING_OBJ_WIFI_SPOT.equals(operands)) {
//			if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
//				mAnswer = "已打开WIFI热点";
//				RomControl.enterControl(mContext, RomControl.ROM_OPEN_WIFI_SPOT);
//			} else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
//				mAnswer = "已关闭WIFI热点";
//				RomControl.enterControl(mContext, RomControl.ROM_CLOSE_WIFI_SPOT);
//			}
		} else if (SessionPreference.VALUE_SETTING_ACT_FASTF.equals(operator)){
			//快进
			/*String time = getJsonValue(mDataObject, SessionPreference.KEY_TIME_DELTA);
			mAnswer = "已为您快进";
			Bundle bundle = new Bundle();
			bundle.putInt(SessionPreferenceOfIntent.KEY_KIND, 0);
			bundle.putLong(SessionPreferenceOfIntent.KEY_TIME, getSByTime(time.split(":")));
			showMessage(SessionPreferenceOfIntent.INTENT_PLAY_CONTROL, bundle);*/
			
		} else if(SessionPreference.VALUE_SETTING_ACT_FASTB.equals(operator)){
			//快退
			/*String time = getJsonValue(mDataObject, SessionPreference.KEY_TIME_DELTA);
			mAnswer = "已为您快退";
			Bundle bundle = new Bundle();
			bundle.putInt(SessionPreferenceOfIntent.KEY_KIND, 1);
			bundle.putLong(SessionPreferenceOfIntent.KEY_TIME, getSByTime(time.split(":")));
			showMessage(SessionPreferenceOfIntent.INTENT_PLAY_CONTROL, bundle);*/
		} else if(SessionPreference.VALUE_SETTING_ACT_PLAY.equals(operator)){
			
			String time = getJsonValue(mDataObject, SessionPreference.KEY_TIME);
			
			Bundle bundle = new Bundle();
			if(TextUtils.isEmpty(time)){
				//播放
				mAnswer = "已为您执行播放";
				bundle.putInt(SessionPreferenceOfIntent.KEY_KIND, 3);
				showMessage(SessionPreferenceOfIntent.INTENT_PLAY_CONTROL, bundle);
			}else{
				//定位播放
				mAnswer = "已为您执行定位播放";
				bundle.putInt(SessionPreferenceOfIntent.KEY_KIND, 2);
				bundle.putLong(SessionPreferenceOfIntent.KEY_TIME, getSByTime(time.split(":")));
				showMessage(SessionPreferenceOfIntent.INTENT_PLAY_CONTROL, bundle);
			}
		} else if(SessionPreference.VALUE_SETTING_ACT_PAUSE.equals(operator)){ 
			//暂停
			mAnswer = "已为您执行暂停";
			Bundle bundle = new Bundle();
			bundle.putInt(SessionPreferenceOfIntent.KEY_KIND, 4);
			showMessage(SessionPreferenceOfIntent.INTENT_PLAY_CONTROL, bundle);
		} else if(SessionPreference.VALUE_SETTING_ACT_NEXT.equals(operator)){
			//下一集
			/*mAnswer = "已为您执行下一集";
			Bundle bundle = new Bundle();
			bundle.putInt(SessionPreferenceOfIntent.KEY_KIND, 5);
			showMessage(SessionPreferenceOfIntent.INTENT_PLAY_CONTROL, bundle);*/
		} else if(SessionPreference.VALUE_SETTING_ACT_PREV.equals(operator)){
			//上一集
			/*mAnswer = "已为您执行上一集";
			Bundle bundle = new Bundle();
			bundle.putInt(SessionPreferenceOfIntent.KEY_KIND, 6);
			showMessage(SessionPreferenceOfIntent.INTENT_PLAY_CONTROL, bundle);*/
		} else if (operator.equals(SessionPreference.VALUE_SETTING_ACT_ARROW_UP)){
			//mAnswer = "已经为你处理向上";
			sendVirtualKeyMessage(KeyEvent.KEYCODE_DPAD_UP);
		} else if (operator.equals(SessionPreference.VALUE_SETTING_ACT_ARROW_DOWN)){
			mAnswer = "已经为你处理" + "\"" + mkeyQuestion + "\"";
			sendVirtualKeyMessage(KeyEvent.KEYCODE_DPAD_DOWN);
		} else if (operator.equals(SessionPreference.VALUE_SETTING_ACT_ARROW_LEFT)){
			mAnswer = "已经为你处理" + "\"" + mkeyQuestion + "\"";
			sendVirtualKeyMessage(KeyEvent.KEYCODE_DPAD_LEFT);
		} else if (operator.equals(SessionPreference.VALUE_SETTING_ACT_ARROW_RIGHT)){
			mAnswer = "已经为你处理" + "\"" + mkeyQuestion + "\"";
			sendVirtualKeyMessage(KeyEvent.KEYCODE_DPAD_RIGHT);
		} else if (operator.equals(SessionPreference.VALUE_SETTING_ACT_BACK)){
			mAnswer = "已经为你处理" + "\"" + mkeyQuestion + "\"";
			sendVirtualKeyMessage(KeyEvent.KEYCODE_BACK);
		} else if (operator.equals(SessionPreference.VALUE_SETTING_ACT_PAGE_UP)){
			mAnswer = "已经为你处理" + "\"" + mkeyQuestion + "\"";
			sendVirtualKeyMessage(KeyEvent.KEYCODE_PAGE_UP);
		} else if (operator.equals(SessionPreference.VALUE_SETTING_ACT_PAGE_DOWN)){
			mAnswer = "已经为你处理" + "\"" + mkeyQuestion + "\"";
			sendVirtualKeyMessage(KeyEvent.KEYCODE_PAGE_DOWN);
		} else if (operands.equals(SessionPreference.VALUE_SETTING_OBJ_MENU)){
			mAnswer = "已经为你处理" + "\"" + mkeyQuestion + "\"";
			sendVirtualKeyMessage(KeyEvent.KEYCODE_MENU);
		} else if (operands.equals(SessionPreference.VALUE_SETTING_OBJ_HOME)){
			mAnswer = "已经为你处理" + "\"" + mkeyQuestion + "\"";
			sendVirtualKeyMessage(KeyEvent.KEYCODE_HOME);
		} else if (getJsonValue(mDataObject, "confirm", "").equals("OK")){
			mAnswer = "已经为你处理" + "\"" + mkeyQuestion + "\"";
			sendVirtualKeyMessage(KeyEvent.KEYCODE_DPAD_CENTER);
		} else if("OBJ_BIANXINGJINGGANG".equals(operands)){
			if("ACT_OPEN_BIANXINGJINGGANG".equals(operator)){
				Toast.makeText(mContext, "我要买变形金刚", Toast.LENGTH_SHORT).show();
				mAnswer = "我要买变形金刚";
				Intent openpicture = new Intent();	  		   
	  			openpicture.setAction("cn.open.picture.bianxingjingang");
	  			mContext.sendBroadcast(openpicture);
			
			}
		}else if("OBJ_ALI_MOVIE".equals(operands)){
			if("ACT_OPEN_ALI_MOVIE".equals(operator)){
				Toast.makeText(mContext, "我要看电影", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看电影";
				Intent opendianying = new Intent();	  		   
				opendianying.setAction("cn.open.picture.dianying");
	  			mContext.sendBroadcast(opendianying);
			}
		}
		else if("OBJ_ALI_JU".equals(operands)){
			if("ACT_OPEN_ALI_JU".equals(operator)){
				Toast.makeText(mContext, "我要看电视剧", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看电视剧";
				/*Intent intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setClass(mContext, ShowMoviePictureActivity.class);	
				intent.putExtra("onlinewatch", 4);
				mContext.startActivity(intent);*/
				Intent opendianshiju = new Intent();	  		   
				opendianshiju.setAction("cn.open.picture.dianshiju");
	  			mContext.sendBroadcast(opendianshiju);
			}
		}
		else if("OBJ_ALI_SHIMEN".equals(operands)){
			if("ACT_OPEN_ALI_SHIMEN".equals(operator)){
				Toast.makeText(mContext, "我要看影视精选", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看影视精选";
				Intent openyingshixuan = new Intent();	  		   
				openyingshixuan.setAction("cn.open.picture.yingshijingxuan");
	  			mContext.sendBroadcast(openyingshixuan);
			}
		}
		else if("OBJ_ALI_BLUARY".equals(operands)){
			if("ACT_OPEN_ALI_BLUARY".equals(operator)){
				Toast.makeText(mContext, "我要看蓝光", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看蓝光";				
				Intent openbluary = new Intent();	  		   
				openbluary.setAction("cn.open.picture.bluary");
	  			mContext.sendBroadcast(openbluary);
			}
		}
		else if("OBJ_ALI_QIUSUO".equals(operands)){
			if("ACT_OPEN_ALI_QIUSUO".equals(operator)){
				Toast.makeText(mContext, "我要看求索", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看求索";			
				Intent openqiusuo = new Intent();	  		   
				openqiusuo.setAction("cn.open.picture.qiusuo");
	  			mContext.sendBroadcast(openqiusuo);
			}
		}
		else if("OBJ_ALI_CHILD".equals(operands)){
			if("ACT_OPEN_ALI_CHILD".equals(operator)){
				Toast.makeText(mContext, "我要看动漫", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看动漫";			
				Intent openchild = new Intent();	  		   
				openchild.setAction("cn.open.picture.child");
	  			mContext.sendBroadcast(openchild);
			}
		}
		else if("OBJ_ALI_EADUCATION".equals(operands)){
			if("ACT_OPEN_ALI_EADUCATION".equals(operator)){
				Toast.makeText(mContext, "我要看教育", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看教育";		
				Intent openedu = new Intent();	  		   
				openedu.setAction("cn.open.picture.education");
	  			mContext.sendBroadcast(openedu);
			}
		}
		else if("OBJ_ALI_GAME".equals(operands)){
			if("ACT_OPEN_ALI_GAME".equals(operator)){
				Toast.makeText(mContext, "打开游戏", Toast.LENGTH_SHORT).show();
				mAnswer = "打开游戏";			
				Intent opengame = new Intent();	  		   
				opengame.setAction("cn.open.picture.game");
	  			mContext.sendBroadcast(opengame);
			}
		}
		else if("OBJ_ALI_APP".equals(operands)){
			if("ACT_OPEN_ALI_APP".equals(operator)){
				Toast.makeText(mContext, "打开应用", Toast.LENGTH_SHORT).show();
				mAnswer = "打开应用";	
				Intent openapp = new Intent();	  		   
				openapp.setAction("cn.open.picture.app");
	  			mContext.sendBroadcast(openapp);
			}
		}
		else if("OBJ_ALI_SHOP".equals(operands)){
			if("ACT_OPEN_ALI_SHOP".equals(operator)){
				Toast.makeText(mContext, "我要购物", Toast.LENGTH_SHORT).show();
				mAnswer = "我要购物";		
				Intent openshop = new Intent();	  		   
				openshop.setAction("cn.open.picture.shop");
	  			mContext.sendBroadcast(openshop);
			}
		}
		else if("OBJ_ALI_MUSIC".equals(operands)){
			if("ACT_OPEN_ALI_MUSIC".equals(operator)){
				Toast.makeText(mContext, "打开音乐", Toast.LENGTH_SHORT).show();
				mAnswer = "打开音乐";		
				Intent openmusic = new Intent();	  		   
				openmusic.setAction("cn.open.picture.music");
	  			mContext.sendBroadcast(openmusic);
			}
		}
		else if("OBJ_ALI_TIANLAI".equals(operands)){
			if("ACT_OPEN_ALI_TIANLAI".equals(operator)){
				Toast.makeText(mContext, "我要唱歌", Toast.LENGTH_SHORT).show();
				mAnswer = "我要唱歌";		
				Intent opentianlai = new Intent();	  		   
				opentianlai.setAction("cn.open.picture.tianlai");
	  			mContext.sendBroadcast(opentianlai);
			}
		}	
		else if("OBJ_ALI_MV1".equals(operands)){
			if("ACT_OPEN_ALI_MV1".equals(operator)){
				Toast.makeText(mContext, "我要看地心引力", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看地心引力";		
				Intent openmovie1 = new Intent();	  		   
				openmovie1.setAction("cn.open.picture.movie1");
				Log.d(TAG,"openmovie1111");
	  			mContext.sendBroadcast(openmovie1);
			}
		}	
		else if("OBJ_ALI_MV2".equals(operands)){
			if("ACT_OPEN_ALI_MV2".equals(operator)){
				Toast.makeText(mContext, "我要看暮光之城", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看暮光之城";		
				Intent openmovie2 = new Intent();	  		   
				openmovie2.setAction("cn.open.picture.movie2");
	  			mContext.sendBroadcast(openmovie2);
			}
		}	
		else if("OBJ_ALI_MV3".equals(operands)){
			if("ACT_OPEN_ALI_MV3".equals(operator)){
				Toast.makeText(mContext, "我要看环太平洋", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看环太平洋";		
				Intent openmovie3 = new Intent();	  		   
				openmovie3.setAction("cn.open.picture.movie3");
	  			mContext.sendBroadcast(openmovie3);
			}
		}	
		else if("OBJ_ALI_MV4".equals(operands)){
			if("ACT_OPEN_ALI_MV4".equals(operator)){
				Toast.makeText(mContext, "我要看激情与速度", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看激情与速度";		
				Intent openmovie4 = new Intent();	  		   
				openmovie4.setAction("cn.open.picture.movie4");
	  			mContext.sendBroadcast(openmovie4);
			}
		}	
		else if("OBJ_ALI_MV5".equals(operands)){
			if("ACT_OPEN_ALI_MV5".equals(operator)){
				Toast.makeText(mContext, "我要看未来战警", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看未来战警";		
				Intent openmovie5 = new Intent();	  		   
				openmovie5.setAction("cn.open.picture.movie5");
	  			mContext.sendBroadcast(openmovie5);
			}
		}
		else if("OBJ_ALI_MV6".equals(operands)){
			if("ACT_OPEN_ALI_MV6".equals(operator)){
				Toast.makeText(mContext, "我要看潜龙风云", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看潜龙风云";		
				Intent openmovie6 = new Intent();	  		   
				openmovie6.setAction("cn.open.picture.movie6");
	  			mContext.sendBroadcast(openmovie6);
			}
		}
		else if("OBJ_ALI_MV7".equals(operands)){
			if("ACT_OPEN_ALI_MV7".equals(operator)){
				Toast.makeText(mContext, "我要看食人虫", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看食人虫";		
				Intent openmovie7 = new Intent();	  		   
				openmovie7.setAction("cn.open.picture.movie7");
	  			mContext.sendBroadcast(openmovie7);
			}
		}
		else if("OBJ_ALI_MV8".equals(operands)){
			if("ACT_OPEN_ALI_MV8".equals(operator)){
				Toast.makeText(mContext, "我要看特殊身份", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看特殊身份";		
				Intent openmovie7 = new Intent();	  		   
				openmovie7.setAction("cn.open.picture.movie8");
	  			mContext.sendBroadcast(openmovie7);
			}
		}
		else if("OBJ_ALI_MV9".equals(operands)){
			if("ACT_OPEN_ALI_MV9".equals(operator)){
				Toast.makeText(mContext, "我要看疯狂原始人", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看疯狂原始人";		
				Intent openmovie7 = new Intent();	  		   
				openmovie7.setAction("cn.open.picture.movie9");
	  			mContext.sendBroadcast(openmovie7);
			}
		}
		else if("OBJ_ALI_MV10".equals(operands)){
			if("ACT_OPEN_ALI_MV10".equals(operator)){
				Toast.makeText(mContext, "我要看深夜前的五分钟", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看深夜前的五分钟";		
				Intent openmovie7 = new Intent();	  		   
				openmovie7.setAction("cn.open.picture.movie10");
	  			mContext.sendBroadcast(openmovie7);
			}
		}
		else if("OBJ_ALI_MV11".equals(operands)){
			if("ACT_OPEN_ALI_MV11".equals(operator)){
				Toast.makeText(mContext, "我要看王者归来", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看王者归来";		
				Intent openmovie7 = new Intent();	  		   
				openmovie7.setAction("cn.open.picture.movie11");
	  			mContext.sendBroadcast(openmovie7);
			}
		}
		else if("OBJ_ALI_DU1".equals(operands)){
			if("ACT_OPEN_ALI_DJU1".equals(operator)){
				Toast.makeText(mContext, "我要看满仓进城", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看满仓进城";		
				Intent openju1 = new Intent();	  		   
				openju1.setAction("cn.open.picture.dianshiju1");
	  			mContext.sendBroadcast(openju1);
			}
		}	
		else if("OBJ_ALI_DJU2".equals(operands)){
			if("ACT_OPEN_ALI_DJU2".equals(operator)){
				Toast.makeText(mContext, "我要看半路父子", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看半路父子";		
				Intent openju2 = new Intent();	  		   
				openju2.setAction("cn.open.picture.dianshiju2");
	  			mContext.sendBroadcast(openju2);
			}
		}	
		else if("OBJ_ALI_DJU3".equals(operands)){
			if("ACT_OPEN_ALI_DJU3".equals(operator)){
				Toast.makeText(mContext, "我要看第二次人生", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看第二次人生";		
				Intent openju3= new Intent();	  		   
				openju3.setAction("cn.open.picture.dianshiju3");
	  			mContext.sendBroadcast(openju3);
			}
		}
		else if("OBJ_ALI_DJU4".equals(operands)){
			if("ACT_OPEN_ALI_DJU4".equals(operator)){
				Toast.makeText(mContext, "我要看红高粱", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看红高粱";		
				Intent openju4 = new Intent();	  		   
				openju4.setAction("cn.open.picture.dianshiju4");
	  			mContext.sendBroadcast(openju4);
			}
		}
		else if("OBJ_ALI_DJU5".equals(operands)){
			if("ACT_OPEN_ALI_DJU5".equals(operator)){
				Toast.makeText(mContext, "我要看一代枭雄", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看一代枭雄";		
				Intent openju5 = new Intent();	  		   
				openju5.setAction("cn.open.picture.dianshiju5");
	  			mContext.sendBroadcast(openju5);
			}
		}
		else if("OBJ_ALI_DJU6".equals(operands)){
			if("ACT_OPEN_ALI_DJU6".equals(operator)){
				Toast.makeText(mContext, "我要看美人心计", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看美人心计";		
				Intent openju5 = new Intent();	  		   
				openju5.setAction("cn.open.picture.dianshiju6");
	  			mContext.sendBroadcast(openju5);
			}
		}
		else if("OBJ_ALI_DJU7".equals(operands)){
			if("ACT_OPEN_ALI_DJU7".equals(operator)){
				Toast.makeText(mContext, "我要看草帽警察", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看草帽警察";		
				Intent openju5 = new Intent();	  		   
				openju5.setAction("cn.open.picture.dianshiju7");
	  			mContext.sendBroadcast(openju5);
			}
		}
		else if("OBJ_ALI_DJU8".equals(operands)){
			if("ACT_OPEN_ALI_DJU8".equals(operator)){
				Toast.makeText(mContext, "我要看错伏", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看错服";		
				Intent openju5 = new Intent();	  		   
				openju5.setAction("cn.open.picture.dianshiju8");
	  			mContext.sendBroadcast(openju5);
			}
		}
		else if("OBJ_ALI_DJU9".equals(operands)){
			if("ACT_OPEN_ALI_DJU9".equals(operator)){
				Toast.makeText(mContext, "我要看老农民", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看老农民";		
				Intent openju5 = new Intent();	  		   
				openju5.setAction("cn.open.picture.dianshiju9");
	  			mContext.sendBroadcast(openju5);
			}
		}
		else if("OBJ_ALI_DJU10".equals(operands)){
			if("ACT_OPEN_ALI_DJU10".equals(operator)){
				Toast.makeText(mContext, "我要看金玉良缘", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看金玉良缘";		
				Intent openju5 = new Intent();	  		   
				openju5.setAction("cn.open.picture.dianshiju10");
	  			mContext.sendBroadcast(openju5);
			}
		}
		else if("OBJ_ALI_CHILD1".equals(operands)){
			if("ACT_OPEN_ALI_CHILD1".equals(operator)){
				Toast.makeText(mContext, "巧虎来了", Toast.LENGTH_SHORT).show();
				mAnswer = "巧虎来了";		
				Intent opench1 = new Intent();	  		   
				opench1.setAction("cn.open.picture.child1");
	  			mContext.sendBroadcast(opench1);
			}
		}	
		else if("OBJ_ALI_CHILD2".equals(operands)){
			if("ACT_OPEN_ALI_CHILD2".equals(operator)){
				Toast.makeText(mContext, "我要看天线宝宝", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看天线宝宝";		
				Intent opench2 = new Intent();	  		   
				opench2.setAction("cn.open.picture.child2");
	  			mContext.sendBroadcast(opench2);
			}
		}	
		else if("OBJ_ALI_CHILD3".equals(operands)){
			if("ACT_OPEN_ALI_CHILD3".equals(operator)){
				Toast.makeText(mContext, "我要看海绵宝宝", Toast.LENGTH_SHORT).show();
				mAnswer = "我要看海绵宝宝";		
				Intent opench3 = new Intent();	  		   
				opench3.setAction("cn.open.picture.child3");
	  			mContext.sendBroadcast(opench3);
			}
		}	
		else if("OBJ_ALI_SHOP1".equals(operands)){
			if("ACT_OPEN_ALI_SHOP1".equals(operator)){
				Toast.makeText(mContext, "我要买干果", Toast.LENGTH_SHORT).show();
				mAnswer = "我要买干果";		
				Intent openshop1 = new Intent();	  		   
				openshop1.setAction("cn.open.picture.shop1");
	  			mContext.sendBroadcast(openshop1);
			}
		}	
		else if("OBJ_ALI_SHOP2".equals(operands)){
			if("ACT_OPEN_ALI_SHOP2".equals(operator)){
				Toast.makeText(mContext, "我要买衣服", Toast.LENGTH_SHORT).show();
				mAnswer = "我要买衣服";		
				Intent openshop2 = new Intent();	  		   
				openshop2.setAction("cn.open.picture.shop2");
	  			mContext.sendBroadcast(openshop2);
			}
		}	
		else if("OBJ_ALI_SHOP3".equals(operands)){
			if("ACT_OPEN_ALI_SHOP3".equals(operator)){
				Toast.makeText(mContext, "我要买苹果", Toast.LENGTH_SHORT).show();
				mAnswer = "我要买苹果";		
				Intent openshop3 = new Intent();	  		   
				openshop3.setAction("cn.open.picture.shop3");
	  			mContext.sendBroadcast(openshop3);
			}
		}
		else if("OBJ_ALI_SHOP4".equals(operands)){
			if("ACT_OPEN_ALI_SHOP4".equals(operator)){
				Toast.makeText(mContext, "我要买新百伦运动鞋", Toast.LENGTH_SHORT).show();
				mAnswer = "我要买新百伦运动鞋";		
				Intent openshop4 = new Intent();	  		   
				openshop4.setAction("cn.open.picture.shop4");
	  			mContext.sendBroadcast(openshop4);
			}
		}
		else if("OBJ_ALI_SHOP5".equals(operands)){
			if("ACT_OPEN_ALI_SHOP5".equals(operator)){
				Toast.makeText(mContext, "我要买白色男士阿迪达斯运动鞋", Toast.LENGTH_SHORT).show();
				mAnswer = "我要买白色男士阿迪达斯运动鞋";		
				Intent openshop5 = new Intent();	  		   
				openshop5.setAction("cn.open.picture.shop5");
	  			mContext.sendBroadcast(openshop5);
			}
		}
		else if("OBJ_ALI_SHOP6".equals(operands)){
			if("ACT_OPEN_ALI_SHOP6".equals(operator)){
				Toast.makeText(mContext, "我要买新疆核桃", Toast.LENGTH_SHORT).show();
				mAnswer = "我要买新疆核桃";		
				Intent openshop6 = new Intent();	  		   
				openshop6.setAction("cn.open.picture.shop6");
	  			mContext.sendBroadcast(openshop6);
			}
		}
		else if("OBJ_ALI_SHOP7".equals(operands)){
			if("ACT_OPEN_ALI_SHOP7".equals(operator)){
				Toast.makeText(mContext, "我要买新疆大枣", Toast.LENGTH_SHORT).show();
				mAnswer = "我要买新疆大枣";		
				Intent openshop7 = new Intent();	  		   
				openshop7.setAction("cn.open.picture.shop7");
	  			mContext.sendBroadcast(openshop7);
			}
		}
		else if("OBJ_ALI_SHOP8".equals(operands)){
			if("ACT_OPEN_ALI_SHOP8".equals(operator)){
				Toast.makeText(mContext, "我要买马桶", Toast.LENGTH_SHORT).show();
				mAnswer = "我要买马桶";		
				Intent openshop8 = new Intent();	  		   
				openshop8.setAction("cn.open.picture.shop8");
	  			mContext.sendBroadcast(openshop8);
			}
		}
		else if("OBJ_ALI_SHOP9".equals(operands)){
			if("ACT_OPEN_ALI_SHOP9".equals(operator)){
				Toast.makeText(mContext, "我要买窗帘", Toast.LENGTH_SHORT).show();
				mAnswer = "我要买窗帘";		
				Intent openshop9 = new Intent();	  		   
				openshop9.setAction("cn.open.picture.shop9");
	  			mContext.sendBroadcast(openshop9);
			}
		}
		else if("OBJ_ALI_SHOP10".equals(operands)){
			if("ACT_OPEN_ALI_SHOP10".equals(operator)){
				Toast.makeText(mContext, "我要买山地自行车", Toast.LENGTH_SHORT).show();
				mAnswer = "我要买山地自行车";		
				Intent openshop10 = new Intent();	  		   
				openshop10.setAction("cn.open.picture.shop10");
	  			mContext.sendBroadcast(openshop10);
			}
		}
		else if("OBJ_ALI_SHOP11".equals(operands)){
			if("ACT_OPEN_ALI_SHOP11".equals(operator)){
				Toast.makeText(mContext, "我要买彩票", Toast.LENGTH_SHORT).show();
				mAnswer = "我要买彩票";		
				Intent openshop11 = new Intent();	  		   
				openshop11.setAction("cn.open.picture.shop11");
	  			mContext.sendBroadcast(openshop11);
			}
		}
		else if("OBJ_ALI_MUSIC13".equals(operands)){
			if("ACT_OPEN_ALI_MUSIC13".equals(operator)){
				Toast.makeText(mContext, "我要听邓紫棋的泡沫", Toast.LENGTH_SHORT).show();
				mAnswer = "我要听邓紫棋的泡沫";		
				Intent openmusic1 = new Intent();	  		   
				openmusic1.setAction("cn.open.music.paomo");
	  			mContext.sendBroadcast(openmusic1);
			}
		}
		else if("OBJ_ALI_MUSIC12".equals(operands)){
			if("ACT_OPEN_ALI_MUSIC12".equals(operator)){
				Toast.makeText(mContext, "我要听邓紫棋的喜欢你", Toast.LENGTH_SHORT).show();
				mAnswer = "我要听邓紫棋的喜欢你";		
				Intent openmusic2 = new Intent();	  		   
				openmusic2.setAction("cn.open.music.xihuanni");
	  			mContext.sendBroadcast(openmusic2);
			}
		}
		else if("OBJ_ALI_MUSIC3".equals(operands)){
			if("ACT_OPEN_ALI_MUSIC3".equals(operator)){
				Toast.makeText(mContext, "我要听彭丽媛的歌", Toast.LENGTH_SHORT).show();
				mAnswer = "我要听彭丽媛的歌";		
				Intent openmusic3 = new Intent();	  		   
				openmusic3.setAction("cn.open.music.zaixiwangdetianyeshang");
	  			mContext.sendBroadcast(openmusic3);
			}
		}
		else if("OBJ_ALI_MUSIC4".equals(operands)){
			if("ACT_OPEN_ALI_MUSIC4".equals(operator)){
				Toast.makeText(mContext, "我要听北京北京", Toast.LENGTH_SHORT).show();
				mAnswer = "我要听北京北京";		
				Intent openmusic4 = new Intent();	  		   
				openmusic4.setAction("cn.open.music.beijingbeijing");
	  			mContext.sendBroadcast(openmusic4);
			}
		}
		else if("OBJ_ALI_MUSIC11".equals(operands)){
			if("ACT_OPEN_ALI_MUSIC11".equals(operator)){
				Toast.makeText(mContext, "我要听匆匆那年", Toast.LENGTH_SHORT).show();
				mAnswer = "我要听匆匆那年";		
				Intent openmusic5 = new Intent();	  		   
				openmusic5.setAction("cn.open.music.chongchongnanian");
	  			mContext.sendBroadcast(openmusic5);
			}
		}
		else if("OBJ_ALI_MUSIC14".equals(operands)){
			if("ACT_OPEN_ALI_MUSIC14".equals(operator)){
				Toast.makeText(mContext, "我要听漂洋过海来看你", Toast.LENGTH_SHORT).show();
				mAnswer = "我要听漂洋过海来看你";		
				Intent openmusic6 = new Intent();	  		   
				openmusic6.setAction("cn.open.music.piaoyangguohailaikanni");
	  			mContext.sendBroadcast(openmusic6);
			}
		}
		else if("OBJ_ALI_MUSIC6".equals(operands)){
			if("ACT_OPEN_ALI_MUSIC6".equals(operator)){
				Toast.makeText(mContext, "我要听何以爱情", Toast.LENGTH_SHORT).show();
				mAnswer = "我要听何以爱情";		
				Intent openmusic7 = new Intent();	  		   
				openmusic7.setAction("cn.open.music.heyiaiqing");
	  			mContext.sendBroadcast(openmusic7);
			}
		}
		else if("OBJ_ALI_MUSIC5".equals(operands)){
			if("ACT_OPEN_ALI_MUSIC5".equals(operator)){
				Toast.makeText(mContext, "我要听时间都去哪了", Toast.LENGTH_SHORT).show();
				mAnswer = "我要听时间都去哪了";		
				Intent openmusic8 = new Intent();	  		   
				openmusic8.setAction("cn.open.music.shijiandouqunale");
	  			mContext.sendBroadcast(openmusic8);
			}
		}
		else if("OBJ_ALI_MUSIC7".equals(operands)){
			if("ACT_OPEN_ALI_MUSIC7".equals(operator)){
				Toast.makeText(mContext, "我要听小苹果", Toast.LENGTH_SHORT).show();
				mAnswer = "我要听小苹果";		
				Intent openmusic9 = new Intent();	  		   
				openmusic9.setAction("cn.open.music.xiaopingguo");
	  			mContext.sendBroadcast(openmusic9);
			}
		}
		else if("OBJ_ALI_MUSIC10".equals(operands)){
			if("ACT_OPEN_ALI_MUSIC10".equals(operator)){
				Toast.makeText(mContext, "我要听小情歌", Toast.LENGTH_SHORT).show();
				mAnswer = "我要听小情歌";		
				Intent openmusic10 = new Intent();	  		   
				openmusic10.setAction("cn.open.music.xiaoqingge");
	  			mContext.sendBroadcast(openmusic10);
			}
		}
		else if("OBJ_ALI_MUSIC8".equals(operands)){
			if("ACT_OPEN_ALI_MUSIC8".equals(operator)){
				Toast.makeText(mContext, "我要听最炫民族风", Toast.LENGTH_SHORT).show();
				mAnswer = "我要听最炫民族风";		
				Intent openmusic11 = new Intent();	  		   
				openmusic11.setAction("cn.open.music.zuixuanminzufeng");
	  			mContext.sendBroadcast(openmusic11);
			}
		}
		else if("OBJ_ALI_MUSIC15".equals(operands)){
			if("ACT_OPEN_ALI_MUSIC15".equals(operator)){
				Toast.makeText(mContext, "我要听京剧", Toast.LENGTH_SHORT).show();
				mAnswer = "我要听京剧";		
				Intent openmusic12 = new Intent();	  		   
				openmusic12.setAction("cn.open.music.jingju");
	  			mContext.sendBroadcast(openmusic12);
			}
		}
		else if("OBJ_ALI_MUSIC16".equals(operands)){
			if("ACT_OPEN_ALI_MUSIC16".equals(operator)){
				Toast.makeText(mContext, "我要听豫剧", Toast.LENGTH_SHORT).show();
				mAnswer = "我要听豫剧";		
				Intent openmusic13 = new Intent();	  		   
				openmusic13.setAction("cn.open.music.yuju");
	  			mContext.sendBroadcast(openmusic13);
			}
		}
		else if("OBJ_ALI_MUSIC17".equals(operands)){
			if("ACT_OPEN_ALI_MUSIC17".equals(operator)){
				Toast.makeText(mContext, "我要听王菲的笑忘书", Toast.LENGTH_SHORT).show();
				mAnswer = "我要听王菲的笑忘书";		
				Intent openmusic14 = new Intent();	  		   
				openmusic14.setAction("cn.open.music.xiaowangshu");
	  			mContext.sendBroadcast(openmusic14);
			}
		}
		else if("OBJ_ALI_DEMOVOIDE1".equals(operands))
		{
			if("ACT_OPEN_ALI_DEMOVOIDE1".equals(operator)){
				Toast.makeText(mContext, "私人影院", Toast.LENGTH_SHORT).show();
				mAnswer = "私人影院";		
				Intent openmusic15 = new Intent();	  		   
				openmusic15.setAction("cn.open.music.yingyuan");
	  			mContext.sendBroadcast(openmusic15);
			}
		}
		else if("OBJ_ALI_DEMOVOIDE2".equals(operands))
		{
			if("ACT_OPEN_ALI_DEMOVOIDE2".equals(operator)){
				Toast.makeText(mContext, "高清幼儿教育", Toast.LENGTH_SHORT).show();
				mAnswer = "高清幼儿教育";		
				Intent openmusic16 = new Intent();	  		   
				openmusic16.setAction("cn.open.music.jiaoyu");
	  			mContext.sendBroadcast(openmusic16);
			}
		}
		else if("OBJ_ALI_DEMOVOIDE3".equals(operands))
		{
			if("ACT_OPEN_ALI_DEMOVOIDE3".equals(operator)){
				Toast.makeText(mContext, "我要唱歌", Toast.LENGTH_SHORT).show();
				mAnswer = "我要唱歌";		
				Intent openmusic17 = new Intent();	  		   
				openmusic17.setAction("cn.open.music.changge");
	  			mContext.sendBroadcast(openmusic17);
			}
		}
		else if("OBJ_ALI_DEMOVOIDE4".equals(operands))
		{
			if("ACT_OPEN_ALI_DEMOVOIDE4".equals(operator)){
				Toast.makeText(mContext, "我的影院", Toast.LENGTH_SHORT).show();
				mAnswer = "我的影院";		
				Intent openmovie = new Intent();	  		   
				openmovie.setAction("cn.open.yingyuan");
	  			mContext.sendBroadcast(openmovie);
			}
		}
		else if("OBJ_ALI_DEMOVOIDE5".equals(operands))
		{
			if("ACT_OPEN_ALI_DEMOVOIDE5".equals(operator)){
				Toast.makeText(mContext, "打开激流快艇", Toast.LENGTH_SHORT).show();
				mAnswer = "打开激流快艇";		
				Intent openjiliu = new Intent();	  		   
				openjiliu.setAction("cn.open.jiliukuaiting");
	  			mContext.sendBroadcast(openjiliu);
			}
		}
		else {
			if (operator.equals(SessionPreference.VALUE_SETTING_ACT_OPEN_CHANNEL)) {
				
			}else if (operator.equals(SessionPreference.VALUE_SETTING_ACT_SCREEN_SHOT)) {
				mAnswer = Util.appendAnswer(mAnswer, KnowledgeMode.getKnowledgeHelpAnswer(mContext, KnowledgeMode.KNOWLEDGE_NOSUPPORT));
			}
		}
		// addQuestionViewText(mQuestion);
		addAnswerViewText(mAnswer);
		LogUtil.d(TAG, "answer: " + mAnswer);
		playTTS(mAnswer);
	}

	
	private void showMessage(String message, Bundle extras) {
		MessageManager.sendPrivateMessage(mContext, message, extras);
	}
	
	private long getSByTime(String[] timeArgs){
		long s = 0;
		int hour1,hour2,min1,min2,s1,s2;
		switch (timeArgs.length) {
		case 3:
			hour1 = Integer.parseInt(timeArgs[0].charAt(0)+"");
			hour2 = Integer.parseInt(timeArgs[0].charAt(1)+"");
			if(hour1 != 0){
				s += hour1*10*60*60;
			}
			if(hour2 != 0){
				s += hour2*60*60;
			}
			min1 = Integer.parseInt(timeArgs[1].charAt(0)+"");
			min2 = Integer.parseInt(timeArgs[1].charAt(1)+"");
			if(min1 != 0){
				s += min1*10*60;
			}
			if(min2 != 0){
				s += min2*60;
			}
			s1 = Integer.parseInt(timeArgs[2].charAt(0)+"");
			s2 = Integer.parseInt(timeArgs[2].charAt(1)+"");
			if(s1 != 0){
				s += s1*10;
			}  
			if(s2 != 0){
				s += s2;
			}
			
			break;
		case 2:
			System.out.println("length == 2");
			min1 = Integer.parseInt(timeArgs[0].charAt(0)+"");
			min2 = Integer.parseInt(timeArgs[0].charAt(1)+"");
			if(min1 != 0){
				s += min1*10*60;
			}
			if(min2 != 0){
				s += min2*60;
			}
			s1 = Integer.parseInt(timeArgs[1].charAt(0)+"");
			s2 = Integer.parseInt(timeArgs[1].charAt(1)+"");
			if(s1 != 0){
				s += s1*10;
			}  
			if(s2 != 0){
				s += s2;
			}
			break;
		case 1:
			System.out.println("length == 1");
			s1 = Integer.parseInt(timeArgs[0].charAt(0)+"");
			s2 = Integer.parseInt(timeArgs[0].charAt(1)+"");
			if(s1 != 0){
				s += s1*10;
			}  
			if(s2 != 0){
				s += s2;
			}
			break;
		}
		
		return s;
	}
	
	public static final String MSG_VIRTUAL_KEY = "cn.yunzhisheng.intent.virtualKey";
	public static final String KEY_CODE = "KEY_CODE";
	public static final String MSG_VIRTUAL_KEY_SERVER_ACTIVE = "MSG_VIRTUAL_KEY_SERVER_ACTIVE";
	private void sendVirtualKeyMessage(int keyCode) {
		LogUtil.d(TAG, "sendVirtualKeyMessage   " + keyCode);
		Intent intent = new Intent();
		intent.setAction(MSG_VIRTUAL_KEY);
		intent.putExtra(KEY_CODE, keyCode);
		mContext.sendBroadcast(intent);
	}
}
