package com.rs.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
public class ResignationDto {
	private long id;
	private String mobileNo;
	private String dateOfApplying;
	private String reason;
	private String status;
	@JsonBackReference
	private EmpPostDto employee;

}
