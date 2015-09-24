package com.example.adbar;

import java.util.ArrayList;

import com.hrtvbic.adpop.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TimeListAdapter extends BaseAdapter {
	
	private ArrayList<String> locationData;

	private Context context = null;
	// 布局加载对象
	private LayoutInflater inflater = null;

	public TimeListAdapter(Context context,
			ArrayList<String> locationData) {
		super();
		this.context = context;
		this.locationData = locationData;
		inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		// 返回适配器中数据的数量
		return locationData.size();
	}

	public Object getItem(int position) {
		// 用不到
		return null;
	}

	public long getItemId(int position) {
		// 用不到
		return 0;
	}

	// 此方法的convertView是在grid_item里定义的组件。这里是一个ImageView和TextView
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.time_list_item, null);
		}
		TextView text = (TextView) convertView
				.findViewById(R.id.family_user_location_textview);
		text.setText(locationData.get(position));
		return convertView;// 返回已经改变过的convertView，节省系统资源
	}

}
