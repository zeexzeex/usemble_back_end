package com.mycompany.webapp.dto;

import lombok.Data;

@Data
public class SocialImg {
	private int sno;
	private String sdetailImgName;
	private String sdetailImgType;
	private byte[] sdetailImgData;
}
