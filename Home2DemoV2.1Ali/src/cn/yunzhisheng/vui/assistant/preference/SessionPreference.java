package cn.yunzhisheng.vui.assistant.preference;

public class SessionPreference {
	/********************** session key *********************/
	// common
	public static final String KEY_DOMAIN = "domain";
	public static final String KEY_TYPE = "type";
	public static final String KEY_ORIGIN_TYPE = "origin_type";
	public static final String KEY_DATA = "data";
	
	public static final String KEY_QUESTION = "question";
	public static final String KEY_ANSWER = "answer";
	public static final String KEY_RESULT = "result";
	public static final String KEY_ON_CANCEL = "onCancel";
	public static final String KEY_URL = "url";
	public static final String KEY_KEYWORD = "keyword";
	public static final String KEY_CLASS_NAME="class_name";
	public static final String KEY_PACKAGE_NAME="package_name";
	
	public static final String KEY_CATEGORY = "category";
	public static final String KEY_NAME = "name";
	// tv
	public static final String KEY_CHANNEL = "channel";
	public static final String KEY_BYDATE = "byDate";
	public static final String KEY_DATE = "date";
	public static final String KEY_PROGRAMS = "programs";
	public static final String KEY_PID = "pid";
	public static final String KEY_TIME = "time";
	public static final String KEY_TIME_DELTA = "timeDelta";
	public static final String KEY_TITLE = "title";
	public static final String KEY_PERIOD = "period";
	public static final String KEY_BROADCASTS = "broadcasts";
	public static final String KEY_SCORE = "score";
	public static final String KEY_CONTENT = "content";
	public static final String KEY_VENDOR= "vendor";
	public static final String KEY_PIC = "pic";

	public static final String KEY_PROTOCAL = "protocal";
	public static final String KEY_UNSUPPROT_TEXT = "unsupportText";
	
	/********************** session value *********************/
	public static final String VALUE_SESSION_BENGIN = "SESSION_BEGIN";
	public static final String VALUE_SESSION_SHOW = "SESSION_SHOW";
	public static final String VALUE_SESSION_END = "SESSION_END";
	
	public static final String VALUE_TYPE_WAITING = "WAITING";
	public static final String VALUE_TYPE_WEATHER_SHOW = "WEATHER_SHOW";
	public static final String VALUE_TYPE_WEB_SHOW = "WEB_SHOW";
	public static final String VALUE_TYPE_TRANSLATION_SHOW = "TRANSLATION_SHOW";
	public static final String VALUE_TYPE_STOCK_SHOW = "STOCK_SHOW";
	public static final String VALUE_TYPE_MUSIC_SHOW = "MUSIC_SHOW";
	public static final String VALUE_TYPE_PROG_RECOMMEND = "PROG_RECOMMEND";
	public static final String VALUE_TYPE_PROG_SEARCH_RESULT = "PROG_SEARCH_RESULT";
	public static final String VALUE_TYPE_CHANNEL_PROG_LIST = "CHANNEL_PROG_LIST";
	public static final String VALUE_TYPE_ROUTE_SHOW = "ROUTE_SHOW";
	public static final String VALUE_TYPE_POSITION_SHOW = "POSITION_SHOW";
	public static final String VALUE_TYPE_TALK_SHOW = "TALK_SHOW";
	public static final String VALUE_TYPE_ERROR_SHOW = "ERROR_SHOW";
	public static final String VALUE_TYPE_APP_LAUNCH = "APP_LAUNCH";
	public static final String VALUE_TYPE_APP_UNINSTALL = "APP_UNINSTALL";
	public static final String VALUE_TYPE_APP_EXIT = "APP_EXIT";
	public static final String VALUE_TYPE_SETTING = "SETTING_SHOW";
	public static final String VALUE_TYPE_REMINDER_CONFIRM = "REMINDER_SHOW";
	public static final String VALUE_TYPE_REMINDER_OK = "REMINDER_OK";
	public static final String VALUE_TYPE_POI_SHOW = "POI_SHOW";
	public static final String VALUE_TYPE_MULTIPLE_SHOW = "MULTIPLE_SHOW";
	public static final String VALUE_TYPE_UI_HANDLE_SHOW="UI_HANDLE_SHOW";
	//note
	public static final String VALUE_TYPE_NOTE_SHOW = "NOTE_SHOW";
	// alarm 
	public static final String VALUE_TYPE_ALARM_SHOW = "ALARM_SHOW";
	// multiple app
	public static final String VALUE_TYPE_APP_MUTIPLEP_SHOW = "MUTIPLE_APP";
	//sms
	public static final String VALUE_TYPE_SMS_CANCEL = "SMS_CANCEL";
	public static final String VALUE_TYPE_SMS_OK 	= "SMS_OK";
	public static final String VALUE_TYPE_SMS_READ 	= "SMS_READ";
	
