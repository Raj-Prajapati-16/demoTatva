package com.example.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.EmployeeDto;
import com.example.demo.dtos.EmployeeFinacialReportDto;
import com.example.demo.dtos.MonthlyAmountReleasedDto;
import com.example.demo.dtos.MonthlySalaryReportDto;
import com.example.demo.entity.EmployeeRecord;
import com.example.demo.repository.EmployeeReportRepo;
import com.example.demo.utils.CsvUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Raj.Prajapati
 * @since 02-02-2023
 */
@Service
@RequiredArgsConstructor
public class EmployeeReportServiceImpl implements EmployeeReportService{

	private final EmployeeReportRepo employeeReportRepo;
	private final CsvUtils csvUtils;
	
	@Override
	public void saveAll(List<EmployeeRecord> employeeRecords) {
		employeeReportRepo.saveAll(employeeRecords);
	}

	@Override
	public Integer getTotalEmpCount() {
		return employeeReportRepo.getToatalEmployeeCount();
	}
	@Override
	public ResponseEntity<InputStreamResource> employeeFinancialReport(LocalDate dateBegin, LocalDate dateEnd) {
		List<List<String>> csvDataList = new ArrayList<>();
		List<String> headers = Arrays.asList("EmployeeId","FirstName","LastName", "Total_amount_paid");
		List<EmployeeFinacialReportDto> finacialReportDtos =employeeReportRepo.getEmployeeFinancialReport(java.sql.Date.valueOf(dateBegin),java.sql.Date.valueOf(dateEnd));
		if(!finacialReportDtos.isEmpty()) {
			finacialReportDtos.forEach((dto)->{
				List<String> record = new ArrayList<>();
				record.add(dto.getEmpId().toString());
				record.add(dto.getEmpFName());
				record.add(dto.getEmpLName());
				record.add(dto.getTotalAmountPaid());
				csvDataList.add(record);
			});
		}
		return csvUtils.downloadToCsv(headers, csvDataList, "Employee_Financial_Yealy_Report");
	}

	@Override
	public ResponseEntity<InputStreamResource> getEmployeeJoinedByMonth(LocalDate monthBegin, LocalDate monthEnd) {
		List<String> headers =Arrays.asList("EmployeeId","FirstName","LastName","Designation");
		List<EmployeeRecord> employeeRecords = employeeReportRepo.getEmployeeByJoingMonth(monthBegin,monthEnd);
		List<List<String>> csvDataList = new ArrayList<>();
		csvDataList= employeeRecords.stream().map((employeeRecord)->convertToList(employeeRecord,headers)).collect(Collectors.toList());
		return csvUtils.downloadToCsv(headers, csvDataList, "Month_wise_new_joined_employeelist");
	}
	@Override
	public ResponseEntity<InputStreamResource> getEmployeeExitByMonth(LocalDate monthBegin, LocalDate monthEnd) {
		List<String> headers =Arrays.asList("FirstName","LastName");
		List<EmployeeRecord> employeeRecords = employeeReportRepo.getEmployeeByExitMonth(monthBegin,monthEnd);
		List<List<String>> csvDataList = new ArrayList<>();
		csvDataList= employeeRecords.stream().map((employeeRecord)->convertToList(employeeRecord,headers)).collect(Collectors.toList());
		return csvUtils.downloadToCsv(headers, csvDataList, "Month_wise_new_Resigned_employeelist");
	}
	/**
     * This method converts object parameters into list of string in order.
     *
     * @param employeeRecord
     * @param headers
     * @see EmployeeRecord.class
     * @return List<String>
     * @author raj.prajapati
     * @since 02-02-2023
     */
	private List<String> convertToList(EmployeeRecord employeeRecord, List<String> headers) {
		List<String> csvRecord = new ArrayList<>();
		if(headers.contains("SequenceNo"))
			csvRecord.add(employeeRecord.getId().toString());
		if(headers.contains("EmployeeId"))
			csvRecord.add(employeeRecord.getEmpId().toString());
		if(headers.contains("FirstName"))
			csvRecord.add(employeeRecord.getEmpFName());
		if(headers.contains("LastName"))
			csvRecord.add(employeeRecord.getEmpLName());
		if(headers.contains("Designation"))
			csvRecord.add(employeeRecord.getDesignation());
		if(headers.contains("Event"))
			csvRecord.add(employeeRecord.getEvent());
		if(headers.contains("Notes"))
			csvRecord.add(employeeRecord.getNotes());
		if(headers.contains("EventDate"))
			csvRecord.add(employeeRecord.getEventDate().toString());
		if(headers.contains("EventValue"))
			csvRecord.add(employeeRecord.getValue());
		return csvRecord;
	}

	@Override
	public ResponseEntity<InputStreamResource> getYearlyReport(LocalDate dateBegin, LocalDate dateEnd) {
		List<List<String>> csvDataList = new ArrayList<>();
		List<String> headers =Arrays.asList("EmployeeId","Event","EventDate","EventValue");
		List<EmployeeRecord> employeeRecords = employeeReportRepo.getYearlyReport(java.sql.Date.valueOf(dateBegin),java.sql.Date.valueOf(dateEnd));
		csvDataList= employeeRecords.stream().map((employeeRecord)->convertToList(employeeRecord,headers)).collect(Collectors.toList());
		return csvUtils.downloadToCsv(headers, csvDataList, "Yearly_Report");
	}
	
	@Override
	public ResponseEntity<InputStreamResource> monthlySalaryReport(LocalDate dateBegin, LocalDate dateEnd) {
		List<List<String>> csvDataList = new ArrayList<>();
		List<String> headers =Arrays.asList("Month","Total Salary","TotalEmployee");
		List<MonthlySalaryReportDto> monthlySalaryReportDtos= employeeReportRepo.getMonthlySalaryReport(java.sql.Date.valueOf(dateBegin),java.sql.Date.valueOf(dateEnd));
		if(!monthlySalaryReportDtos.isEmpty() && monthlySalaryReportDtos.get(0).getMonth()!=null) {
		monthlySalaryReportDtos.forEach((dto)->{
			List<String> record = new ArrayList<>();
			record.add(dto.getMonth().toString());
			record.add(dto.getTotalSalary());
			record.add(dto.getTotalEmployee().toString());
			csvDataList.add(record);
		});
		}
		return csvUtils.downloadToCsv(headers, csvDataList, "monthly_salary_report");
	}

	@Override
	public ResponseEntity<InputStreamResource> getMonthlyFinacialReport(LocalDate monthBegin, LocalDate monthEnd) {
		List<List<String>> csvDataList = new ArrayList<>();
		List<String> headers =Arrays.asList("Month","Total Amount","TotalEmployee");
		List<MonthlyAmountReleasedDto> amountReleasedDtos = employeeReportRepo.getMonthlyAmountReleasedReport(java.sql.Date.valueOf(monthBegin),java.sql.Date.valueOf(monthEnd));
		if(!amountReleasedDtos.isEmpty() && amountReleasedDtos.get(0).getTotalAmount()!=null) {
			amountReleasedDtos.forEach((dto)->{
				List<String> record = new ArrayList<>();
				record.add(dto.getMonth().toString());
				record.add(dto.getTotalAmount());
				record.add(dto.getTotalEmployees().toString());
				csvDataList.add(record);
			});
		}
		return csvUtils.downloadToCsv(headers, csvDataList, "monthly_finacial_report");
	}

	
}
