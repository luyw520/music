package com.music.activity.gesturepassword;

import com.music.MusicApplication;
import com.music.lu.R;
import com.music.widget.lockpatternview.App;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 用户密码设置引导界面
 * 
 * @author jgduan
 * 
 *         在用户初次进入应用时显示
 * 
 */
public class GuideGesturePasswordActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 设置布局
		setContentView(R.layout.activity_gesturepassword_guide);

		// 在布局中找到创建手势密码按钮并为其绑定点击事件监听器
		findViewById(R.id.gesturepwd_guide_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {

						// 清除保存过的密码
						MusicApplication.getInstance().getLockPatternUtils().clearLock();
						// 指定Intent跳转目标
						Intent intent = new Intent(
								GuideGesturePasswordActivity.this,
								CreateGesturePasswordActivity.class);
						// 打开新的Activity
						startActivity(intent);
						// 结束当前Activity
						finish();

					}
				});
	}

}
