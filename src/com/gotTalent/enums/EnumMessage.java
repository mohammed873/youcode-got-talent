package com.gotTalent.enums;

public enum EnumMessage {
	//Error Messages
		ONLYDEGITID("Only degit numbers (9 degits)"),
		CATNOTFOUND("Category id not found, please select an available from the list."),
		DESCRIPTION("Description too short (need to be more than 14 characters)"),
		TIMEFORMAT("Invalid time format (yyy-mmm-dd h:m:s)"),
		USERNOTFOUND("User not found."),
		REGISTER("Please register"),
		EMAILVALID("Unvalid email format."),
		PHONEVALID("Unvalid Phone format (+212XXXXXXXXX)."),
		FNAMEVALID("First name is too short."),
		LNAMEVALID("Last name is too short."),
		WRONGCRID("Wrong Cridentials !"),
		EMAILNOTFOUND("User email not found."),
		PARTDENIED("Participation Denied"),
		CATCHOOSEN("You can't participate in the same category."),
	//Success Messages
		SIGNUP("Your inscription is done successfully, This is your id save it : "),
		UPDATE("Your informations are successfully updated"),
		CHECKEMAIL("Check your email for more information."),
		PARTACC("Participation Accepted"),
		EMAILSENT("Email sent sucessfully");
		private String label;

		private EnumMessage(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
		
}
