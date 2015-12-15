package com.music.mp3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class Mp3ReadId3v2 {

	private InputStream mp3ips;
//	public String charset = "GBK"; //
	public String charset = "UTF-8"; //
	private Id3v2Info info;
	private String fileName;
	private File audioFile;

	public Mp3ReadId3v2(File in) {
		audioFile = in;
		fileName = in.getName();
		try {
			this.mp3ips = new FileInputStream(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		info = new Id3v2Info(audioFile, fileName, null, null, null, "未知");
	}

	public void readId3v2() throws Exception {
		try {
			readId3v2(1024 * 100);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// /**
	// *
	// * */
	// public void readId3v2(int buffSize) throws Exception {
	// try {
	// if (buffSize > mp3ips.available()) {
	// buffSize = mp3ips.available();
	// }
	// byte[] buff = new byte[buffSize];
	// mp3ips.read(buff, 0, buffSize);
	//
	// if (ByteUtil.indexOf("ID3".getBytes(), buff, 1, 512) == -1) {
	// mp3ips.close();
	// return;
	// }
	//
	// // 锟斤拷宄帮拷锟芥径鏉戯拷锟�
	// if (ByteUtil.indexOf("APIC".getBytes(), buff, 0, buffSize) != -1) {
	// int searLen = ByteUtil.indexOf(new byte[] { (byte) 0xFF, (byte) 0xFB },
	// buff);
	// int imgStart = ByteUtil.indexOf(new byte[] { (byte) 0xFF, (byte) 0xD8 },
	// buff);
	// int imgEnd = ByteUtil.lastIndexOf(new byte[] { (byte) 0xFF, (byte) 0xD9
	// }, buff, 1, searLen) + 2;
	// byte[] imgb = ByteUtil.cutBytes(imgStart, imgEnd, buff);
	// info.setApic(imgb);
	// }
	// if (ByteUtil.indexOf("TIT2".getBytes(), buff, 0, 512) != -1) {
	//
	// if (ByteUtil.indexOf(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF
	// }, buff) != -1) {
	// info.setTit2(new String(readInfo(buff, "TIT2"), 3, buff.length - 3,
	// charset));
	// } else {
	// info.setTit2(new String(readInfo(buff, "TIT2"), charset));
	// }
	// System.out.println("info:" + info.getTit2());
	// }
	//
	// if (ByteUtil.indexOf("TPE1".getBytes(), buff, 0, buffSize) != -1) {
	// if (ByteUtil.indexOf(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF
	// }, buff) != -1) {
	// info.setTpe1(new String(readInfo(buff, "TPE1"), 3, buff.length - 3,
	// charset));
	// } else {
	// info.setTpe1(new String(readInfo(buff, "TPE1"), charset));
	// }
	// System.out.println("info:" + info.getTpe1());
	//
	// }
	// if (ByteUtil.indexOf("TALB".getBytes(), buff, 0, buffSize) != -1) {
	// if (ByteUtil.indexOf(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF
	// }, buff) != -1) {
	// info.setTalb(new String(readInfo(buff, "TALB"), 3, buff.length - 3,
	// charset));
	// } else {
	// info.setTalb(new String(readInfo(buff, "TALB"), charset));
	// }
	// System.out.println("info:" + info.getTalb());
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// mp3ips.close();
	// }
	//
	// }

	public void readId3v2(int buffSize) throws Exception {
		try {
			if (buffSize > mp3ips.available()) {
				buffSize = mp3ips.available();
			}
			byte[] buff = new byte[buffSize];
			mp3ips.read(buff, 0, buffSize);
			if (ByteUtil.indexOf("ID3".getBytes(), buff, 1, 512) == -1) {
				mp3ips.close();
				return;
			}
			if (ByteUtil.indexOf("APIC".getBytes(), buff, 1, 512) != -1) {
				int searLen = ByteUtil.indexOf(new byte[] { (byte) 0xFF, (byte) 0xFB }, buff);
				int imgStart = ByteUtil.indexOf(new byte[] { (byte) 0xFF, (byte) 0xD8 }, buff);
				int imgEnd = ByteUtil.lastIndexOf(new byte[] { (byte) 0xFF, (byte) 0xD9 }, buff, 1, searLen) + 2;
				byte[] imgb = ByteUtil.cutBytes(imgStart, imgEnd, buff);
				info.setApic(imgb);
			}
			if (ByteUtil.indexOf("TIT2".getBytes(), buff, 1, buffSize) != -1) {
				info.setTit2(new String(readInfo(buff, "TIT2"), charset));
				System.out.println("TIT2:" + info.getTit2());
			}
			if (ByteUtil.indexOf("TPE1".getBytes(), buff, 1, buffSize) != -1) {
				info.setTpe1(new String(readInfo(buff, "TPE1"), charset));
				System.out.println("TPE1:" + info.getTpe1());
			}
			if (ByteUtil.indexOf("TALB".getBytes(), buff, 1, buffSize) != -1) {
				info.setTalb(new String(readInfo(buff, "TALB"), charset));
				System.out.println("TALB:" + info.getTalb());
			}
			// if (ByteUtil.indexOf("TPE1".getBytes(), buff, 0, buffSize) != -1)
			// {
			// if (ByteUtil.indexOf(new byte[] { (byte) 0xEF, (byte) 0xBB,
			// (byte) 0xBF }, buff) != -1) {
			// info.setTpe1(new String(readInfo(buff, "TPE1"), 3, buff.length -
			// 3, charset));
			// } else {
			// info.setTpe1(new String(readInfo(buff, "TPE1"), charset));
			// }
			// System.out.println("info:" + info.getTpe1());
			// }
			// if (ByteUtil.indexOf("TALB".getBytes(), buff, 0, buffSize) != -1)
			// {
			// if (ByteUtil.indexOf(new byte[] { (byte) 0xEF, (byte) 0xBB,
			// (byte) 0xBF }, buff) != -1) {
			// info.setTalb(new String(readInfo(buff, "TALB"), 3, buff.length -
			// 3, charset));
			// } else {
			// info.setTalb(new String(readInfo(buff, "TALB"), charset));
			// }
			// System.out.println("info:" + info.getTalb());
			// }
			// if (ByteUtil.indexOf("TIT2".getBytes(), buff, 0, 512) != -1) {
			// if (ByteUtil.indexOf(new byte[] { (byte) 0xEF, (byte) 0xBB,
			// (byte) 0xBF }, buff) != -1) {
			// info.setTit2(new String(readInfo(buff, "TIT2"), 3, buff.length -
			// 3, charset));
			// } else {
			// info.setTit2(new String(readInfo(buff, "TIT2"), charset));
			// }
			// System.out.println("info:" + info.getTit2());
			// }

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			mp3ips.close();
		}
	}

	// /**
	// * 鐠囪锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界粵锟�
	// **/
	// private byte[] readInfo(byte[] buff, String tag) {
	// int len = 0;
	// int offset = ByteUtil.indexOf(tag.getBytes(), buff);
	// len = buff[offset + 4] & 0xFF;
	// len = (len << 8) + (buff[offset + 5] & 0xFF);
	// len = (len << 8) + (buff[offset + 6] & 0xFF);
	// len = (len << 8) + (buff[offset + 7] & 0xFF);
	// len = len - 1;
	// return ByteUtil.cutBytes(ByteUtil.indexOf(tag.getBytes(), buff) + 13,
	// ByteUtil.indexOf(tag.getBytes(), buff) + 11 + len, buff);
	//
	// }

	/**
	 * 鐠囪锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟界粵锟�
	 **/
	private byte[] readInfo(byte[] buff, String tag) {
		int len = 0;
		int offset = ByteUtil.indexOf(tag.getBytes(), buff);
		len = buff[offset + 4] & 0xFF;
		len = (len << 8) + (buff[offset + 5] & 0xFF);
		len = (len << 8) + (buff[offset + 6] & 0xFF);
		len = (len << 8) + (buff[offset + 7] & 0xFF);
		len = len - 1;
		return ByteUtil.cutBytes(ByteUtil.indexOf(tag.getBytes(), buff) + 11, ByteUtil.indexOf(tag.getBytes(), buff) + 11 + len, buff);

	}

	public void setInfo(Id3v2Info info) {
		this.info = info;
	}

	public Id3v2Info getInfo() {
		return info;
	}

	public String getName() {
		return getInfo().getTit2();

	}

	public String getAuthor() {

		return getInfo().getTpe1();

	}

	public String getSpecial() {
		return getInfo().getTalb();
	}

	public byte[] getImg() {
		return getInfo().getApic();
	}

	public File getAudioFile() {
		return audioFile;
	}
}
