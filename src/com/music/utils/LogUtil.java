package com.music.utils;
import android.annotation.SuppressLint;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat") public class LogUtil {
	private static final boolean DEBUG = true;
	public static Boolean MYLOG_SWITCH = true;
	public static Boolean MYLOG_WRITE_TO_FILE = false;
	private static char MYLOG_TYPE = 'v';
	@SuppressLint("SdCardPath")
	private static String MYLOG_PATH_SDCARD_DIR = "/sdcard/";
	private static int SDCARD_LOG_FILE_SAVE_DAYS = 0;
	private static String MYLOGFILEName = "musiclog.txt";
	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat myLogSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");

	public static void w(String tag, Object msg) {
		log(tag, msg.toString(), 'w');
	}

	public static void e(String tag, Object msg) {
		log(tag, msg.toString(), 'e');
	}

	public static void d(String tag, Object msg) {
		log(tag, msg.toString(), 'd');
	}

	public static void i(String tag, Object msg) {
		log(tag, msg.toString(), 'i');
	}

	public static void v(String tag, Object msg) {
		log(tag, msg.toString(), 'v');
	}

	public static void w(String tag, String text) {
		log(tag, text, 'w');
	}

	public static void e(String tag, String text) {
		log(tag, text, 'e');
	}

	public static void d(String tag, String text) {
//		log(tag, text, 'd');
		DebugLog.d2("tag:"+tag+","+text);
	}

	public static void i(String tag, String text) {
//		log(tag, text, 'i');
		DebugLog.d2("tag:"+tag+","+text);
	}

	public static void v(String tag, String text) {
//		log(tag, text, 'v');
		DebugLog.d2("tag:"+tag+","+text);
	}
	private static void log(String tag, String msg, char level) {
		if (MYLOG_SWITCH) {
			if ('e' == level && ('e' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
				d(tag, msg);
			} else if ('w' == level && ('w' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
				d(tag, msg);
			} else if ('d' == level && ('d' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
				d(tag, msg);
			} else if ('i' == level && ('d' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
				d(tag, msg);
			} else {
				d(tag, msg);
			}
			if (MYLOG_WRITE_TO_FILE) {
				writeLogtoFile(String.valueOf(level), tag, msg);
			}
		}
	}

	public static void writeLogtoFile(String mylogtype, String tag, String text) {
		Date nowtime = new Date();
//		String needWriteFiel = logfile.format(nowtime);
		String needWriteMessage = myLogSdf.format(nowtime) + "    " + mylogtype + "    " + tag + "    " + text;
		File file = new File(MYLOG_PATH_SDCARD_DIR, MYLOGFILEName);
		Log.d("MyLog", "getAbsolutePath:" + file.getAbsolutePath());
		try {
			FileWriter filerWriter = new FileWriter(file, true);
			BufferedWriter bufWriter = new BufferedWriter(filerWriter);
			Log.d("MyLog", needWriteMessage);
			bufWriter.write(needWriteMessage);
			bufWriter.newLine();
			bufWriter.close();
			filerWriter.close();
		} catch (IOException e) {
			Log.d("MyLog", e.getMessage());
		}
	}

	public static void delFile() {
		String needDelFiel = logfile.format(getDateBefore());
		File file = new File(MYLOG_PATH_SDCARD_DIR, needDelFiel + MYLOGFILEName);
		if (file.exists()) {
			file.delete();
		}
	}

	private static Date getDateBefore() {
		Date nowtime = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(nowtime);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - SDCARD_LOG_FILE_SAVE_DAYS);
		return now.getTime();
	}
	 public static void d(Class<?> class1, String msg) {
	        if (DEBUG) {
	            d(class1.getSimpleName(), msg);
	        }

	    }
	    public static void d(Object o, String msg) {
	        if (DEBUG) {
	            DebugLog.d2(o.getClass().getSimpleName(),msg);
	        }

	    }

	    public static void e(Class<?> class1, String msg) {
	        if (DEBUG) {
				DebugLog.d2(class1.getSimpleName(), msg);
	        }
	    }

	    public static void i(Class<?> class1, String msg) {
	        if (DEBUG) {
				DebugLog.d2(class1.getSimpleName(), msg);
	        }
	    }

	    public static void v(Class<?> class1, String msg) {
	        if (DEBUG) {
				DebugLog.d2(class1.getSimpleName(), msg);
	        }
	    }

	    public static void w(Class<?> class1, String msg) {
	        if (DEBUG) {
	            d(class1.getSimpleName(), msg);
	        }
	    }
}
