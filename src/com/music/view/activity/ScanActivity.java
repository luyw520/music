package com.music.view.activity;



import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.music.lu.R;
@ContentView(value=R.layout.activity_scan)
public class ScanActivity extends BaseHeaderActivity {
	@ViewInject(value = R.id.iv_search)
	protected ImageView iv_search;
	
	@ViewInject(value = R.id.iv_more)
	protected ImageView iv_more;
	@ViewInject(value = R.id.iv_back)
	protected ImageView iv_back;

	@ViewInject(value = R.id.tv_title)
	protected TextView tv_title;
	private final static int SCANNIN_GREQUEST_CODE = 1;
	/**
	 */
	private TextView mTextView ;
	/**
	 */
	private ImageView mImageView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_scan);
		
		mTextView = (TextView) findViewById(R.id.result); 
		mImageView = (ImageView) findViewById(R.id.qrcode_bitmap);
		
		Button mButton = (Button) findViewById(R.id.button1);
		mButton.setVisibility(View.GONE);
//		mButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(ScanActivity.this, MipcaCaptureActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
//			}
//		});
		initWidget();
	}
	@OnClick({R.id.iv_search,R.id.iv_back})
	public void viewClick(View view){
		switch (view.getId()) {
		case R.id.iv_search:
			Intent intent = new Intent();
			intent.setClass(ScanActivity.this, MipcaCaptureActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			break;
		case R.id.iv_back:
			finish();
			break;

		default:
			break;
		}
		
	}
	private void initWidget() {
		iv_more.setVisibility(View.INVISIBLE);
//		iv_search.setVisibility(View.GONE);
		tv_title.setText("ɨһɨ");
		
		
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if(resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();
				mTextView.setText(bundle.getString("result"));
				mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
			}
			break;
		}
    }	

}
