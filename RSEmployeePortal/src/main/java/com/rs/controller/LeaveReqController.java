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

import com.rs.dto.LeaveDto;
import com.rs.service.LeaveRegService;

@RestController
@RequestMapping("/LeaveRequest")
public class LeaveReqController {
	
	@Autowired
	private LeaveRegService service;
	
	@PostMapping("/add")
	public ResponseEntity<String> post(@RequestBody LeaveDto data){
		return service.post(data);
	}
	
	@GetMapping("/fetch")
	public ResponseEntity<List<LeaveDto>> getAll(){
		return service.getAll();
	}
	
	@GetMapping("/GetById/{id}")
	public ResponseEntity<?>getById(@PathVariable long id){
		return service.getById(id);
	}
	
	@DeleteMapping("/Delete/{id}")
	public ResponseEntity<?>deleteById(@PathVariable long id){
		return service.deleteById(id);
	}
	
	@DeleteMapping("/Erase/{id}")
	public ResponseEntity<?>delete(@PathVariable long id){
		return service.delete(id);
	}
	
	@PutMapping("/LeaveUpdate/{id}")
	public ResponseEntity<String> updateLeave(@PathVariable long id, @RequestBody LeaveDto dto) {
	    return service.updateLeave(id, dto);
	}


}
