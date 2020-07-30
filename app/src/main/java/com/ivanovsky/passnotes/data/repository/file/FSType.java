package com.ivanovsky.passnotes.data.repository.file;

public enum FSType {
	REGULAR_FS("regular"),
	DROPBOX("dropbox");

	private final String textName;

	public static FSType fromTextName(String textName) {
		for (FSType type : FSType.values()) {
			if (type.textName.equals(textName)) {
				return type;
			}
		}

		return null;
	}

	FSType(String textName) {
	    this.textName = textName;
	}

	public String getTextName() {
		return textName;
	}
}
