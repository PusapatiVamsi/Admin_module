package com.rs.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="resignation")
public class Resignation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String dateOfApplying;
	private String reason;
	private String status;
	
	@OneToOne
	@JoinColumn(name="employee_id",referencedColumnName = "id")
	@JsonBackReference
	private Employee employee;

}
