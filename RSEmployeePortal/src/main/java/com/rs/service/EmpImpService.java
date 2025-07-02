package com.rs.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rs.dto.EmpPostDto;
import com.rs.exception.DuplicateFound;
import com.rs.exception.ResponseNotFound;
import com.rs.model.Employee;
import com.rs.repo.EmployeeRepo;


@Service
public class EmpImpService implements EmpService {
	
	@Autowired
	EmployeeRepo repo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseEntity<String> save(EmpPostDto data) {
		Optional<Employee> existingData= repo.findByEmailId(data.getEmailId());
		Optional<Employee> existingNum=repo.findByMobileNo(data.getMobileNo());
		if(existingData.isPresent() || existingNum.isPresent()) {
			throw new DuplicateFound("Data already exist");
		}
		repo.save(modelMapper.map(data, Employee.class));
		
		return ResponseEntity.status(HttpStatus.OK).body("Data was created");
	}

	@Override
	public ResponseEntity<List<EmpPostDto>> getAll() {
		List<Employee> empData=repo.findAll();
		List<EmpPostDto> EmpDto= empData.stream()
				.map(d->modelMapper.map(d,EmpPostDto.class))
				.collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(EmpDto);
	}

	@Override
	public ResponseEntity<?> getById(long id) {
		Optional<Employee> existingData=repo.findById(id);
		if(!existingData.isPresent()) {
			throw new ResponseNotFound("Give valid input");
		}
		return ResponseEntity.status(HttpStatus.OK).body(existingData);
	}

	@Override
	public ResponseEntity<?> deleteById(long id) {
		Optional<Employee> existingData=repo.findById(id);
		if(!existingData.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data not Exist");
		}else {
			repo.delete(existingData.get());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("The data was deleted");
		}
	}	

	@Override
	public ResponseEntity<?> updateById(long id, EmpPostDto data) {
		 Optional<Employee> existingData = repo.findById(id);
		 String status=data.getStatus();

		    if (!existingData.isPresent()) {
		        throw new ResponseNotFound("Employee with ID " + id + " not found.");
		    }else {

		    Employee existingUser = existingData.get();
		    // dto data
		    String newMobileNo = data.getMobileNo();
		    String newEmailId = data.getEmailId();
		    
		    // database data
		    String mobNo = existingUser.getMobileNo();
		    String emailId = existingUser.getEmailId();
		    // setting data for non checks
		    existingUser.setDesignation(data.getDesignation());
	        existingUser.setFullName(data.getFullName());
	        // checking the mobile Number and Email data for duplicates
	        if(mobNo.equalsIgnoreCase(newMobileNo)) {
	        	existingUser.setMobileNo(mobNo);
	        }else {
	        	// get the data if duplicates here MobNo
	        	Optional<Employee> duplicateForMobileNo = repo.findByMobileNo(newMobileNo);
	        	if(duplicateForMobileNo.isPresent()) {
	        		throw new DuplicateFound("Employee with Mobile Number is Exists");
	        	}else {
	        		existingUser.setMobileNo(newMobileNo);
	        	}
	        }
	        
	        if(emailId.equalsIgnoreCase(newEmailId)) {
	        	existingUser.setEmailId(newEmailId);
	        }else {
	        	// get the data if duplicates here for EmailId
	        	Optional<Employee> duplicateForEmail = repo.findByEmailId(newEmailId);
	        	if(duplicateForEmail.isPresent()) {
	        		throw new DuplicateFound("Employee with Email is Exists");
	        	}else {
	        		existingUser.setEmailId(newEmailId);
	        	}
	        }
	        repo.save(existingUser);
	        return ResponseEntity.status(HttpStatus.OK).body("The data was Updated Successfully");
			
	       	}
			
		 }
	}

//	@Override
//	public ResponseEntity<?> updateById(long id) {
//		Optional<Employee> existingData=repo.findById(id);
//		if(!existingData.isPresent()) {
//			throw new ResponseNotFound("Give valid input");
//		}
//		Optional<Employee> existData= repo.findByEmailId(data.getEmailId());
//	}

