package com.music.ui.view.gesturepressword;

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
import com.music.MusicApplication;
import com.music.ui.view.activity.BaseActivity;
import com.music.ui.widget.lockpatternview.LockPatternUtils;
import com.music.ui.widget.lockpatternview.LockPatternView;
import com.music.ui.widget.lockpatternview.LockPatternView.Cell;
@ContentView(R.layout.activity_gesturepassword_unlock)
public class UnlockGesturePasswordActivity extends BaseActivity {
	private LockPatternView mLockPatternView;
	private int mFailedPatternAttemptsSinceLastTimeout = 0;
	private CountDownTimer mCountdownTimer = null;
	/** Handler **/
	private Handler mHandler = new Handler();
	private TextView mHeadTextView;
	private Animation mShakeAnim;

	@ViewInject(R.id.gesturepwd_unlock_forget)
	private TextView gesturepwd_unlock_forget;
	/**
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
//		setContentView(R.layout.activity_gesturepassword_unlock);

		
		
//		if (ApplicationUtil.getAppLockState(this) != 1) {
//			startActivity(LocalMusicActivity.class);
//			finish();
//			return;
//		}
		mLockPatternView = (LockPatternView) this
				.findViewById(R.id.gesturepwd_unlock_lockview);
		mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
		mLockPatternView.setTactileFeedbackEnabled(true);
		mHeadTextView = (TextView) findViewById(R.id.gesturepwd_unlock_text);
		mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);
		
		DeBug.d(this, ".......onCreate:"+(SystemClock.currentThreadTimeMillis()-start)/1000.0+" s");
		
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mCountdownTimer != null)//
			mCountdownTimer.cancel();//
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

			if (pattern == null)//
				return;

			if (MusicApplication.getInstance().getLockPatternUtils()
					.checkPattern(pattern)) {//

				mLockPatternView
						.setDisplayMode(LockPatternView.DisplayMode.Correct);

				showToast("aaa");
				ApplicationUtil.setAppToBack(
						UnlockGesturePasswordActivity.this, 0);
				setResult(0, new Intent());
				finish();

			} else {

				mLockPatternView
						.setDisplayMode(LockPatternView.DisplayMode.Wrong);

				if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {//

					mFailedPatternAttemptsSinceLastTimeout++;
					int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT
							- mFailedPatternAttemptsSinceLastTimeout;
					if (retry >= 0) {
						if (retry == 0)//
							showToast("bbb");
						mHeadTextView.setText("bb " + retry + "ss");
						mHeadTextView.setTextColor(Color.RED);
						mHeadTextView.startAnimation(mShakeAnim);
					}

				} else {//
					showToast("ddd");
				}

				if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) {// ��������ﵽ�������
					mHandler.postDelayed(attemptLockout, 2000);

				} else {//
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
			mLockPatternView.clearPattern();
			mLockPatternView.setEnabled(false);
			mCountdownTimer = new CountDownTimer(
					LockPatternUtils.FAILED_ATTEMPT_TIMEOUT_MS + 1, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
					if (secondsRemaining > 0) {//
						mHeadTextView.setText(secondsRemaining + " ggg");
					} else {//
						mHeadTextView.setText("hhhh");
						mHeadTextView.setTextColor(Color.WHITE);
					}

				}

				@Override
				public void onFinish() {
					mLockPatternView.setEnabled(true);
					mFailedPatternAttemptsSinceLastTimeout = 0;
				}
			}.start();
		}
	};

}