	//contact
	public static final String VALUE_TYPE_CONTACT_SHOW = "CONTACT_SHOW";
	public static final String VALUE_TYPE_CONTACT_ADD = "CONTACT_ADD";
	
	
	public static final String VALUE_TYPE_CHANNEL_SWITCH_SHOW="CHANNEL_SWITCH_SHOW";
	public static final String VALUE_TYPE_SHOP_SHOW = "SHOP_SHOW";
	// route
	public static final String VALUE_ROUTE_METHOD_UNKNOWN = "ROUTE_METHOD_UNKNOWN";
	public static final String VALUE_ROUTE_METHOD_BUS = "BUS";
	public static final String VALUE_ROUTE_METHOD_WALK = "WALK";
	public static final String VALUE_ROUTE_METHOD_CAR = "CAR";

	// setting
	public static final String VALUE_SETTING_ACT_OPEN = "ACT_OPEN";
	public static final String VALUE_SETTING_ACT_CLOSE = "ACT_CLOSE";
	public static final String VALUE_SETTING_ACT_INCREASE = "ACT_INCREASE";
	public static final String VALUE_SETTING_ACT_DECREASE = "ACT_DECREASE";
	public static final String VALUE_SETTING_ACT_MAX = "ACT_MAX";
	public static final String VALUE_SETTING_ACT_MIN = "ACT_MIN";
	public static final String VALUE_SETTING_ACT_CLEAR = "ACT_CLEAR";
	public static final String VALUE_SETTING_ACT_SET = "ACT_SET";
	public static final String VALUE_SETTING_ACT_LOOKUP = "ACT_LOOKUP";
	public static final String VALUE_SETTING_ACT_OPEN_CHANNEL = "ACT_OPEN_CHANNEL";
	public static final String VALUE_SETTING_ACT_SCREEN_SHOT = "ACT_SCREEN_SHOT";
	public static final String VALUE_SETTING_ACT_FASTF = "ACT_FASTF";
	public static final String VALUE_SETTING_ACT_FASTB = "ACT_FASTB";
	public static final String VALUE_SETTING_ACT_PLAY = "ACT_PLAY";
	public static final String VALUE_SETTING_ACT_PAUSE = "ACT_PAUSE";
	public static final String VALUE_SETTING_ACT_NEXT = "ACT_NEXT";
	public static final String VALUE_SETTING_ACT_PREV = "ACT_PREV";
	
	public static final String VALUE_SETTING_ACT_ARROW_UP = "ACT_ARROW_UP";
	public static final String VALUE_SETTING_ACT_ARROW_DOWN = "ACT_ARROW_DOWN";
	public static final String VALUE_SETTING_ACT_ARROW_LEFT = "ACT_ARROW_LEFT";
	public static final String VALUE_SETTING_ACT_ARROW_RIGHT = "ACT_ARROW_RIGHT";
	public static final String VALUE_SETTING_ACT_BACK = "ACT_BACK";
	
	public static final String VALUE_SETTING_ACT_PAGE_UP = "ACT_PAGE_UP";
	public static final String VALUE_SETTING_ACT_PAGE_DOWN = "ACT_PAGE_DOWN";

