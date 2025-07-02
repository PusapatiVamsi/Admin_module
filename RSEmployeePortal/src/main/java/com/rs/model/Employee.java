 package com.rs.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="employee")
public class Employee {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String fullName;
	private String mobileNo;
	private String emailId;
	private String designation;
	private String empId;
	private String role;
	private String status;
	@OneToMany(mappedBy="employee",cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<LeaveRequest> leaveRequest;
	
	@OneToOne(mappedBy="employee",cascade = CascadeType.ALL)
	@JsonManagedReference
	private Resignation resignation;

}
