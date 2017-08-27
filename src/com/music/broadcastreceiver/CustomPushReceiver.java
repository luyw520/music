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
			text = "aaa" + message;
		} else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
			text = "bbb" + message;
		}
		Log.d(TAG, "ccc" + message.toString()
				);
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
			text = message + "aaa";
			@SuppressWarnings("unused")
			String token = message.getToken();
		} else {
			text = message + "dddd" + errorCode;
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
			text = "\"" + tagName + "\"fff";
		} else {
			text = "\"" + tagName + "\"hhhh" + errorCode;
		}
//		Log.d(LogTag, text);
//		show(context, text);
	}

	@Override
	public void onTextMessage(Context arg0, XGPushTextMessage message) {
		Log.i(TAG, "onTextMessage");
		if(arg0==null || message==null) return;
		
		String textString="lll:"+message.toString();
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
