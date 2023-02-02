package com.example.demo.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRecord {

	@Id
	@Column(name = "sequence_no", unique = true)
	private Long id;
	
	private Long empId;
	private String empFName;
	private String empLName;
	private String designation;
	private String event;
	private String value;
	private Date eventDate;
	private String notes;
	
}
