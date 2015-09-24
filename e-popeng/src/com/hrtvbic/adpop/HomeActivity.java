package com.hrtvbic.adpop;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.adbar.ADSerivce;
import com.example.adbar.TimeListAdapter;
import com.hrtvbic.adpop.R;

public class HomeActivity extends Activity implements OnClickListener {

	private MyDockPanelEng panel;
	FrameLayout frameLayout;
	private int ON_OFF = 1;
	// popupwindow弹出窗口
	private PopupWindow popupWindow;;
	// 显示城市和省份信息的Listview
	private ListView timeListView;
	// popupwindow要加载的布局
	private View view;
	Button bt1;
	// Button bt2;
	Button kaiguan;
	EditText editText;
	Integer time = 60000;
	ArrayList<String> list;
	// 默认开机不启动
	String isbootStart = "0";
	// 广告弹出模式
	String style;

	Button spinner;
	CheckBox checkBox;
	RadioGroup setradioGroup;
	RadioButton settime;
	RadioButton setapp;
	SharedPreferences sps;
	Editor editors;
	private String time_sString = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);// 取消标题栏
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏
		getWindow().setFormat(PixelFormat.RGBA_8888);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		setContentView(R.layout.activity_home);
		sps = HomeActivity.this.getSharedPreferences("guanggao", MODE_PRIVATE);

		bt1 = (Button) findViewById(R.id.btnOK1);
		// bt2 = (Button) findViewById(R.id.btnCancel1);
		panel = new MyDockPanelEng(this);
		initView();

		bt1.setOnClickListener(this);
		// bt2.setOnClickListener(this);
	}

	private void initWindow() {
		if (popupWindow != null) {
			popupWindow = null;
		}
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		view = layoutInflater.inflate(R.layout.time_list, null);

		timeListView = (ListView) view.findViewById(R.id.time_list_view);

	}

	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(30);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	private void showCityWindow(View parent) {
		// 初始化popupwindow窗口
		initWindow();
		// 加载数据
		TimeListAdapter timeListAdapter = new TimeListAdapter(
				getApplicationContext(), list);
		// 显示时间
		timeListView.setAdapter(timeListAdapter);
		// 设置listview高度
		LayoutParams layoutPsrams = timeListView.getLayoutParams();
		layoutPsrams.height = 180;
		timeListView.setLayoutParams(layoutPsrams);

		// 创建一个PopuWidow对象
		popupWindow = new PopupWindow(view, dip2px(HomeActivity.this, 190), 200);

		popupWindow.setOnDismissListener(new OnDismissListener() {

			public void onDismiss() {
				// TODO Auto-generated method stub
				spinner.setBackgroundResource(R.drawable.time_btn_selector);
			}
		});

		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);

		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable());

		popupWindow.showAsDropDown(parent, 0, 0);
		// 下拉菜单点击事件
		timeListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				time = (position + 1) * 60000;
				// time = (position + 1) * 60000;
				time_sString = list.get(position);
				spinner.setText(time_sString);

				// 关闭下拉菜单
				if (popupWindow != null) {
					spinner.setBackgroundResource(R.drawable.time_btn_selector);
					popupWindow.dismiss();
				}
			}

		});

	}

	private void info() {
		// 获取SharedPreferences对象
		if (sps.getInt("SHUJU", 0) == 0) {
			// 存入数据
			editors = sps.edit();
			editors.putInt("START", 0);
			editors.putInt("SHUJU", 1);
			editors.putInt("KAIGUAN", 1);
			editors.putBoolean("CHECKBOX", true);
			editors.putString("TIME", "5mins");
			editors.putInt("TIME_INT", 5 * 60000);
			editors.putBoolean("SETTIME", true);
			editors.putBoolean("SETAPP", false);
			editors.commit();
		}

	}

	public void initView() {
		info();
		checkBox = (CheckBox) findViewById(R.id.setCheckbox);

		setradioGroup = (RadioGroup) findViewById(R.id.setradioGroup);

		settime = (RadioButton) findViewById(R.id.settime);

		setapp = (RadioButton) findViewById(R.id.setapp);

		frameLayout = (FrameLayout) findViewById(R.id.meng);
		/* 下拉组件测试 Spinner 对象首先获取 */
		spinner = (Button) findViewById(R.id.setSpinner);
		// 开关按钮
		kaiguan = (Button) findViewById(R.id.kaiguan);

		setradioGroup.clearCheck();
		checkBox.setChecked(sps.getBoolean("CHECKBOX", true));
		settime.setChecked(sps.getBoolean("SETTIME", true));
		setapp.setChecked(sps.getBoolean("SETAPP", false));
		time = sps.getInt("TIME_INT", 5 * 60000);
		time_sString = sps.getString("TIME", "5mins");
		spinner.setText(time_sString);
		ON_OFF = sps.getInt("KAIGUAN", 1);
		if (ON_OFF == 1) {
			bt1.setFocusable(true);
			frameLayout.setVisibility(View.GONE);
			spinner.setFocusable(true);
			checkBox.setFocusable(true);
			setradioGroup.setFocusable(true);
			settime.setFocusable(true);
			setapp.setFocusable(true);
			bt1.requestFocus();
			kaiguan.setBackgroundResource(R.drawable.on_btn_selector);
		} else if (ON_OFF == 2) {
			bt1.setFocusable(false);
			kaiguan.setBackgroundResource(R.drawable.off_btn_selector);
			spinner.setFocusable(false);
			checkBox.setFocusable(false);
			setradioGroup.setFocusable(false);
			settime.setFocusable(false);
			setapp.setFocusable(false);
			frameLayout.setVisibility(View.VISIBLE);
		}
		kaiguan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, ADSerivce.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// TODO Auto-generated method stub
				if (ON_OFF == 1) {
					ON_OFF = 2;
					editors = sps.edit();
					editors.putInt("KAIGUAN", ON_OFF);
					editors.putBoolean("CHECKBOX", checkBox.isChecked());
					editors.putString("TIME", time_sString);
					editors.putInt("TIME_INT", time);
					editors.putBoolean("SETTIME", settime.isChecked());
					editors.putBoolean("SETAPP", setapp.isChecked());
					editors.commit();
					kaiguan.setBackgroundResource(R.drawable.off_btn_selector);
					bt1.setFocusable(false);
					spinner.setFocusable(false);
					checkBox.setFocusable(false);
					setradioGroup.setFocusable(false);
					settime.setFocusable(false);
					setapp.setFocusable(false);
					frameLayout.setVisibility(View.VISIBLE);

				} else if (ON_OFF == 2) {
					bt1.setFocusable(true);
					frameLayout.setVisibility(View.GONE);
					spinner.setFocusable(true);
					checkBox.setFocusable(true);
					setradioGroup.setFocusable(true);
					settime.setFocusable(true);
					setapp.setFocusable(true);
					kaiguan.setBackgroundResource(R.drawable.on_btn_selector);
					ON_OFF = 1;
					// 实例化SharedPreferences对象（第一步）
					SharedPreferences mySharedPreferences = getSharedPreferences(
							"test", Activity.MODE_PRIVATE);

					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					if (settime.isChecked()) {
						style = "0";
					} else if (setapp.isChecked()) {
						style = "1";
					}
					editor.putString("time", time.toString());
					editor.putString("style", style);
					editor.putString("isbootStart", isbootStart);
					// 提交当前数据
					editor.commit();
					editors = sps.edit();
					editors.putInt("KAIGUAN", ON_OFF);
					editors.putBoolean("CHECKBOX", checkBox.isChecked());
					editors.putString("TIME", time_sString);
					editors.putInt("TIME_INT", time);
					editors.putBoolean("SETTIME", settime.isChecked());
					editors.putBoolean("SETAPP", setapp.isChecked());
					editors.commit();
					// if (!isServiceRunning(getApplicationContext(),
					// "ADSerivce")) {
					// startService(intent);
					// } else {
					// Log.i("ZCL", "服务已经存在了。");
					// }
				}

			}
		});
		spinner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showCityWindow(v);
				spinner.setBackgroundResource(R.drawable.time_zhankai);
			}
		});

		/* 准备数据源 M , 用集合进行保存 */
		list = new ArrayList<String>();
		for (int i = 1; i < 21; i++) {
			if(i == 1)
				list.add(1 + "min");
			else
				list.add(i + "mins");
		}
	}

	@Override
	public void onClick(View v) {

		// 获取开机启动
		if (checkBox.isChecked()) {
			isbootStart = "1";

		} else {
			isbootStart = "0";
		}
		if (settime.isChecked()) {
			style = "0";
		} else if (setapp.isChecked()) {
			style = "1";
		}
		kill(this);
		Intent intent = new Intent(HomeActivity.this, ADSerivce.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// 实例化SharedPreferences对象（第一步）
		SharedPreferences mySharedPreferences = getSharedPreferences("test",
				Activity.MODE_PRIVATE);

		SharedPreferences.Editor editor = mySharedPreferences.edit();
		// 用putString的方法保存数据
		switch (v.getId()) {
		case R.id.btnOK1:
			/*
			 * b.putString("time", time.toString()); b.putString("style", "0");
			 * intent.putExtras(b);
			 */

			editor.putString("time", time.toString());
			editor.putString("style", style);
			editor.putString("isbootStart", isbootStart);
			// 提交当前数据
			editor.commit();

			// ActivityManager mActivityManager = (ActivityManager)
			// getSystemService(ACTIVITY_SERVICE);
			// List<ActivityManager.RunningServiceInfo> mServiceList =
			// mActivityManager
			// .getRunningServices(30);
			// final String adSerivceClassName = "com.example.adbar.ADSerivce";
			// boolean b = ADSerivceIsStart(mServiceList, adSerivceClassName);
			// if (b) {
			// stopService(intent);
			// }

			editors = sps.edit();
			editors.putInt("START", 1);
			editors.putInt("KAIGUAN", ON_OFF);
			editors.putBoolean("CHECKBOX", checkBox.isChecked());
			editors.putString("TIME", time_sString);
			editors.putInt("TIME_INT", time);
			editors.putBoolean("SETTIME", settime.isChecked());
			editors.putBoolean("SETAPP", setapp.isChecked());
			editors.commit();
			// intent.setFlags(101);
			if (!isServiceRunning(getApplicationContext(), "ADSerivce")) {
				startService(intent);
			} else {
				Log.i("ZCL", "服务已经存在了。");
			}

			finish();

			break;
		case R.id.btnCancel1:
			checkBox.setChecked(false);
			settime.setChecked(true);
			// 提交当前数据
			editor.commit();
			editors = sps.edit();
			editors.putInt("SHUJU", 1);
			editors.putInt("KAIGUAN", 1);
			editors.putBoolean("CHECKBOX", false);
			editors.putString("TIME", "1min");
			editors.putInt("TIME_INT", 60000);
			editors.putBoolean("SETTIME", true);
			editors.putBoolean("SETAPP", false);
			editors.commit();
			checkBox.setChecked(sps.getBoolean("CHECKBOX", false));
			settime.setChecked(sps.getBoolean("SETTIME", false));
			settime.setChecked(sps.getBoolean("SETAPP", false));
			time = sps.getInt("TIME_INT", 60000);
			time_sString = sps.getString("TIME", "1min");
			spinner.setText(sps.getString("TIME", "1min"));
			ON_OFF = sps.getInt("KAIGUAN", 1);

			frameLayout.setVisibility(View.GONE);
			spinner.setFocusable(true);
			checkBox.setFocusable(true);
			setradioGroup.setFocusable(true);
			settime.setFocusable(true);
			setapp.setFocusable(true);
			kaiguan.setBackgroundResource(R.drawable.on_btn_selector);

			Toast.makeText(this, "默认广告设置！", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	private boolean ADSerivceIsStart(
			List<ActivityManager.RunningServiceInfo> mServiceList,
			String className) {

		for (int i = 0; i < mServiceList.size(); i++) {
			if (className.equals(mServiceList.get(i).service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	public static void kill(Context context) {
		// 获取一个ActivityManager 对象
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		// 获取系统中所有正在运行的进程

		List<RunningAppProcessInfo> appProcessInfos = activityManager
				.getRunningAppProcesses();

		// 获取当前activity所在的进程
		String currentProcess = context.getApplicationInfo().processName;
		Log.d("mogu", currentProcess);
		for (RunningAppProcessInfo appProcessInfo : appProcessInfos) {
			String processName = appProcessInfo.processName;
			if (processName.equals(currentProcess)) {
				System.out.println("ApplicationInfo-->" + processName);
				activityManager.killBackgroundProcesses(processName);
			}
		}

	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			SharedPreferences mySharedPreferences = getSharedPreferences(
					"test", Activity.MODE_PRIVATE);
			if (settime.isChecked()) {
				style = "0";
			} else if (setapp.isChecked()) {
				style = "1";
			}
			SharedPreferences.Editor editor = mySharedPreferences.edit();
			editor.putString("time", time.toString());
			editor.putString("style", style);
			editor.putString("isbootStart", isbootStart);
			// 提交当前数据
			editor.commit();
			Intent intent = new Intent(HomeActivity.this, ADSerivce.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			editors = sps.edit();
			editors.putInt("START", 0);
			editors.putInt("KAIGUAN", ON_OFF);
			editors.putBoolean("CHECKBOX", checkBox.isChecked());
			editors.putString("TIME", time_sString);
			editors.putInt("TIME_INT", time);
			editors.putBoolean("SETTIME", settime.isChecked());
			editors.putBoolean("SETAPP", setapp.isChecked());
			editors.commit();

			startService(intent);

			finish();
		}
		return false;
	}
}
