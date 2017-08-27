package com.music.lrc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import android.content.Context;
import android.text.TextUtils;

import com.music.utils.DeBug;
import com.music.utils.FileUtils;

/**
 *
 * @author longdw(longdawei1988@gmail.com)
 * 
 */
public class LyricDownloadManager {
	private static final String TAG = LyricDownloadManager.class
			.getSimpleName();
	public static final String GB2312 = "GB2312";
	public static final String UTF_8 = "utf-8";
	private final int mTimeOut = 10 * 1000;
	private LyricXMLParser mLyricXMLParser = new LyricXMLParser();
	private URL mUrl = null;
	private int mDownloadLyricId = -1;
	private final static String BAIDU_MUSIC_URL = "http://box.zhangmen.baidu.com/x?op=12&count=1&title=";
	private final static String BAIDU_LRC_URL = "http://box.zhangmen.baidu.com/bdlrc/";
	@SuppressWarnings("unused")
	private Context mContext = null;

	public LyricDownloadManager(Context c) {
		mContext = c;
	}

	/*
	 */
	public String searchLyricFromWeb(String musicName, String singerName) {

		if (TextUtils.isEmpty(musicName) || TextUtils.isEmpty(singerName)) {
			return "";
		}
		try {

			musicName = URLEncoder.encode(musicName, UTF_8);
			singerName = URLEncoder.encode(singerName, UTF_8);
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}

		String strUrl = BAIDU_MUSIC_URL + musicName + "$$" + singerName
				+ "$$$$";

		try {
			mUrl = new URL(strUrl);
			DeBug.d(TAG, "uuu" + mUrl);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			HttpURLConnection httpConn = (HttpURLConnection) mUrl
					.openConnection();
			httpConn.setReadTimeout(mTimeOut);
			if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return null;
			}
			httpConn.connect();

			mDownloadLyricId = mLyricXMLParser.parseLyricId(httpConn
					.getInputStream());
			httpConn.disconnect();
		} catch (IOException e1) {
			e1.printStackTrace();
			DeBug.d(TAG, "httpsss");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			DeBug.d(TAG, "XMLggg");
			return null;
		}
		return fetchLyricContent(musicName, singerName);
	}

	/**
	 *
	 */
	private String fetchLyricContent(String musicName, String singerName) {
		if (mDownloadLyricId == -1) {
			return null;
		}
		BufferedReader br = null;
		StringBuilder content = null;
		String temp = null;
		String lyricURL = BAIDU_LRC_URL + mDownloadLyricId / 100 + "/"
				+ mDownloadLyricId + ".lrc";

		try {
			mUrl = new URL(lyricURL);
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}

		try {
			br = new BufferedReader(new InputStreamReader(mUrl.openStream(),
					GB2312));
			if (br != null) {
				content = new StringBuilder();
				while ((temp = br.readLine()) != null) {
					content.append(temp);
					DeBug.d(TAG, "<Lyric>" + temp);
				}
				br.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			DeBug.d(TAG, "hhhh");
		}

		try {
			musicName = URLDecoder.decode(musicName, UTF_8);
			singerName = URLDecoder.decode(singerName, UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (content != null) {

			// String folderPath =
			// PreferenceManager.getDefaultSharedPreferences(
			// mContext).getString(SettingFragment.KEY_LYRIC_SAVE_PATH,
			// Constant.LYRIC_SAVE_FOLDER_PATH);
			String folderPath = FileUtils.lrcPath();

			File savefolder = new File(folderPath);
			if (!savefolder.exists()) {
				savefolder.mkdirs();
			}
			String savePath = folderPath + File.separator + singerName + "-"
					+ musicName + ".lrc";
			// String savePath = folderPath + File.separator + musicName +
			// ".lrc";
			DeBug.d(TAG, "hhh:" + savePath);

			saveLyric(content.toString(), savePath);

			return savePath;
		} else {
			return null;
		}

	}

	private void saveLyric(String content, String filePath) {
		File file = new File(filePath);
		try {
			OutputStream outstream = new FileOutputStream(file);
			OutputStreamWriter out = new OutputStreamWriter(outstream);
			out.write(content);
			out.close();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}
}