	public static final String VALUE_SETTING_OBJ_VOLUMN = "OBJ_VOLUMN";
	public static final String VALUE_SETTING_OBJ_LIGHT = "OBJ_LIGHT";
	public static final String VALUE_SETTING_OBJ_FLOW = "OBJ_FLOW";
	public static final String VALUE_SETTING_OBJ_RINGTONE = "OBJ_RINGTONE";
	public static final String VALUE_SETTING_OBJ_WALLPAPER = "OBJ_WALLPAPER";
	public static final String VALUE_SETTING_OBJ_FACE = "OBJ_FACE";
	public static final String VALUE_SETTING_OBJ_TIME = "OBJ_TIME";
	public static final String VALUE_SETTING_OBJ_3G = "OBJ_3G";
	public static final String VALUE_SETTING_OBJ_WIFI = "OBJ_WIFI";
	public static final String VALUE_SETTING_OBJ_WIFI_SPOT = "OBJ_WIFI_SPOT";
	public static final String VALUE_SETTING_OBJ_BLUETOOTH = "OBJ_BLUETOOTH";
	public static final String VALUE_SETTING_OBJ_GPS = "OBJ_GPS";
	public static final String VALUE_SETTING_OBJ_ROTATION = "OBJ_ROTATION";
	public static final String VALUE_SETTING_OBJ_AUTOLIGHT = "OBJ_AUTOLIGHT";
	public static final String VALUE_SETTING_OBJ_MODEL_STANDARD = "OBJ_MODEL_STANDARD";
	public static final String VALUE_SETTING_OBJ_MODEL_MUTE = "OBJ_MODEL_MUTE";
	public static final String VALUE_SETTING_OBJ_MODEL_VIBRA = "OBJ_MODEL_VIBRA";
	public static final String VALUE_SETTING_OBJ_MODEL_INAIR = "OBJ_MODEL_INAIR";
	public static final String VALUE_SETTING_OBJ_MODEL_INCAR = "OBJ_MODEL_INCAR";
	public static final String VALUE_SETTING_OBJ_MODEL_SAVEPOWER = "OBJ_MODEL_SAVEPOWER";
	public static final String VALUE_SETTING_OBJ_MODEL_SAVEFLOW = "OBJ_MODEL_SAVEFLOW";
	public static final String VALUE_SETTING_OBJ_MODEL_GUEST = "OBJ_MODEL_GUEST";
	public static final String VALUE_SETTING_OBJ_MODEL_OUTDOOR = "OBJ_MODEL_OUTDOOR";
	public static final String VALUE_SETTING_OBJ_MEMORY = "OBJ_MEMORY";
	public static final String VALUE_SETTING_OBJ_DEVICE = "OBJ_DEVICE";
	public static final String VALUE_SETTING_OBJ_SS_TV = "OBJ_SS_TV";
	public static final String VALUE_SETTING_OBJ_CHANNEL = "OBJ_CHANNEL";
	
	public static final String VALUE_SETTING_OBJ_MENU = "OBJ_MENU";
	public static final String VALUE_SETTING_OBJ_HOME = "OBJ_HOME";

	public static final String VALUE_TYPE_SOME_PERSON = "MUTIPLE_CONTACTS";
	public static final String VALUE_TYPE_INPUT_CONTACT = "INPUT_CONTACT";
	public static final String VALUE_TYPE_CALL_START = "CALL";
	public static final String VALUE_TYPE_SOME_NUMBERS= "MUTIPLE_NUMBERS";
	public static final String VALUE_TYPE_CALL_ONE_NUMBER= "CONFIRM_CALL";
	public static final String VALUE_TYPE_CALL_OK = "CALL_OK";
	public static final String VALUE_TYPE_CALL_CANCEL = "CALL_CANCEL";
	
	/**help*/
	public static final String VALUE_TYPE_HELP_SHOW = "HELP_SHOW";
	
	/**fullvoice*/
	public static final String DOMAIN_FULLVOICE_SHOW = "DOMAIN_FULLVOICE_SHOW";
	
	public static final String VALUE_TYPE_INPUT_MSG_CONTENT = "INPUT_FREETEXT_SMS";
	
	/*weibo*/
	public static final String VALUE_TYPE_INPUT_CONTENT_WEIBO	 = "INPUT_FREETEXT_WEIBO";
	public static final String VALUE_TYPE_WEIBO_OK = "CONFIRM_WEIBO";
	public static final String VALUE_TYPE_WEIBO_CANCEL = "WEIBO_CANCEL";
	public static final String VALUE_TYPE_WEIBO_CONTENT_TXT = "content";
	
	public static final int VALUE_WEIBO_CLIENT_ID_SINA = 0;
	public static final int VALUE_WEIBO_CLIENT_ID_TENCENT = 1;
	public static final int VALUE_WEIBO_CLIENT_ID_RENREN = 2;
	
	public static final String VALUE_WEIBO_VENDOR_SINA = "SINA";
	public static final String VALUE_WEIBO_VENDOR_TENCENT = "TENCENT";
	public static final String VALUE_WEIBO_VENDOR_RENREN = "RENREN";

	public static final String VALUE_UI_PROTOCAL_SHOW = "UI_PROTOCAL_SHOW";
	
