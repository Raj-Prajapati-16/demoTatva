package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeFinacialReportDto {

	private Long empId;
	private String empFName;
	private String empLName;
	private String totalAmountPaid;
}
