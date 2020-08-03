package com.xiaoshu.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class QueryVo extends Goods {

	 private String tTypename;
	 
	 @DateTimeFormat(pattern="yyyy-MM-dd")
	 private Date start;

	 @DateTimeFormat(pattern="yyyy-MM-dd")
	 private Date end;
	 
	public String gettTypename() {
		return tTypename;
	}

	public void settTypename(String tTypename) {
		this.tTypename = tTypename;
	}
}