	// origin type - domain
	public static final String DOMAIN_ALARM = "cn.yunzhisheng.alarm";
	public static final String DOMAIN_APP = "cn.yunzhisheng.appmgr";
	public static final String DOMAIN_CALL = "cn.yunzhisheng.call";
	public static final String DOMAIN_CONTACT = "cn.yunzhisheng.contact";
	public static final String DOMAIN_CONTACT_SEND = "cn.yunzhisheng.contact";
	public static final String DOMAIN_FLIGHT = "cn.yunzhisheng.flight";
	public static final String DOMAIN_INCITY_SEARCH = "cn.yunzhisheng.localsearch";
	public static final String DOMAIN_MUSIC = "cn.yunzhisheng.music";
	public static final String DOMAIN_NEARBY_SEARCH = "cn.yunzhisheng.localsearch";
	public static final String DOMAIN_NOTE = "cn.yunzhisheng.note";
	public static final String DOMAIN_POSITION = "cn.yunzhisheng.map";
	public static final String DOMAIN_ROUTE = "cn.yunzhisheng.map";
	public static final String DOMAIN_REMINDER = "cn.yunzhisheng.reminder";
	public static final String DOMAIN_SEARCH = "cn.yunzhisheng.websearch";
	public static final String DOMAIN_SITEMAP = "cn.yunzhisheng.website";
	public static final String DOMAIN_SMS = "cn.yunzhisheng.sms";
	public static final String DOMAIN_STOCK = "cn.yunzhisheng.stock";
	public static final String DOMAIN_TRAIN = "cn.yunzhisheng.train";
	public static final String DOMAIN_TV = "cn.yunzhisheng.tv";
	public static final String DOMAIN_WEATHER = "cn.yunzhisheng.weather";
	public static final String DOMAIN_WEIBO = "cn.yunzhisheng.microblog";
	public static final String DOMAIN_YELLOWPAGE = "cn.yunzhisheng.hotline";
	public static final String DOMAIN_WEBSEARCH = "cn.yunzhisheng.websearch";
	public static final String DOMAIN_CALENDAR = "cn.yunzhisheng.calendar";
	public static final String DOMAIN_NEWS = "cn.yunzhisheng.news";
	public static final String DOMAIN_COOKBOOK = "cn.yunzhisheng.cookbook";
	public static final String DOMAIN_TRANSLATION = "cn.yunzhisheng.translation";
	public static final String DOMAIN_DIANPING = "cn.yunzhisheng.localsearch";
	public static final String DOMAIN_SETTING = "cn.yunzhisheng.setting";
	public static final String DOMAIN_MOVIE = "cn.yunzhisheng.movie";
	public static final String DOMAIN_NOVEL = "cn.yunzhisheng.novel";
	public static final String DOMAIN_VIDEO = "cn.yunzhisheng.video";
	public static final String DOMAIN_CHAT = "cn.yunzhisheng.chat";
	public static final String DOMAIN_HELP = "cn.yunzhisheng.help";
	public static final String DOMAIN_HOTEL = "cn.yunzhisheng.hotel";
	
	public static final String DOMAIN_ERROR = "DOMAIN_ERROR";
	public static final String DOMAIN_LOCAL = "DOMAIN_LOCAL";
	public static final String DOMAIN_NOSUPPORT_CHANNEL="DOMAIN_NOSUPPORT_CHANNEL";
	
	public static final String DOMAIN_CHANNEL_SWITCH = "cn.yunzhisheng.setting.tv";
	public static final String DOMAIN_SHOP = "cn.yunzhisheng.shopping";

	public static final String DOMAIN_MOBILE_CONTROL = "DOMAIN_MOBILE_CONTROL";
	// memo
	public static final String KEY_TEXT = "text";
	public static final String KEY_MORNING = "am";
	public static final String KEY_AFTERNOON = "pm";

	public static final String KEY_YEAR_REPEAT = "yearrep";
	public static final String KEY_MONTH_REPEAT = "monthrep";
	public static final String KEY_WEEK_REPEAT = "weekrep";
	public static final String KEY_YEAR = "yy";
	public static final String KEY_MONTH = "MM";
	public static final String KEY_DAY = "dd";
	public static final String KEY_HOUR = "hh";
	public static final String KEY_MINUTE = "mm";
	public static final String KEY_WEEKDAY = "ww";

	public static final String KEY_YEAR_STEP = "year";
	public static final String KEY_MON_STEP = "month";
	public static final String KEY_DAY_STEP = "day";
	public static final String KEY_HOUR_STEP = "hour";
	public static final String KEY_MIN_STEP = "minute";
	public static final String KEY_WEEK_STEP = "week";

	public static final String KEY_REPEATDATE = "repeatDate";
	public static final String KEY_DATETIME = "dateTime";
	public static final String KEY_LABEL = "label";
	
