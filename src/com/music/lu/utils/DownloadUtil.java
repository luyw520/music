package com.music.lu.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadUtil {
	public static boolean downloadUrlToStream(String urlPath, OutputStream outputStream) {
		HttpURLConnection con = null;
		BufferedInputStream bis = null;
		BufferedOutputStream out = null;
		URL url = null;
		try {
			url = new URL(urlPath);

			con = (HttpURLConnection) url.openConnection();
			con.connect();
			byte[] buf = new byte[1024];

			bis = new BufferedInputStream(con.getInputStream());
			out = new BufferedOutputStream(outputStream);
			int len = 0;
			while ((len = bis.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
			return true;
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			if (con != null) {
				con.disconnect();
			}

			try {
				if (out != null) {
					out.close();
				}

				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return false;
	}
}
