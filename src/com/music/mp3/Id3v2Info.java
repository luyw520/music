package com.music.mp3;

import java.io.File;

public class Id3v2Info {

	private String tit2 = null; // title
	private String tpe1 = null; // ablume
	private String talb = null; // artist
	private byte[] apic = null; // image
	private String mDefult;
	File mFile;

	public Id3v2Info(String title, String album, String artist, byte[] mapic) {
		this.tit2 = title;
		this.tpe1 = album;
		this.talb = artist;
		this.apic = mapic;
	}

	public Id3v2Info(File audioFile, String tit2, String tpe1, String talb, byte[] apic, String defult) {
		setTit2(tit2);
		setTpe1(tpe1);
		setTalb(talb);
		setApic(apic);

		mFile = audioFile;
		mDefult = defult;
	}

	public void setTit2(String tit2) {
		this.tit2 = tit2;
	}

	public String getTit2() {
		return tit2;
	}

	public void setTpe1(String tpe1) {
		this.tpe1 = tpe1;
	}

	public String getTpe1() {
		return tpe1 == null ? mDefult : tpe1;
	}

	public void setTalb(String talb) {
		this.talb = talb;
	}

	public String getTalb() {
		return talb == null ? mDefult : talb;
	}

	public void setApic(byte[] apic) {
		this.apic = apic;
	}

	public byte[] getApic() {
		return apic;
	}

	public File getFile() {
		return mFile;
	}
}
