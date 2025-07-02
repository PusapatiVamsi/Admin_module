package com.rs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rs.dto.ResignationDto;
import com.rs.service.ResignationService;

@RestController
@RequestMapping("/Resig")

public class ResignationController {
			@Autowired
		private ResignationService service;
		@PostMapping("/Add")
		public ResponseEntity<String> add(@RequestBody ResignationDto data){
			return service.add(data);
		}
		@GetMapping("/findall")
		public ResponseEntity<List<ResignationDto>>getAll(){
			return service.getAll();
		}
		@GetMapping("/GetById/{id}")
		public ResponseEntity<?>getById(@PathVariable long id){
			return service.getById(id);
		}
		@DeleteMapping("/delete/{id}")
		public ResponseEntity<?>deleteById(@PathVariable long id){
			return service.deleteById(id);
		}
		@PutMapping("/update/{id}")
		public ResponseEntity<String> updateResignation(@PathVariable long id, @RequestBody ResignationDto dto) {
		    return service.updateResignation(id, dto);
		}
		
}
