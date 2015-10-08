/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName	: ChannelList.java
 * @ProjectName	: vui_voicetv
 * @PakageName	: cn.yunzhisheng.voicetv.util
 * @Author		: Conquer
 * @CreateDate	: 2013-12-19
 */
package cn.yunzhisheng.vui.assistant.util;

import java.util.HashMap;

import cn.yunzhisheng.common.util.LogUtil;

/**
 * @Module		: 隶属模块名
 * @Comments	: 描述
 * @Author		: Conquer
 * @CreateDate	: 2013-12-19
 * @ModifiedBy	: Conquer
 * @ModifiedDate: 2013-12-19
 * @Modified: 
 * 2013-12-19: 实现基本功能
 */
public class ChannelList {
	public static final String TAG = "ChannelList";
	
//	public   HashMap<String,String> channle_list = new HashMap<String, String>();
	
//	public  HashMap<String,String> getChannelList(){
////		if(channle_list.size() == 0){
////			initChannelList();
////		}
//		return channle_list;
//	}
	
//	private static void initChannelList(){
//		channle_list.clear();
//		channle_list.put("湖南卫视", "http://tvie01.ucatv.com.cn/channels/xjyx/HuNan-HD-Suma/flv:HUN_HD_Suma/live?1387528704026");
//		channle_list.put("江苏卫视", "http://live-cdn.kksmg.com/channels/tvie/test3/flv:sd"); 
//		channle_list.put("东方卫视", "http://qqlive.dnion.com:1863/3900155972.flv?apptype=live&pla=WIN&time=1387529718&cdn=dilian&vkey=983342706B595F59BF59C83711044FF012AEE9D73DCF2AB0C8AF5C5BE939FFB165A55B239C15BBEC");
//		channle_list.put("浙江卫视", "http://live-cdn.kksmg.com/channels/tvie/test2/flv:sd"); 
//		channle_list.put("CCTV1", "http://hdstime.cntv.chinacache.net:8000/live/flv/channel59");  
//		channle_list.put("CCTV-1", "http://hdstime.cntv.chinacache.net:8000/live/flv/channel59"); 
//		channle_list.put("凤凰卫视", "http://cmp.zqredstar.net/tv/iftv.asp?uuid=5B63686E5D445830303030303034327C3130303031327C7C7C7C72746D707C666C765B2F63686E5D5B74735D315B2F74735D5B7375735D315B2F7375735DVSDNSOOONERCOM00");
//	}
	public  HashMap<String,String> initChannelList(){
		 HashMap<String,String> channle_list = new HashMap<String, String>();
		 //1387528704026      1387529718
		 String time = System.currentTimeMillis()+"";
//		System.out.println(time+"====");
		channle_list.put("江苏卫视", "http://live-cdn.kksmg.com/channels/tvie/test3/flv:sd"); 
		channle_list.put("东方卫视", "http://qqlive.dnion.com:1863/3900155972.flv?apptype=live&pla=WIN&time="+time.substring(0, 9)+"&cdn=dilian&vkey=983342706B595F59BF59C83711044FF012AEE9D73DCF2AB0C8AF5C5BE939FFB165A55B239C15BBEC");
		channle_list.put("浙江卫视", "http://live-cdn.kksmg.com/channels/tvie/test2/flv:sd"); 
		channle_list.put("湖南卫视", "http://live-cdn.kksmg.com/channels/tvie/test/flv:500k");  
		channle_list.put("凤凰卫视", "http://cmp.zqredstar.net/tv/iftv.asp?uuid=5B63686E5D445830303030303034327C3130303031327C7C7C7C72746D707C666C765B2F63686E5D5B74735D315B2F74735D5B7375735D315B2F7375735DVSDNSOOONERCOM00");
		channle_list.put("安徽卫视", "http://qqlive.dnion.com:1863/623043810.flv?apptype=unknow&pla=unknow&time="+time.substring(0, 9)+"&cdn=zijian&vkey=3430ACE0ADA0CCD968E275E0B22920CA4563AD7C86A847B456351D29D8F158B988AEC599EF668FA9");
		channle_list.put("星空卫视","http://resource.ws.kukuplay.com/players/2013/12/19/47882/fengyun.swf?cid=17803_1348061485429");
		channle_list.put("CCTV-3", "http://cc1.cdn.64ma.com:8000/live/flv/channel7");
		return channle_list;
	}
}
