package com.music.animator;

import java.util.Random;

import com.music.lu.R;

import android.annotation.SuppressLint;
import android.app.Activity;
/**
 * activity切换效果类
 * @author Administrator
 *
 */
@SuppressLint("NewApi")
public class ActivityAnimator {
	public String[] animators = { "flipHorizontalAnimation",
			"flipVerticalAnimation", "fadeAnimation",
			"disappearTopLeftAnimation", "appearTopLeftAnimation",
			"disappearBottomRightAnimation", "unzoomAnimation" };
	/**
	 * 随机产生一个动画效果
	 * @return
	 */
	public String randomAnimator(){
		Random random=new Random();
		return animators[random.nextInt(animators.length)];
	}
	public void flipHorizontalAnimation(Activity a) {
		a.overridePendingTransition(R.anim.flip_horizontal_in,
				R.anim.flip_horizontal_out);
	}

	public void flipVerticalAnimation(Activity a) {
		a.overridePendingTransition(R.anim.flip_vertical_in,
				R.anim.flip_vertical_out);
	}

	public void fadeAnimation(Activity a) {
		a.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	public void disappearTopLeftAnimation(Activity a) {
		a.overridePendingTransition(R.anim.disappear_top_left_in,
				R.anim.disappear_top_left_out);
	}

	public void appearTopLeftAnimation(Activity a) {
		a.overridePendingTransition(R.anim.appear_top_left_in,
				R.anim.appear_top_left_out);
	}

	public void disappearBottomRightAnimation(Activity a) {
		a.overridePendingTransition(R.anim.disappear_bottom_right_in,
				R.anim.disappear_bottom_right_out);
	}

	public void appearBottomRightAnimation(Activity a) {
		a.overridePendingTransition(R.anim.appear_bottom_right_in,
				R.anim.appear_bottom_right_out);
	}

	public void unzoomAnimation(Activity a) {
		a.overridePendingTransition(R.anim.unzoom_in, R.anim.unzoom_out);
	}
}
