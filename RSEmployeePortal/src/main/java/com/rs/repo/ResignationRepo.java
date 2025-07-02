package com.rs.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rs.model.Employee;
import com.rs.model.Resignation;
@Repository
public interface ResignationRepo extends JpaRepository<Resignation,Long> {

	List<Resignation> findByDateOfApplyingAndStatusAndEmployee(String dateOfApplying, String status, Employee emp);

}
