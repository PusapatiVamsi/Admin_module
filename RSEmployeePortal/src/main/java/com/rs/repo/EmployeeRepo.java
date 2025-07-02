package com.rs.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rs.model.Employee;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
	
	Optional<Employee>findByEmailId(String email);
	Optional<Employee>findByMobileNo(String num);
	Optional<Employee> findByEmailIdOrMobileNo(String emailId, String mobNo);
	

}
