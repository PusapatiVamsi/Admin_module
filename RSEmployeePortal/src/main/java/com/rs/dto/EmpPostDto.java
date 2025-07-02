package com.rs.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rs.model.Resignation;

import lombok.Data;

@Data
public class EmpPostDto {
	

	private long id;
	private String fullName;
	private String mobileNo;
	private String emailId;
	private String designation;
	private String empId;
	private String role;
	private String status;
	@JsonManagedReference
	private List<LeaveDto> leaveRequest;
//	@JsonManagedReference
	private ResignationDto resignation;
	
}
