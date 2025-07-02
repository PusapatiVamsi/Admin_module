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

import com.rs.dto.ResignationDto;
import com.rs.exception.DuplicateFound;
import com.rs.exception.ResponseNotFound;
import com.rs.model.Employee;
import com.rs.model.Resignation;
import com.rs.repo.EmployeeRepo;
import com.rs.repo.ResignationRepo;

import jakarta.transaction.Transactional;

@Service
public class ResignationImpService implements ResignationService{
	
	@Autowired
	private EmployeeRepo repo;
	@Autowired
	private ResignationRepo res;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseEntity<String> add(ResignationDto data) {
		// finding emp by mobileno
		String status=data.getStatus();
		Employee emp = repo.findByMobileNo(data.getMobileNo())
				.orElseThrow(()-> new ResponseNotFound("Employee not found for mobile number: "+ data.getMobileNo()));
		
		//chaging format of ApplyingDate
			DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			 LocalDate dateOfApplying = LocalDate.parse(data.getDateOfApplying(), format);
		
		        List<Resignation> existingResignation = res.findByDateOfApplyingAndStatusAndEmployee(data.getDateOfApplying(),status,emp);
		        if (!existingResignation.isEmpty()) {
		            throw new DuplicateFound("Resignation already applied for the given end date: " + data.getDateOfApplying());
		        }

		        //  Map DTO to entity 
		        Resignation resignation = new Resignation();
		        resignation.setEmployee(emp);
		        resignation.setDateOfApplying(data.getDateOfApplying());
		        resignation.setReason(data.getReason());
		        resignation.setStatus(status);
		        res.save(resignation);
		        
		        return ResponseEntity.status(HttpStatus.OK).body("Resignation submitted successfully");
		    }

	@Override
	public ResponseEntity<List<ResignationDto>> getAll() {
		List<Resignation>resigData=res.findAll();
		List<ResignationDto> resDto=resigData.stream()
				.map(d->modelMapper.map(d,ResignationDto.class))
				.collect(Collectors.toList());
				return ResponseEntity.status(HttpStatus.OK).body(resDto);

	}
	@Override
	public ResponseEntity<?> getById(long id) {
		Optional<Resignation> existingData=res.findById(id);
		if(!existingData.isPresent()) {
			throw new ResponseNotFound("Give valid input");
		}
		return ResponseEntity.status(HttpStatus.OK).body(existingData);
	}
	
//	@Transactional
//	@Override
//	public ResponseEntity<?> deleteById(long id) {
//		Optional<Resignation> existingData=res.findById(id);
//		if(!existingData.isPresent()) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data not Exist");
//		}else {
//			res.delete(existingData.get());
//			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("The data was deleted");
//		}
//
//}
	
	@Transactional
	@Override
	public ResponseEntity<?> deleteById(long id) {
	    Resignation resignation = res.findById(id)
	            .orElseThrow(() -> new ResponseNotFound("Resignation with ID " + id + " not found"));

	    resignation.setEmployee(null); 
	    res.delete(resignation);

	    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Resignation deleted successfully");
	}


	@Override
	public ResponseEntity<String> updateResignation(long id, ResignationDto dto) {
		// Find leave request by ID
	    Resignation existingData = res.findById(id)
	            .orElseThrow(() -> new ResponseNotFound("Resignation request with ID " + id + " not found."));

	    // Check status is "request" or "pending"
	    String currentStatus = existingData.getStatus();
	    if (!currentStatus.equalsIgnoreCase("request") && !currentStatus.equalsIgnoreCase("pending")) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot update Resignation with status: " + currentStatus);
	    }

	    // Compare new start date with old start date
	    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    LocalDate oldStartDate = LocalDate.parse(existingData.getDateOfApplying(), format);
	    LocalDate newStartDate = LocalDate.parse(dto.getDateOfApplying(), format);

	    if (!newStartDate.isAfter(oldStartDate)) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("New start date must be after existing End date.");
	    }

	    // Update fields
	    existingData.setDateOfApplying(dto.getDateOfApplying());
	    existingData.setReason(dto.getReason());
	    existingData.setStatus(dto.getStatus());

	    res.save(existingData);

	    return ResponseEntity.status(HttpStatus.OK).body("Resignation updated successfully.");
	}
	
}
