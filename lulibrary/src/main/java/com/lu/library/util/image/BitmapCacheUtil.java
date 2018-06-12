package com.lu.library.util.image;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import com.music.utils.DiskLruCache;
import com.music.utils.DownloadUtil;
import com.music.utils.FileUtils;
import com.music.utils.LogUtil;

@SuppressLint("NewApi")
public class BitmapCacheUtil {
	private static final String TAG = "BitmapCacheUtil";
	private DiskLruCache mDiskLruCache = null;
	private LruCache<String, Bitmap> mMemoryCache;
	private Context context;
	private static BitmapCacheUtil bitmapCacheUtil;

	private BitmapCacheUtil(Context context) {
		this.context = context;
		openDiskLruCache();
		openLruCache();
	}

	@SuppressLint("NewApi")
	private void openLruCache() {
		int maxSize = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxSize / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// TODO Auto-generated method stub
				return bitmap.getByteCount();
			}
		};
		LogUtil.d(TAG, "openLruCache");
	}

	/**
	 * add bitmap to memeorycache
	 *
	 * @param key
	 * @param bitmap
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemoryCache(key) == null && bitmap!=null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemoryCache(String key) {
		if (key == null) {
			return null;
		}
		return mMemoryCache.get(key);
	}

	public static void init(Context context) {
		if (bitmapCacheUtil == null) {
			bitmapCacheUtil = new BitmapCacheUtil(context);
		}
	}

	public static BitmapCacheUtil getDefalut() {
		return bitmapCacheUtil;
	}

	private void openDiskLruCache() {

		File cachDir = FileUtils.getDiskCacheDir(context, "bitmap");
		if (!cachDir.exists()) {
			cachDir.mkdirs();
		}

		try {
			mDiskLruCache = DiskLruCache.open(cachDir, getAppVersion(context),
					1, 10 * 1024 * 1024);
			LogUtil.d("BitmapCacheUtil", "mDiskLruCache opened");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.d("BitmapCacheUtil", "mDiskLruCache opened faild");
		}
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	private String hashKeyForDisk(String key) {

		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());

			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;

	}

	private String bytesToHexString(byte[] digest) {
		// TODO Auto-generated method stub
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < digest.length; i++) {
			String hexString = Integer.toHexString(0xff & digest[i]);
			if (hexString.length() == 1) {
				builder.append('0');
			}
			builder.append(hexString);
		}
		return builder.toString();
	}

	public int getAppVersion(Context context) {

		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;

	}

	/**
	 * donw and diskcache
	 *
	 * @param url
	 */
	private void downAndCache(final String url) {

		try {
			String key = hashKeyForDisk(url);
			LogUtil.d(TAG, "downAndCache url:" + url);
			LogUtil.d(TAG, "downAndCache key:" + key);
			DiskLruCache.Editor editor = mDiskLruCache.edit(key);
			if (editor != null) {
				OutputStream outputStream = editor.newOutputStream(0);
				if (DownloadUtil.downloadUrlToStream(url, outputStream)) {
					editor.commit();
				} else {
					editor.abort();
				}
			}
			mDiskLruCache.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 *  diskCache
	 * @param url
	 */
	private void diskCache(final String url) {

		try {
			String key = hashKeyForDisk(url);
			LogUtil.d(TAG, "downAndCache url:" + url);
			LogUtil.d(TAG, "downAndCache key:" + key);
			DiskLruCache.Editor editor = mDiskLruCache.edit(key);
			if (editor != null) {
				OutputStream outputStream = editor.newOutputStream(0);
				if(outputStream!=null){
					editor.commit();
				}else{
					editor.abort();
				}
			}
			mDiskLruCache.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 *
	 * @param url
	 * @return
	 */
	public Bitmap getCacheBitmap(String url) {

		if (url == null) {
			return null;
		}
		Bitmap bitmap = getBitmapFromMemoryCache(url);
		if (bitmap == null) {
			bitmap = BitmapFactory.decodeStream(getImgInputStream(url));



			if (bitmap == null) {

				bitmap=BitmapFactory.decodeFile(url);

				if(bitmap==null){
					downAndCache(url);

					bitmap = BitmapFactory.decodeStream(getImgInputStream(url));
				}else{

					diskCache(url);
				}


			}
		}
		if (bitmap != null) {
			addBitmapToMemoryCache(url, bitmap);
//
		}
		return bitmap;
	}

	private InputStream getImgInputStream(String url) {

		String key = hashKeyForDisk(url);
		LogUtil.d(TAG, "getImgInputStream url:" + url);
		LogUtil.d(TAG, "getImgInputStream key:" + key);
		try {
			DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
			if (snapshot != null) {
				return snapshot.getInputStream(0);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public void closeCache() {
		try {
			mDiskLruCache.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void clearCache() {
		try {
			mDiskLruCache.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @return
	 */
	public long getCacheSize() {
		return mDiskLruCache.size();
	}

	/**
	 *
	 * @param size
	 * @return
	 */
	public String formatFileSize() {
		DecimalFormat df = new DecimalFormat("#.00");
		long size = getCacheSize();
		String fileSizeString = "sss";
		if (size < 1024) {
			fileSizeString = df.format((double) size) + "B";
		} else if (size < 1048576) {
			fileSizeString = df.format((double) size / 1024) + "K";
		} else if (size < 1073741824) {
			fileSizeString = df.format((double) size / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) size / 1073741824) + "G";
		}
		return fileSizeString;
	}
}
