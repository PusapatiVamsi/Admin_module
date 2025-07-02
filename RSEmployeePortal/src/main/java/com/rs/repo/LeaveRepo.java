package com.rs.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rs.model.Employee;
import com.rs.model.LeaveRequest;

@Repository
public interface LeaveRepo extends JpaRepository<LeaveRequest, Long> {

	List<LeaveRequest> findByEndDateAndStatusAndEmployee(String endDate, String status, Employee emp);

}
