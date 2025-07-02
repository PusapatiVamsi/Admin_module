package com.rs.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.rs.dto.ResignationDto;

public interface ResignationService {

	ResponseEntity<String> add(ResignationDto data);

	ResponseEntity<List<ResignationDto>> getAll();

	ResponseEntity<?> getById(long id);

	ResponseEntity<?> deleteById(long id);

	ResponseEntity<String> updateResignation(long id, ResignationDto dto);

}
