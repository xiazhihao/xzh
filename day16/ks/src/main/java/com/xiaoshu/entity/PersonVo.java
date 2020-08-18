package com.xiaoshu.entity;

import java.util.Date;

import javax.persistence.Column;

import org.springframework.format.annotation.DateTimeFormat;

public class PersonVo extends Person{
	@Column(name = "b_name")
    private String bName;
	
	private Integer num;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date start;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date end;
	
	
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

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

	public String getbName() {
		return bName;
	}

	public void setbName(String bName) {
		this.bName = bName;
	}
}
