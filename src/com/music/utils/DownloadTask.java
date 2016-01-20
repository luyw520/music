package com.music.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

import android.os.Handler;
import android.util.Log;

public class DownloadTask extends Thread {
	@SuppressWarnings("unused")
	private int blockSize, downloadSizeMore, fileSize, downloadedSize;
	@SuppressWarnings("unused")
	private int threadNum = 5;
	String urlStr, threadNo, fileName;
	@SuppressWarnings("unused")
	private Handler handler;
	public DownloadTask(String urlStr, int threadNum, String fileName) {
		this.urlStr = urlStr;
		this.threadNum = threadNum;
		this.fileName = fileName;
	}
	public DownloadTask(String urlStr, String fileName,Handler handler) {
		this.urlStr = urlStr;
		this.fileName = fileName;
		this.handler=handler;
	}

	@Override
	public void run() {/*
		FileDownloadThread[] fds = new FileDownloadThread[threadNum];
		try {
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();
			// 防止返回-1
			InputStream in = conn.getInputStream();
			// 获取下载文件的总大小
			fileSize = conn.getContentLength();
			Log.i("bb", "======================fileSize:" + fileSize);
			// 计算每个线程要下载的数据量
			blockSize = fileSize / threadNum;
			// 解决整除后百分比计算误差
			downloadSizeMore = (fileSize % threadNum);
			File file = new File("/sdcard/lu/music/",fileName);
			for (int i = 0; i < threadNum; i++) {
				Log.i("bb", "======================i:" + i);
				// 启动线程，分别下载自己需要下载的部分
				FileDownloadThread fdt = new FileDownloadThread(url, file, i
						* blockSize, (i + 1) * blockSize - 1);
				fdt.setName("Thread" + i);
				fdt.start();
				fds[i] = fdt;
			}
			boolean finished = false;
			while (!finished) {
				// 先把整除的余数搞定
				downloadedSize = downloadSizeMore;
				finished = true;
				for (int i = 0; i < fds.length; i++) {
					downloadedSize += fds[i].getDownloadSize();
					if (!fds[i].isFinished()) {
						finished = false;
					}
				}
				if(finished){
					 handler.sendEmptyMessage(0);
				}
				
				// 线程暂停一秒
//				sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	*/}

	class FileDownloadThread extends Thread {
		private static final int BUFFER_SIZE = 1024;
		private URL url;
		private File file;
		private int startPosition;
		private int endPosition;
		private int curPosition;
		// 标识当前线程是否下载完成
		private boolean finished = false;
		private int downloadSize = 0;

		public FileDownloadThread(URL url, File file, int startPosition,
				int endPosition) {
			this.url = url;
			this.file = file;
			this.startPosition = startPosition;
			this.curPosition = startPosition;
			this.endPosition = endPosition;
		}

		@Override
		public void run() {
			BufferedInputStream bis = null;
			RandomAccessFile fos = null;
			byte[] buf = new byte[BUFFER_SIZE];
			URLConnection con = null;
			try {
				con = url.openConnection();
				con.setAllowUserInteraction(true);
				// 设置当前线程下载的起止点
				con.setRequestProperty("Range", "bytes=" + startPosition + "-"
						+ endPosition);
				Log.i("bb", Thread.currentThread().getName() + "  bytes="
						+ startPosition + "-" + endPosition);
				// 使用java中的RandomAccessFile 对文件进行随机读写操作
				fos = new RandomAccessFile(file, "rw");
				// 设置写文件的起始位置
				fos.seek(startPosition);
				bis = new BufferedInputStream(con.getInputStream());
				// 开始循环以流的形式读写文件
				while (curPosition < endPosition) {
					int len = bis.read(buf, 0, BUFFER_SIZE);
					if (len == -1) {
						break;
					}
					fos.write(buf, 0, len);
					curPosition = curPosition + len;
					if (curPosition > endPosition) {
						downloadSize += len - (curPosition - endPosition) + 1;
					} else {
						downloadSize += len;
					}
				}
				// 下载完成设为true
				this.finished = true;
				bis.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public boolean isFinished() {
			return finished;
		}

		public int getDownloadSize() {
			return downloadSize;
		}
	}
}
