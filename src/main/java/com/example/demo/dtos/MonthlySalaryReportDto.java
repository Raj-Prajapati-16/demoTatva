package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MonthlySalaryReportDto {
	Integer month;
	String totalSalary;
	Long totalEmployee;
}
