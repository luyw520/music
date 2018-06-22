package com.music.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lu.library.util.ScreenUtils;
import com.music.lu.R;
import com.music.ui.view.ImageFactoryCrop;
import com.music.ui.view.ImageFactoryFliter;
import com.music.utils.PhotoUtils;

/**
 * @author Administrator
 *
 */
@ContentView(value=R.layout.activity_imagefactory)
public class ImageFactoryActivity extends BaseActivity {
//	private HeaderLayout mHeaderLayout;
	private ViewFlipper mVfFlipper;
//	private Button mBtnLeft;
	private Button mBtnRight;

	private ImageFactoryCrop mImageFactoryCrop;
	private ImageFactoryFliter mImageFactoryFliter;
	private String mPath;
	private String mNewPath;
	private int mIndex = 0;
	private String mType;

	public static final String TYPE = "type";
	public static final String CROP = "crop";
	public static final String FLITER = "fliter";


	@ViewInject(value = R.id.iv_search)
	private ImageView iv_search;
	@ViewInject(value = R.id.iv_more)
	private ImageView iv_more;
	@ViewInject(value = R.id.iv_back)
	private ImageView iv_back;

	@ViewInject(value = R.id.tv_title)
	private TextView tv_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
		init();

		initWidget();
	}


	private void initWidget() {
//		iv_more.setVisibility(View.GONE);
		iv_search.setVisibility(View.GONE);
		tv_title.setText(R.string.crop);
		iv_more.setOnClickListener(new OnRightImageButtonClickListener());
	}
	protected void initViews() {
//		mHeaderLayout = (HeaderLayout) findViewById(R.id.imagefactory_header);
//		mHeaderLayout.init(HeaderStyle.TITLE_RIGHT_IMAGEBUTTON);
		mVfFlipper = (ViewFlipper) findViewById(R.id.imagefactory_vf_viewflipper);
//		mBtnLeft = (Button) findViewById(R.id.btn_back);
		mBtnRight = (Button) findViewById(R.id.btn_next);
	}



	@OnClick({R.id.btn_back,R.id.btn_next,R.id.iv_back})
	public void onClick(View view){

		switch (view.getId()) {
		case R.id.btn_back:
			if (mIndex == 0) {
				setResult(RESULT_CANCELED);
				finish();
			} else {
				if (FLITER.equals(mType)) {
					setResult(RESULT_CANCELED);
					finish();
				} else {
					mIndex = 0;
					initImageFactory();
					mVfFlipper.setInAnimation(ImageFactoryActivity.this,
							R.anim.push_right_in);
					mVfFlipper.setOutAnimation(ImageFactoryActivity.this,
							R.anim.push_right_out);
					mVfFlipper.showPrevious();
				}
			}
			break;
		case R.id.btn_next:
			if (mIndex == 1) {
				mNewPath = PhotoUtils.savePhotoToSDCard(mImageFactoryFliter
						.getBitmap());
				Intent intent = new Intent();
				intent.putExtra("path", mNewPath);
				setResult(RESULT_OK, intent);
				finish();
			} else {
				mNewPath = PhotoUtils.savePhotoToSDCard(mImageFactoryCrop
						.cropAndSave());
				mIndex = 1;
				initImageFactory();
				mVfFlipper.setInAnimation(ImageFactoryActivity.this,
						R.anim.push_left_in);
				mVfFlipper.setOutAnimation(ImageFactoryActivity.this,
						R.anim.push_left_out);
				mVfFlipper.showNext();
			}
			break;
		case R.id.iv_back:
			finish();
			break;

		default:
			break;
		}
	}
	@Override
	public void onBackPressed() {
		if (mIndex == 0) {
			setResult(RESULT_CANCELED);
			finish();
		} else {
			if (FLITER.equals(mType)) {
				setResult(RESULT_CANCELED);
				finish();
			} else {
				mIndex = 0;
				initImageFactory();
				mVfFlipper.setInAnimation(ImageFactoryActivity.this,
						R.anim.push_right_in);
				mVfFlipper.setOutAnimation(ImageFactoryActivity.this,
						R.anim.push_right_out);
				mVfFlipper.showPrevious();
			}
		}
	}

	private void init() {
		mPath = getIntent().getStringExtra("path");
		mType = getIntent().getStringExtra(TYPE);
		mNewPath = new String(mPath);
		if (CROP.equals(mType)) {
			mIndex = 0;
		} else if (FLITER.equals(mType)) {
			mIndex = 1;
			mVfFlipper.showPrevious();
		}
		initImageFactory();
	}

	private void initImageFactory() {
		switch (mIndex) {
		case 0:
				mImageFactoryCrop = new ImageFactoryCrop(this,
						mVfFlipper.getChildAt(0));
//			}
			mImageFactoryCrop.init(mPath, ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenHeight(this));
			mBtnRight.setText(R.string.next_step);
//			tv_title.setText(R.string.crop);
			break;

		case 1:
//			if (mImageFactoryFliter == null) {
				mImageFactoryFliter = new ImageFactoryFliter(this,
						mVfFlipper.getChildAt(1));
//			}
			mImageFactoryFliter.init(mNewPath);
//			tv_title.setText(R.string.crop);
			mBtnRight.setText(R.string.finished);
			break;
		}
	}

	private class OnRightImageButtonClickListener implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			switch (mIndex) {
			case 0:
				if (mImageFactoryCrop != null) {
					mImageFactoryCrop.Rotate();
				}
				break;

			case 1:
				if (mImageFactoryFliter != null) {
					mImageFactoryFliter.Rotate();
				}
				break;
			}

		}
	}
}
