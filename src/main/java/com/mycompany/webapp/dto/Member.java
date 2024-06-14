package com.mycompany.webapp.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Member {
	private String mid;
	private String mpassword;
	private String mname;
	private int mssn;
	private String msex;
	private String mphone;
	private String mbankName;
	private String mpayAccount;
	private byte[] mprofileData;
	private String mprofileType;
	private String mprofileName;
	private String mintroduce;
	private String mrole;
	private boolean menabled;
	private Date mdate;
}
