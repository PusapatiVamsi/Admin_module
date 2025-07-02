package com.rs.service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rs.dto.LeaveDto;
import com.rs.exception.DuplicateFound;
import com.rs.exception.ResponseNotFound;
import com.rs.model.Employee;
import com.rs.model.LeaveRequest;
import com.rs.repo.EmployeeRepo;
import com.rs.repo.LeaveRepo;

@Service
public class LeaveReqImp implements LeaveRegService {
	
	@Autowired
	private LeaveRepo rep;
	@Autowired
	private EmployeeRepo repo;
	
	@Autowired
	private ModelMapper modelMapper;
	

	@Override
	public ResponseEntity<String> post(LeaveDto data) {
		// finding emp by mobileno
		String status=data.getStatus();
		Employee emp = repo.findByMobileNo(data.getMobileNo())
				.orElseThrow(()-> new ResponseNotFound("Employee not found for mobile number: "+ data.getMobileNo()));
		
		//chaging format of start and end date
			DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			 LocalDate startDate = LocalDate.parse(data.getStartDate(), format);
		        LocalDate endDate = LocalDate.parse(data.getEndDate(), format);
		
		        List<LeaveRequest> existingLeaves = rep.findByEndDateAndStatusAndEmployee(data.getEndDate(),status,emp);
		        if (!existingLeaves.isEmpty()) {
		            throw new DuplicateFound("Leave already applied for the given end date: " + data.getEndDate());
		        }

		        //  Map DTO to entity 
		        LeaveRequest leaveRequest = new LeaveRequest();
		        leaveRequest.setEmployee(emp);
		        leaveRequest.setStartDate(data.getStartDate());
		        leaveRequest.setEndDate(data.getEndDate());
		        leaveRequest.setReason(data.getReason());
		        leaveRequest.setStatus(status);
		        rep.save(leaveRequest);
		        
		        return ResponseEntity.status(HttpStatus.OK).body("Leave request submitted successfully");
		    }
	@Override
	public ResponseEntity<List<LeaveDto>> getAll() {
		List<LeaveRequest> leaveData=rep.findAll();
		List<LeaveDto> leaveDto= leaveData.stream()
				.map(d->modelMapper.map(d,LeaveDto.class))
				.collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(leaveDto);
	}

	@Override
	public ResponseEntity<?> getById(long id) {
		Optional<LeaveRequest> existingData=rep.findById(id);
		if(!existingData.isPresent()) {
			throw new ResponseNotFound("Give valid input");
		}
		return ResponseEntity.status(HttpStatus.OK).body(existingData);
	}

	@Override
	public ResponseEntity<?> deleteById(long id) {
		Optional<LeaveRequest> existingData=rep.findById(id);
		if(!existingData.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data not Exist");
		}else {
			rep.delete(existingData.get());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("The data was deleted");
		}
	}
	@Override
	public ResponseEntity<?> delete(long id) {
		LeaveRequest data=rep.findById(id)
				.orElseThrow(()-> new ResponseNotFound("Employee with id: "+id + "not found"));
		String status = data.getStatus();
		if(status.equalsIgnoreCase("request") || status.equalsIgnoreCase("pending")) {
			rep.delete(data);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Employee no content with id :" +id);
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You can't delete leave for id: "+ id);
		}
		
	}
	@Override
	public ResponseEntity<String> updateLeave(long id, LeaveDto dto) {
		  // Find leave request by ID
	    LeaveRequest existingLeave = rep.findById(id)
	            .orElseThrow(() -> new ResponseNotFound("Leave request with ID " + id + " not found."));

	    // Check status is "request" or "pending"
	    String currentStatus = existingLeave.getStatus();
	    if (!currentStatus.equalsIgnoreCase("request") && !currentStatus.equalsIgnoreCase("pending")) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot update leave with status: " + currentStatus);
	    }

	    // Compare new start date with old start date
	    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    LocalDate oldStartDate = LocalDate.parse(existingLeave.getStartDate(), format);
	    LocalDate newStartDate = LocalDate.parse(dto.getEndDate(), format);

	    if (!newStartDate.isAfter(oldStartDate)) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("New start date must be after existing End date.");
	    }

	    // Update fields
	    existingLeave.setStartDate(dto.getStartDate());
	    existingLeave.setEndDate(dto.getEndDate());
	    existingLeave.setReason(dto.getReason());
	    existingLeave.setStatus(dto.getStatus());

	    rep.save(existingLeave);

	    return ResponseEntity.status(HttpStatus.OK).body("Leave request updated successfully.");
	}
}
	

//	@Override
//	public ResponseEntity<?> updateById(long id, EmpPostDto data) {
//		 Optional<Employee> existingData = repo.findById(id);
//
//		    if (!existingData.isPresent()) {
//		        throw new ResponseNotFound("Employee with ID " + id + " not found.");
//		    }else {
//
//		    Employee existingUser = existingData.get();
//		    // dto data
//		    String newMobileNo = data.getMobileNo();
//		    String newEmailId = data.getEmailId();
//		    
//		    // database data
//		    String mobNo = existingUser.getMobileNo();
//		    String emailId = existingUser.getEmailId();
//		    // setting data for non checks
//		    existingUser.setDesignation(data.getDesignation());
//	        existingUser.setFullName(data.getFullName());
//	        // checking the mobile Number and Email data for duplicates
//	        if(mobNo.equalsIgnoreCase(newMobileNo)) {
//	        	existingUser.setMobileNo(mobNo);
//	        }else {
//	        	// get the data if duplicates here MobNo
//	        	Optional<Employee> duplicateForMobileNo = repo.findByMobileNo(newMobileNo);
//	        	if(duplicateForMobileNo.isPresent()) {
//	        		throw new DuplicateFound("Employee with Mobile Number is Exists");
//	        	}else {
//	        		existingUser.setMobileNo(newMobileNo);
//	        	}
//	        }
//	        
//	        if(emailId.equalsIgnoreCase(newEmailId)) {
//	        	existingUser.setEmailId(newEmailId);
//	        }else {
//	        	// get the data if duplicates here for EmailId
//	        	Optional<Employee> duplicateForEmail = repo.findByEmailId(newEmailId);
//	        	if(duplicateForEmail.isPresent()) {
//	        		throw new DuplicateFound("Employee with Email is Exists");
//	        	}else {
//	        		existingUser.setEmailId(newEmailId);
//	        	}
//	        }
//	        repo.save(existingUser);
//	        return ResponseEntity.status(HttpStatus.OK).body("The data was Updated Successfully");
//			
//	       	}
//			
//		 }
//	}



