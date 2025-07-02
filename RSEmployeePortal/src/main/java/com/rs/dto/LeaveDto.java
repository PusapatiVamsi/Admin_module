package com.rs.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
public class LeaveDto {
	private long id;
	private String startDate;
	private String endDate;
	private String mobileNo;
	private String reason;
	private String status;
	@JsonBackReference
	private EmpPostDto employee;

}
