package com.music.view.gesturepressword;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.music.lu.R;
import com.music.utils.ApplicationUtil;
import com.music.utils.DeBug;
import com.music.utils.DialogUtil;
import com.music.view.MusicApplication;
import com.music.view.activity.BaseActivity;
import com.music.widget.lockpatternview.LockPatternUtils;
import com.music.widget.lockpatternview.LockPatternView;
import com.music.widget.lockpatternview.LockPatternView.Cell;
@ContentView(R.layout.activity_gesturepassword_unlock)
public class UnlockGesturePasswordActivity extends BaseActivity {
	/** 中间解锁图案 **/
	private LockPatternView mLockPatternView;
	/** 解锁错误次数 **/
	private int mFailedPatternAttemptsSinceLastTimeout = 0;
	/** 计时器 **/
	private CountDownTimer mCountdownTimer = null;
	/** Handler **/
	private Handler mHandler = new Handler();
	/** 顶部文本 **/
	private TextView mHeadTextView;
	private Animation mShakeAnim;

	@ViewInject(R.id.gesturepwd_unlock_forget)
	private TextView gesturepwd_unlock_forget;
	/**
	 * 弹出提示信息
	 * 
	 * @param message
	 */
	private void showToast(CharSequence message) {
		DialogUtil.showToast(this, message);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		long start=SystemClock.currentThreadTimeMillis();
		super.onCreate(savedInstanceState);
		DeBug.d(this, "onCreate........");
		// 设置布局
//		setContentView(R.layout.activity_gesturepassword_unlock);

		
		
//		if (ApplicationUtil.getAppLockState(this) != 1) {
//			startActivity(LocalMusicActivity.class);
//			finish();
//			return;
//		}
		// 根据id在布局中找到控件对象
		mLockPatternView = (LockPatternView) this
				.findViewById(R.id.gesturepwd_unlock_lockview);
		mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
		mLockPatternView.setTactileFeedbackEnabled(true);
		mHeadTextView = (TextView) findViewById(R.id.gesturepwd_unlock_text);
		mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);
		
		DeBug.d(this, ".......onCreate:"+(SystemClock.currentThreadTimeMillis()-start)/1000.0+" s");
		
	}

	@Override
	protected void onResume() {
		super.onResume();

		// 判断是否设置了锁屏密码,如果没设置,跳转到设置界面
		// if (!App.getInstance().getLockPatternUtils().savedPatternExists()) {
		// startActivity(new Intent(this, GuideGesturePasswordActivity.class));
		// finish();
		// }
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 判断计时器对象是否为空
		if (mCountdownTimer != null)// 不为空
			mCountdownTimer.cancel();// 取消计时器
	}
	@OnClick({R.id.gesturepwd_unlock_forget})
	public void viewClick(View view){
		DeBug.d(this, "...........viewClick");
		switch (view.getId()) {
		case R.id.gesturepwd_unlock_forget:
			
			startActivity(new Intent(this,CreateGesturePasswordActivity.class));
//			finish();
			break;

		default:
			break;
		}
	}
	/**
	 * 清除绘制的图案,恢复到初始状态
	 */
	private Runnable mClearPatternRunnable = new Runnable() {
		public void run() {
			mLockPatternView.clearPattern();
		}
	};
	protected LockPatternView.OnPatternListener mChooseNewLockPatternListener = new LockPatternView.OnPatternListener() {
		public void onPatternStart() {
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
			patternInProgress();
		}
		public void onPatternCleared() {
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
		}
		public void onPatternDetected(List<LockPatternView.Cell> pattern) {

			if (pattern == null)// 判断pattern是否为空
				return;

			// 判断解锁是否成功
			if (MusicApplication.getInstance().getLockPatternUtils()
					.checkPattern(pattern)) {// 成功

				// 设置当前模式为正确而模式
				mLockPatternView
						.setDisplayMode(LockPatternView.DisplayMode.Correct);

				// 设置Intent跳转目标
				// Intent intent = new
				// Intent(UnlockGesturePasswordActivity.this,
				// GuideGesturePasswordActivity.class);
				// // 打开新的Activity
				// startActivity(intent);
				showToast("解锁成功");
				ApplicationUtil.setAppToBack(
						UnlockGesturePasswordActivity.this, 0);
				setResult(0, new Intent());
				// 结束当前的Activity
				finish();

			} else {// 未成功

				// 设置当前模式为错误模式
				mLockPatternView
						.setDisplayMode(LockPatternView.DisplayMode.Wrong);

				// 判断输入长度
				if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {// 输入长度达到最低要求

					// 统计输入错误次数
					mFailedPatternAttemptsSinceLastTimeout++;
					// 统计剩余的解锁次数
					int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT
							- mFailedPatternAttemptsSinceLastTimeout;
					// 判断剩余的解锁次数
					if (retry >= 0) {
						if (retry == 0)// 如果剩余次数等于0,通知用户30秒后重试
							showToast("您已5次输错密码，请30秒后再试");
						mHeadTextView.setText("密码错误，还可以再输入" + retry + "次");
						mHeadTextView.setTextColor(Color.RED);
						mHeadTextView.startAnimation(mShakeAnim);
					}

				} else {// 输入长度未达到要求
					showToast("输入长度不够，请重试");
				}

				// 判断输入错误次数
				if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) {// 错误次数达到最高限制
					// 通知禁用解锁区域30秒,30秒后重置获得5次解锁机会
					mHandler.postDelayed(attemptLockout, 2000);

				} else {// 错误次数未达到最高限制
					// 通知清除绘制的图案,恢复所有图案状态
					mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
				}
			}
		}

		private void patternInProgress() {
		}

		public void onPatternCellAdded(List<Cell> pattern) {
			// TODO Auto-generated method stub

		}
	};

	Runnable attemptLockout = new Runnable() {

		@Override
		public void run() {
			// 清除已经绘制的图案
			mLockPatternView.clearPattern();
			// 禁用中间图案解锁
			mLockPatternView.setEnabled(false);
			// 使用计时器机型计时
			mCountdownTimer = new CountDownTimer(
					LockPatternUtils.FAILED_ATTEMPT_TIMEOUT_MS + 1, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					// 计算过去的秒数
					int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
					if (secondsRemaining > 0) {// 如果大于0
						// 每隔一秒更新顶部文本信息
						mHeadTextView.setText(secondsRemaining + " 秒后重试");
					} else {// 倒计时结束
						mHeadTextView.setText("请绘制手势密码");
						mHeadTextView.setTextColor(Color.WHITE);
					}

				}

				@Override
				public void onFinish() {
					// 启用中间解锁区域
					mLockPatternView.setEnabled(true);
					// 重置输入错误次数
					mFailedPatternAttemptsSinceLastTimeout = 0;
				}
			}.start();
		}
	};

}
