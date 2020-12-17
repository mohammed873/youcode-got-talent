package com.gotTalent.enums;

public enum EnumMail {
	MAILADR("projectmailtestyc@gmail.com"),
	MAILPSWRD("youcode2020"),
	MAILAUTH("mail.smtp.auth", "true"),
	MAILSTARTTLS("mail.smtp.starttls.enable", "true"),
	MAILHOST("mail.smtp.host", "smtp.gmail.com"),
	MAILPORT("mail.smtp.port", "587");
	
	
	private String label;
	private String label1;

	private EnumMail(String label) {
		this.label = label;
	}
	private EnumMail(String label,String label1) {
		this.label = label;
		this.label1 = label1;
	}

	public String getLabel1() {
		return label1;
	}
	
	public String getLabel() {
		return label;
	}
	
}
