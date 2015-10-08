{
"main":[
{"name":"contact", "data":["([<head>] 给 [\"<unk>\"] (<Contact>) [\"<unk>\"] (<CallPost>))","([<head>] (<CallPre>) [\"<unk>\"] (<Contact>) [的][<PhoneAddr>][的][<PhoneType>])","([<head>] 给 [\"<unk>\"] (<Contact>) [\"<unk>\"] (<Send> [一 个|一 条|个|条] <Message>))","([<head>] (<Send> [一 个|一 条|条|个] <Message> [给|到]) [\"<unk>\"] (<Contact>) [的][<PhoneAddr>][的][<PhoneType>])", "<Contact>"]},
{"name":"app", "data": ["( [<head>] <Launch> [\"<unk>\"] <Apps> )","( [<head>] <Uninstall> [\"<unk>\"] <Apps> )", "( [<head>] <KillApp> [\"<unk>\"] <Apps> )", "<Apps>"]},
{"name":"video", "data": ["( [<head>] <SearchVideo> [一 下|下] [\"<unk>\"] <Video> )","<Video>"]},
{"name":"channel", "data": ["( [<head>] <ChangeChannel> [\"<unk>\"] <Channel> )","<Channel>"]},
{"name":"music", "data":["([<head>] [听|播 放] [一 首|首|一 个|个] [\"<unk>\"] <Singer> (的) [\"<unk>\"] (<SongTag>|<Song>))","([<head>] [听|播 放] [一 首|首|一 个|个] [\"<unk>\"] <Song>)","<Song>"]},
{"name":"poi", "define": "true", "data": ["<Poi>"]},
{"name":"city", "data":["[<head>] (切 换 到|调 整 到|改 到) <City>", "<City>"]},
{"name":"setting", "priority":"1", "data": ["<Setting>"]},
{"name":"command", "priority":"2", "data":["<Command>"]},
{"name":"custom", "priority":"3", "data":["<Custom>"]},
{"name":"scenecallback_key","priority":"2","data":["[<head>] <FullVoiceCallBack_KEY>"]},
{"name":"scene", "priority":"4", "data":["<Scene>"]}
],

"tag":[
{"name":"head","data":["我想","我要","我想要","帮我","替我","给我","帮","帮忙","你给我","你替我"]},
{"name":"PhoneAddr","data":["家","家庭","家里","公司","公司里","办公","办公室","单位","单位里","固定"]},
{"name":"PhoneType","data":["小灵通","工作","工作电话","手提","手提电话","手机","手机号","座机","座机号","移动","移动号","移动号码","移动电话","电信","电信号","电信号码","电信电话","电话","号码","电话号码","联通","联通号","联通号码","联通电话"]},
{"name":"Send","data":["去","写","发","回","发送","编","编写","编辑"]},
{"name":"SongTag", "data":["歌","歌曲","音乐","曲","小曲","曲子"]}
],

"action":[
{"name":"Message","data":["信息","消息","文字讯息","简讯","短信","短信息","短消息"]},
{"name":"CallPre","data":["打电话给","打个电话给","去个电话给","呼叫","拨打","打给"]},
{"name":"CallPost","data":["打电话","打个电话","打一个电话","去电话","去个电话","去一个电话","去电","拨电话","拨个电话","拨一个电话"]},
{"name":"Launch","data":["加载","启动","打开","运行","玩"]},
{"name":"Uninstall","data":["卸载","删除","移除","去除"]},
{"name":"KillApp","data":["关闭","停止","退出","停掉","关掉"]},
{"name":"SearchVideo","data":["搜","看","搜索","查找","查询","检索","打开"]},
{"name":"ChangeChannel","data":["打开","切换到","跳转到","转到","切到","调到","看","看一下","看下"]}
],

"param":[
{"name":"poi","protocal":"{\"service\":\"DOMAIN_POI\",\"message\":\"{0}\",\"poi\":\"{1}\"}","action":[],"params":["MESSAGE","Poi"]},
{"name":"city","protocal":"{\"service\":\"DOMAIN_CHANGE_CITY\",\"message\":\"{0}\",\"city\":\"{1}\"}","action":[],"params":["MESSAGE","City"]},

{"name":"music_by_song_artist","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.music\",\"code\":\"SEARCH_SONG\",\"semantic\":{\"intent\":{\"artist\":\"{1}\",\"song\":\"{2}\",\"keyword\":\"{1} {2}\"}},\"history\":\"cn.yunzhisheng.music\"}","action":[],"params":["MESSAGE","Singer","Song"]},
{"name":"music_by_song","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.music\",\"code\":\"SEARCH_SONG\",\"semantic\":{\"intent\":{\"song\":\"{1}\",\"keyword\":\"{1}\"}},\"history\":\"cn.yunzhisheng.music\"}","action":[],"params":["MESSAGE","Song"]},

{"name":"call_contact","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.call\",\"code\":\"CALL\",\"semantic\":{\"intent\":{\"name\":\"{1}\"}},\"history\":\"cn.yunzhisheng.call\"}","action":["CallPre","CallPost"],"params":["MESSAGE","Contact"]},
{"name":"contact","protocal":"{\"rc\":0,\"text\":\"{1}\",\"service\":\"cn.yunzhisheng.contact\",\"code\":\"CONTACT_SEARCH\",\"semantic\":{\"intent\":{\"name\":\"{1}\"}},\"history\":\"cn.yunzhisheng.contact\"}","action":[],"params":["MESSAGE","Contact"]},

{"name":"sms","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.sms\",\"code\":\"SMS_SEND\",\"semantic\":{\"intent\":{\"name\":\"{1}\"}},\"history\":\"cn.yunzhisheng.sms\"}","action":["Message"],"params":["MESSAGE","Contact"]},

{"name":"launch_app","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.appmgr\",\"code\":\"APP_LAUNCH\",\"semantic\":{\"intent\":{\"name\":\"{1}\"}},\"history\":\"cn.yunzhisheng.appmgr\"}","action":["Launch"],"params":["MESSAGE","Apps"]},
{"name":"app","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.appmgr\",\"code\":\"APP_LAUNCH\",\"semantic\":{\"intent\":{\"name\":\"{1}\"}},\"history\":\"cn.yunzhisheng.appmgr\"}","action":[],"params":["MESSAGE","Apps"]},

{"name":"uninstall_app","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.appmgr\",\"code\":\"APP_UNINSTALL\",\"semantic\":{\"intent\":{\"name\":\"{1}\"}},\"history\":\"cn.yunzhisheng.appmgr\"}","action":["Uninstall"],"params":["MESSAGE","Apps"]},
{"name":"kill_app","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.appmgr\",\"code\":\"APP_EXIT\",\"semantic\":{\"intent\":{\"name\":\"{1}\"}},\"history\":\"cn.yunzhisheng.appmgr\"}","action":["KillApp"],"params":["MESSAGE","Apps"]},

{"name":"search_video","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.video\",\"code\":\"SEARCH\",\"semantic\":{\"intent\":{\"keyword\":\"{0}\",\"url\":\"{1}\"}},\"general\":{\"type\":\"U\",\"url\":\"http://www.soku.com/m/y/video?q=%E7%94%84%E5%AC%9B%E4%BC%A0\",\"urlAlt\":\"视频链接\"},\"history\":\"cn.yunzhisheng.video\"}","action":["SearchVideo"],"params":["MESSAGE","Video"]},
{"name":"video","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.video\",\"code\":\"SEARCH\",\"semantic\":{\"intent\":{\"keyword\":\"{0}\",\"url\":\"{1}\"}},\"general\":{\"type\":\"U\",\"url\":\"http://www.soku.com/m/y/video?q=%E7%94%84%E5%AC%9B%E4%BC%A0\",\"urlAlt\":\"视频链接\"},\"history\":\"cn.yunzhisheng.video\"}","action":[],"params":["MESSAGE","Video"]},

{"name":"change_channel","protocal":"[{\"rc\":\"0\",\"id\":\"test_id\",\"message\":\"{0}\",\"service\":\"cn.yunzhisheng.setting.tv\",\"code\":\"SETTING_EXEC_TV\",\"hasAnswer\":false,\"semantic\":{\"intent\":{\"operator\":\"ACT_OPEN_CHANNEL\",\"operands\":\"{1}\"}},\"unsupportedText\":\"很抱歉，暂不支持设置功能.\",\"history\":\"SETTING\"}]","action":["ChangeChannel"],"params":["MESSAGE","Channel"]},
{"name":"channel","protocal":"[{\"rc\":\"0\",\"id\":\"test_id\",\"message\":\"{0}\",\"service\":\"cn.yunzhisheng.setting.tv\",\"code\":\"SETTING_EXEC_TV\",\"hasAnswer\":false,\"semantic\":{\"intent\":{\"operator\":\"ACT_OPEN_CHANNEL\",\"operands\":\"{1}\"}},\"unsupportedText\":\"很抱歉，暂不支持设置功能.\",\"history\":\"SETTING\"}]","action":[],"params":["MESSAGE","Channel"]},


{"name":"open_3g","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_OPEN\",\"operands\":\"OBJ_3G\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["打开移动网络"],["打开3G"],["打开GPRS"],["打开数据"],["打开移动数据"]]},
{"name":"close_3g","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_CLOSE\",\"operands\":\"OBJ_3G\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["关闭移动网络"],["关闭3G"],["关闭GPRS"],["关闭数据"],["关闭移动数据"]]},
{"name":"open_bluetooth","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_OPEN\",\"operands\":\"OBJ_BLUETOOTH\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["打开蓝牙"],["开启蓝牙"],["启动蓝牙"]]},
{"name":"close_bluetooth","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_CLOSE\",\"operands\":\"OBJ_BLUETOOTH\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["关闭蓝牙"],["关掉蓝牙"],["停止蓝牙"]]},
{"name":"set_time","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_SET\",\"operands\":\"OBJ_TIME\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["设置时间"]]},
{"name":"open_mode_inair","protocal":"[{\"rc\":\"0\",\"id\":\"test_id\",\"message\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"hasAnswer\":false,\"semantic\":{\"intent\":{\"operator\":\"ACT_OPEN\",\"operands\":\"OBJ_MODEL_INAIR\"}},\"unsupportedText\":\"很抱歉，暂不支持设置功能.\",\"history\":\"SETTING\"}]","action":[],"params":["MESSAGE","Setting"],"Setting":[["打开飞行模式"],["开启飞行模式"],["启动飞行模式"]]},
{"name":"close_mode_inair","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_CLOSE\",\"operands\":\"OBJ_MODEL_INAIR\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["关闭飞行模式"],["退出飞行模式"]]},
{"name":"open_mute","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_OPEN\",\"operands\":\"OBJ_MODEL_MUTE\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["静音"],["静音模式"],["打开静音"],["开启静音"],["启动静音"],["打开静音模式"],["开启静音模式"],["启动静音模式"]]},
{"name":"close_mute","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_CLOSE\",\"operands\":\"OBJ_MODEL_MUTE\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["关闭静音"],["取消静音"],["退出静音"],["关闭静音模式"],["取消静音模式"],["退出静音模式"]]},
{"name":"open_mode_vibra","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_OPEN\",\"operands\":\"OBJ_MODEL_VIBRA\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["震动"],["震动模式"],["打开震动"],["开启震动"],["启动震动"],["打开震动模式"],["开启震动模式"],["启动震动模式"]]},
{"name":"close_mode_vibra","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_CLOSE\",\"operands\":\"OBJ_MODEL_VIBRA\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["关闭震动"],["取消震动"],["退出震动"],["关闭震动模式"],["取消震动模式"],["退出震动模式"]]},
{"name":"set_ringtone","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_SET\",\"operands\":\"OBJ_RINGTONE\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["铃声设置"]]},
{"name":"open_rotation","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_OPEN\",\"operands\":\"OBJ_ROTATION\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["打开自动旋转"],["开启自动旋转"],["启动自动旋转"]]},
{"name":"close_rotation","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_CLOSE\",\"operands\":\"OBJ_ROTATION\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["关闭自动旋转"],["取消自动旋转"],["退出自动旋转"]]},
{"name":"set_wallpaper","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_SET\",\"operands\":\"OBJ_WALLPAPER\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["设置壁纸"]]},
{"name":"open_wifi","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_OPEN\",\"operands\":\"OBJ_WIFI\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["打开wifi"],["开启wifi"],["启动wifi"]]},

{"name":"close_wifi","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_CLOSE\",\"operands\":\"OBJ_WIFI\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["关闭wifi"],["退出wifi"],["取消wifi"]]},
{"name":"open_wifi_spot","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_OPEN\",\"operands\":\"OBJ_WIFI_SPOT\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["打开wifi热点"],["开启wifi热点"],["启动wifi热点"]]},
{"name":"close_wifi_spot","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_CLOSE\",\"operands\":\"OBJ_WIFI_SPOT\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["关闭wifi热点"],["退出wifi热点"],["取消wifi热点"]]},

{"name":"increase_light","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_INCREASE\",\"operands\":\"OBJ_LIGHT\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["增大亮度"],["调高亮度"],["亮一点"],["再亮一点"]]},
{"name":"decrease_light","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_DECREASE\",\"operands\":\"OBJ_LIGHT\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["减小亮度"],["调低亮度"],["暗一点"],["再暗一点"]]},



{"name":"key_up","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_ARROW_UP\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["向上"],["向上滑动"],["上"],["往上"],["往上滑动"]]},
{"name":"key_up","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_ARROW_DOWN\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["向下"],["向下滑动"],["下"],["往下"],["往下滑动"]]},
{"name":"key_up","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_ARROW_LEFT\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["向左"],["向左滑动"],["左"],["往左"],["往左滑动"]]},
{"name":"key_up","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_ARROW_RIGHT\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["向右"],["向右滑动"],["右"],["往右"],["往右滑动"]]},
{"name":"key_up","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"confirm\":\"OK\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["确定"],["确认"],["执行"],["打开"]]},
{"name":"key_up","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_OPEN\",\"operands\":\"OBJ_MENU\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["菜单"],["菜单键"]]},
{"name":"key_up","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_OPEN\",\"operands\":\"OBJ_HOME\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["主界面"],["桌面"],["主页"]]},
{"name":"key_up","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_BACK\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["返回"],["退出"],["关闭"]]},
{"name":"key_up","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_PAGE_UP\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["上一页"],["上页"],["往上一页"]]},
{"name":"key_up","protocal":"{\"rc\":0,\"text\":\"{0}\",\"service\":\"cn.yunzhisheng.setting\",\"code\":\"SETTING_EXEC\",\"semantic\":{\"intent\":{\"operator\":\"ACT_PAGE_DOWN\"}},\"history\":\"cn.yunzhisheng.setting\"}","action":[],"params":["MESSAGE","Setting"],"Setting":[["下一页"],["下页"],["往下一页"]]}
],


"custom":[
{"name":"Contact", "data":[["人工台","人工客服","客服"]]},
{"name":"Apps", "data":[["Letv Store","乐tv 死到","Letv 死到","乐易梯威死到","爱要易梯威死到","乐视应用商城","应用商城"],["乐视网tv版","乐视网梯威版"],["登录"]]},
{"name":"Channel", "data":[["乐视直播台","乐视直播"],["CCTV-1","中央一台","中央一套","中央一"]]},
{"name":"Video", "data":[["萧十一郎","萧11郎"],["新十二生肖","新12生肖"]]},
{"name":"City", "data":[["上海市"], ["北京市"]], "file":"%cache%/City.txt"},
{"name":"Singer", "data":[["刘德华"]]},
{"name":"Song", "data":[["忘情水"]]}
]
}
