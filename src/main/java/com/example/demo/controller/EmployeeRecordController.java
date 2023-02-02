package com.example.demo.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

import com.example.demo.entity.EmployeeRecord;
import com.example.demo.service.EmployeeReportService;
import com.example.demo.utils.CsvUtils;

import lombok.RequiredArgsConstructor;

/**
 * @author Raj.Prajapati
 * @since 02-02-2023
 */
@RestController
@RequiredArgsConstructor
public class EmployeeRecordController {

	private final CsvUtils csvUtils;
	private final EmployeeReportService employeeReportService;
	
	@PostMapping("/upload-csv")
	public ResponseEntity<String> saveAllEmployeeRecords(@RequestParam("file") MultipartFile file){
		if(csvUtils.hasTypeCsv(file)) {
			List<EmployeeRecord> employeeRecords =csvUtils.readReportFromCsv(file);
			employeeReportService.saveAll(employeeRecords);
			return ResponseEntity.ok("Records Saved Successfully");
		}
		return new ResponseEntity<String>("file type is not csv/text",HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/emp-count")
	public ResponseEntity<Integer> getToatlEmpCount(){
		return ResponseEntity.ok(employeeReportService.getTotalEmpCount());
	}
	
	@GetMapping("/download/emp-joined")
	public ResponseEntity<InputStreamResource> getJoinedEmployee(
			@RequestParam(value="month", required = true)  Integer month,
			@RequestParam(value="year", required = false)  Integer year) {
		if(year==null || year==0)
			year = Year.now().getValue();
		 LocalDate monthBegin = LocalDate.of(year, month,01);
		 LocalDate monthEnd = monthBegin.withDayOfMonth(
				 monthBegin.getMonth().length(monthBegin.isLeapYear()));

		return employeeReportService.getEmployeeJoinedByMonth(monthBegin,monthEnd);
	}
	@GetMapping("/download/emp-resigned")
	public ResponseEntity<InputStreamResource> getResignedEmployee(
			@RequestParam(value="month", required = false)  Integer month,
			@RequestParam(value="year", required = false)  Integer year) {
		if(year==null || year==0)
			year = Year.now().getValue();
		 LocalDate monthBegin = LocalDate.of(year, month,01);
		 LocalDate monthEnd = monthBegin.withDayOfMonth(
				 monthBegin.getMonth().length(monthBegin.isLeapYear()));
		return employeeReportService.getEmployeeExitByMonth(monthBegin,monthEnd);
	}
	
	@GetMapping("/download/monthly-salary-report")
	public ResponseEntity<InputStreamResource> getMonthlySalaryReport(
			@RequestParam(value="month", required = false, defaultValue = "0")  Integer month,
			@RequestParam(value="year", required = false, defaultValue = "0")  Integer year){
		if(year==null || year==0)
			year = Year.now().getValue();
		if(month==null || month==0)
			month = LocalDate.now().getMonthValue();
		 LocalDate dateBegin = LocalDate.of(year,month,01);
		 LocalDate dateEnd = dateBegin.withDayOfMonth(
				 dateBegin.getMonth().length(dateBegin.isLeapYear()));
		 return employeeReportService.monthlySalaryReport(dateBegin,dateEnd);
	}
	@GetMapping("/download/employee-report")
	public ResponseEntity<InputStreamResource> getEmployeeReport(@RequestParam(value="year", required = false, defaultValue = "0")  Integer year){
		if(year==null || year==0)
			year = Year.now().getValue();
		 LocalDate dateBegin = LocalDate.of(year,04,01);
		 LocalDate dateEnd = LocalDate.of(year+1,03,31);
		 return employeeReportService.employeeFinancialReport(dateBegin,dateEnd.isBefore(LocalDate.now())?dateEnd:LocalDate.now());
	}
	@GetMapping("/download/monthly-financial-report")
	public ResponseEntity<InputStreamResource> getMonthlyFinacialReport(
			@RequestParam(value="month", required = false)  Integer month,
			@RequestParam(value="year", required = false)  Integer year) {
		if(year==null || year==0)
			year = Year.now().getValue();
		 LocalDate monthBegin = LocalDate.of(year, month,01);
		 LocalDate monthEnd = monthBegin.withDayOfMonth(
				 monthBegin.getMonth().length(monthBegin.isLeapYear()));
		return employeeReportService.getMonthlyFinacialReport(monthBegin,monthEnd);
	}
	@GetMapping("/download/yearly-report")
	public ResponseEntity<InputStreamResource> getYearlyReport(@RequestParam(value="year", required = false, defaultValue = "0")  Integer year){
		if(year==null || year==0)
			year = Year.now().getValue();
		 LocalDate dateBegin = LocalDate.of(year,01,01);
		 LocalDate dateEnd = LocalDate.of(year,12,31);
		 return employeeReportService.getYearlyReport(dateBegin,dateEnd);
	}
}
