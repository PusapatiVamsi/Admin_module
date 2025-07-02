package com.rs.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.rs.dto.EmpPostDto;
import com.rs.dto.LeaveDto;

public interface LeaveRegService {

	ResponseEntity<String> post(LeaveDto data);

	ResponseEntity<List<LeaveDto>> getAll();

	ResponseEntity<?> getById(long id);

	ResponseEntity<?> deleteById(long id);

	ResponseEntity<?> delete(long id);

	ResponseEntity<String> updateLeave(long id, LeaveDto dto);

	

}
