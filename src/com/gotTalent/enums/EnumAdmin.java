package com.gotTalent.enums;

public enum EnumAdmin {
	ADMINEMAIL("ahmed.mahmoud.admin@gmail.com"),
	ADMINPSWRD("0000");
	
	
	private String label;

	private EnumAdmin(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
	
}
