package egovframework.let.temp.service;

import java.io.Serializable;

public class TempVO implements Serializable {
	
	//임시데이터 ID
	private java.lang.String tempId;
	
	//임시데이터
	private String tempVal;

	public java.lang.String getTempId() {
		return tempId;
	}

	public void setTempId(java.lang.String tempId) {
		this.tempId = tempId;
	}

	public String getTempVal() {
		return tempVal;
	}

	public void setTempVal(String tempVal) {
		this.tempVal = tempVal;
	}
	
	
	
}
