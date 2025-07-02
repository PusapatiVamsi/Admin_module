package com.rs.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.rs.dto.EmpPostDto;

public interface EmpService {

	public ResponseEntity<String> save(EmpPostDto data);

	public ResponseEntity<List<EmpPostDto>> getAll();

	public ResponseEntity<?> getById(long id);

	public ResponseEntity<?> deleteById(long id);


	public ResponseEntity<?> updateById(long id, EmpPostDto data);
	


}
