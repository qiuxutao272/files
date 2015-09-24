package com.example;


import com.example.adbar.ADSerivce;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver{

    static final String action_boot="android.intent.action.BOOT_COMPLETED";  
    String isbootStart;
    @Override
    public void onReceive(Context context, Intent intent) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("test", 
		Activity.MODE_PRIVATE); 
		isbootStart= sharedPreferences.getString("isbootStart", ""); 
        if (intent.getAction().equals(action_boot)){  
        	
        	if("1".equals(isbootStart)){
        		Intent ootStartIntent=new Intent(context,ADSerivce.class);  
                ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
                context.startService(ootStartIntent);
        	}
              
        } 
  
    } 

}