	/********************** session message *********************/
	public static final int MESSAGE_START_TALK = 1001;
	public static final int MESSAGE_STOP_TALK = 1002;
	public static final int MESSAGE_CANCEL_TALK = 1003;
	public static final int MESSAGE_SESSION_DONE = 1004;
	public static final int MESSAGE_SESSION_CANCEL = 1005;
	public static final int MESSAGE_DISABLE_MICROPHONE = 1006;
	public static final int MESSAGE_ENABLE_MICROPHONE = 1007;
	public static final int MESSAGE_REQUEST_MUSIC_START = 1008;
	public static final int MESSAGE_REQUEST_PLAY_TTS = 1009;
	public static final int MESSAGE_REQUEST_CANCEL_TTS = 1010;
	public static final int MESSAGE_NEW_PROTOCAL = 1011;
	
	public static final int MESSAGE_VAD_RECORD_STOP = 1012;
	
	public static final int MESSAGE_ADD_ANSWER_VIEW = 1013;
	public static final int MESSAGE_ADD_ANSWER_TEXT = 1014;
	public static final int MESSAGE_ADD_QUESTION_TEXT = 1015;
	public static final int MESSAGE_UI_OPERATE_PROTOCAL = 1016;
	public static final int MESSAGE_TASK_DELY = 1017;
	public static final int MESSAGE_OPERATION_TIMEOUT = 1018;
	public static final int MESSAGE_REQUEST_RESET_TIMER = 1019;
	public static final int MESSAGE_REQUEST_CANCEL_TIMER = 1020;
	public static final int MESSAGE_HIDE_WINDOW = 1021;
	 
	
	public static final int OPERATION_WEIBO_IMAGE_REMOVE = 20001;
	public static final int OPERATION_WEIBO_IMAGE_ADD_FROM_CAMERA = 20002;
	public static final int OPERATION_WEIBO_IMAGE_ADD_FROM_ALBUM = 20003;	
	/********************** session request code *********************/
	public static final int REQUEST_CODE_WEIBO_TAKE_PHOTO = 10001;
	public static final int REQUEST_CODE_WEIBO_SELECT_IMAGE = 10002;
	public static final int REQUEST_CODE_WEIBO_TENCENT_AUTHOR = 10003;
	public static final int REQUEST_CODE_WEIBO_SINA_AUTHOR = 32973;
	public static final int REQUEST_CODE_WEIBO_SINA_STATUS_OK = 33000;
	public static final int REQUEST_CODE_WEIBO_SINA_STATUS_FAILED = 33001;
	
	public static final int MESSAGE_MOREFUNCTION_PROTOCAL = 40001;
	
	public final static String TYPE_VERSION_INFO = "VERSION_INFO";
	public final static String TYPE_VERSION_DOWNLOAD = "VERSION_DOWNLOAD";
	
	public final static String ACTION_WEATHER_CITY = "cn.yunzhisheng.voicetv.weatherCity";
	public final static String ACTION_SET_TV_NAME = "cn.yunzhisheng.voicetv.setTvName";
	
	public final static String ACTION_MUSIC_START = "cn.yunzhisheng.voicetv.music.start";
	public final static String ACTION_MUSIC_STOP = "cn.yunzhisheng.voicetv.music.stop";
	public final static String ACTION_MUSIC_PLAY = "cn.yunzhisheng.voicetv.music.play";
	public final static String ACTION_MUSIC_PAUSE = "cn.yunzhisheng.voicetv.music.pause";
	public final static String ACTION_MUSIC_PREV = "cn.yunzhisheng.voicetv.music.prev";
	public final static String ACTION_MUSIC_NEXT = "cn.yunzhisheng.voicetv.music.next";
	
	
	public static final String KEY_OLD_CODE = "oldCode";
	public static final String KEY_NEW_CODE = "newCode";
	public static final String KEY_LOG = "log";
	public static final String KEY_HAS_NEW = "hasNew";
	public static final String KEY_HEARTBEAT_PACKET = "heartbeatPacket";
	public static final String KEY_SETTVNAME_SUCCESS = "setTvNameSuccess";
	
	public static final String KEY_SEND_MUSIC_STATUS = "musicStatus";
	
	public static final int MUSIC_STATUS_STOP = 0;
	public static final int MUSIC_STATUS_START = 1;
	public static final int MUSIC_STATUS_PAUSE = 2;
	public static final int MUSIC_STATUS_BUFFER = 3;
	public static final int MUSIC_STATUS_PROGRESS = 4;
	public static final int MUSIC_STATUS_PLAY = 5;
}
