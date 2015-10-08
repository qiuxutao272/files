package com.example.demo;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MessageReceiver extends BroadcastReceiver {	
	private static String TAG = "MessageReceiver";
	@Override
	public void onReceive(Context mcontext, Intent arg1) {
		// TODO Auto-generated method stub
		Log.v("MessageReceiver", "=========MessageReceiver========="+arg1.getAction());
		if ("cn.open.music.yingyuan".equals(arg1.getAction())) {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setClass(mcontext, YuYinSiRenFilm1.class);	
			intent.putExtra("picid", 0);
			mcontext.startActivity(intent);
		}
		else if ("cn.open.music.jiaoyu".equals(arg1.getAction())) {
//			Intent intent = new Intent();
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			intent.setClass(mcontext, YuYinHighQingJiaoYu1.class);	
//			mcontext.startActivity(intent);
			
			 Intent intent = new Intent();
			 intent.setComponent(new ComponentName("com.cpsoft.youjiao.ma01","com.unity3d.player.UnityPlayerNativeActivity"));
			 intent.setAction(Intent.ACTION_VIEW);
			 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 mcontext.startActivity(intent);
		}
		else if ("cn.open.music.changge".equals(arg1.getAction())) {
			Log.v("bill", "changge************************************");
			ComponentName comptianlai = new ComponentName("com.audiocn.kalaok.tv.haier.amlogict866","com.audiocn.karaoke.tv.main.MainActivity");
			Intent intenttianlai = new Intent();
			intenttianlai.setComponent(comptianlai);
			intenttianlai.setAction("android.intent.action.VIEW");
			intenttianlai.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mcontext.startActivity(intenttianlai);
		}
		else if("cn.open.music.paomo".equals(arg1.getAction())){
			Intent intent_music1 = new Intent();
			intent_music1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent_music1.setClass(mcontext, ShowMusicActivity.class);
			intent_music1.putExtra("online", 30);
			mcontext.startActivity(intent_music1);
		}
		else if("cn.open.yingyuan".equals(arg1.getAction())){
			Intent intent_languang = new Intent();
			intent_languang.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent_languang.setClass(mcontext, YuYinYingYuan.class);
			mcontext.startActivity(intent_languang);
		}
		else if("cn.open.jiliukuaiting".equals(arg1.getAction())){
			ComponentName comptianlai = new ComponentName("com.vectorunit.redcmgeplaycn","com.vectorunit.redcmgeplaycn.Red");
			Intent intentjiliu = new Intent();
			intentjiliu.setComponent(comptianlai);
			intentjiliu.setAction("android.intent.action.VIEW");
			intentjiliu.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mcontext.startActivity(intentjiliu);
		}
		else if("cn.open.picture.shop4".equals(arg1.getAction())){
			Intent intent_shop4 = new Intent();
			intent_shop4.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent_shop4.setClass(mcontext, Bailunxie.class);
			intent_shop4.putExtra("online", 8);
			mcontext.startActivity(intent_shop4);
		}
		else if("cn.open.picture.shop5".equals(arg1.getAction())){
			Intent intent_shop5 = new Intent();
			intent_shop5.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent_shop5.setClass(mcontext, Adidaxie.class);
			intent_shop5.putExtra("online", 9);
			mcontext.startActivity(intent_shop5);
		}
		else if("cn.open.picture.shop6".equals(arg1.getAction())){
			Intent intent_shop6 = new Intent();
			intent_shop6.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent_shop6.setClass(mcontext, Hetao.class);
			intent_shop6.putExtra("online", 10);
			mcontext.startActivity(intent_shop6);
		}
		else if("cn.open.picture.shop7".equals(arg1.getAction())){
			Intent intent_shop7 = new Intent();
			intent_shop7.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent_shop7.setClass(mcontext, Dazao.class);
			intent_shop7.putExtra("online", 11);
			mcontext.startActivity(intent_shop7);
		}
		else if("cn.open.picture.shop8".equals(arg1.getAction())){
			Intent intent_shop8 = new Intent();
			intent_shop8.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent_shop8.setClass(mcontext, Matong.class);
			intent_shop8.putExtra("online", 12);
			mcontext.startActivity(intent_shop8);
		}
		else if("cn.open.picture.shop9".equals(arg1.getAction())){
			Intent intent_shop9 = new Intent();
			intent_shop9.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent_shop9.setClass(mcontext, Chuanglian.class);
			intent_shop9.putExtra("online", 13);
			mcontext.startActivity(intent_shop9);
		}
		else if("cn.open.picture.shop10".equals(arg1.getAction())){
			Intent intent_shop10 = new Intent();
			intent_shop10.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent_shop10.setClass(mcontext, Shandiche.class);
			intent_shop10.putExtra("online", 14);
			mcontext.startActivity(intent_shop10);
		}
		else if("cn.open.picture.shop".equals(arg1.getAction())){
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setClass(mcontext, Shop_Activity.class);
			mcontext.startActivity(intent);
		}
	}
}
