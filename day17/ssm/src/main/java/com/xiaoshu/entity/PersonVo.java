package com.xiaoshu.entity;

import java.util.Date;

import javax.persistence.Column;

import org.springframework.format.annotation.DateTimeFormat;

public class PersonVo extends Person {

	@Column(name = "c_name")
    private String cName;

	@DateTimeFormat(pattern="yyy-MM-dd")
	private Date start;

	@DateTimeFormat(pattern="yyy-MM-dd")
	private Date end;
	
	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}
	
}
