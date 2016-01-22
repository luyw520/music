package com.music.broadcastreceiver;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

public class CustomPushReceiver extends XGPushBaseReceiver{

	private String TAG="CustomPushReceiver";

	@Override
	public void onDeleteTagResult(Context arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNotifactionClickedResult(Context context,
			XGPushClickedResult message) {
		if (context == null || message == null) {
			return;
		}
		@SuppressWarnings("unused")
		String text = "";
		if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
			// 通知在通知栏被点击啦。。。。。
			// APP自己处理点击的相关动作
			// 这个动作可以在activity的onResume也能监听，请看第3点相关内容
			text = "通知被打开 :" + message;
		} else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
			// 通知被清除啦。。。。
			// APP自己处理通知被清除后的相关动作
			text = "通知被清除 :" + message;
		}
		Log.d(TAG, "广播接收到通知被点击:" + message.toString()
				);
		// 获取自定义key-value
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject object=new JSONObject(customContent);
				
				if(!object.isNull("key1")){
					String valueString=object.getString("key1");
					Log.i(TAG, valueString);
					
				}
				if(!object.isNull("key2")){
					String valueString=object.getString("key2");
					Log.i(TAG, valueString);
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onNotifactionShowedResult(Context arg0, XGPushShowedResult arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegisterResult(Context context, int errorCode,
			XGPushRegisterResult message) {
		if (context == null || message == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = message + "注册成功";
			// 在这里拿token
			@SuppressWarnings("unused")
			String token = message.getToken();
		} else {
			text = message + "注册失败，错误码：" + errorCode;
		}
//		int a =XGPushBaseReceiver.SUCCESS;
		Log.i(TAG, text);
	}

	@Override
	public void onSetTagResult(Context context, int arg1, String tagName) {
		
		if (context == null) {
			return;
		}
		@SuppressWarnings("unused")
		String text = "";
		int errorCode=0;
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"设置成功";
		} else {
			text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
		}
//		Log.d(LogTag, text);
//		show(context, text);
	}

	@Override
	public void onTextMessage(Context arg0, XGPushTextMessage message) {
		Log.i(TAG, "onTextMessage");
		if(arg0==null || message==null) return;
		
		String textString="消息是:"+message.toString();
		Log.i(TAG, textString);
		String contentString=message.getCustomContent();
		try { 
			JSONObject object=new JSONObject(contentString);
			
			if(!object.isNull("key1")){
				String valueString=object.getString("key1");
				Log.i(TAG, valueString);
				
			}
			if(!object.isNull("key2")){
				String valueString=object.getString("key2");
				Log.i(TAG, valueString);
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onUnregisterResult(Context arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
