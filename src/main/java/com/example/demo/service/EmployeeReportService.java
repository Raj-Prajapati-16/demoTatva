package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import com.example.demo.entity.EmployeeRecord;

public interface EmployeeReportService {

	public void saveAll(List<EmployeeRecord> employeeRecords);

	public Integer getTotalEmpCount();

	public ResponseEntity<InputStreamResource> getEmployeeJoinedByMonth(LocalDate monthBegin, LocalDate monthEnd);

	ResponseEntity<InputStreamResource> getEmployeeExitByMonth(LocalDate monthBegin, LocalDate monthEnd);

	public ResponseEntity<InputStreamResource> getYearlyReport(LocalDate dateBegin, LocalDate dateEnd);

	ResponseEntity<InputStreamResource> monthlySalaryReport(LocalDate dateBegin, LocalDate dateEnd);

	public ResponseEntity<InputStreamResource> employeeFinancialReport(LocalDate dateBegin, LocalDate dateEnd);

	public ResponseEntity<InputStreamResource> getMonthlyFinacialReport(LocalDate monthBegin, LocalDate monthEnd);
}
