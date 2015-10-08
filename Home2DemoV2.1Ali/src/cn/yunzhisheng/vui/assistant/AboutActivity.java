package cn.yunzhisheng.vui.assistant;

import cn.yunzhisheng.vui.assistant.oem.RomDevice;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(Res.layout.activity_about);
		
		TextView textViewVersion = (TextView) findViewById(Res.id.tv_version);
		textViewVersion.setText(String.format(textViewVersion.getText().toString(), RomDevice.getAppVersionName(this)));
	}
}
