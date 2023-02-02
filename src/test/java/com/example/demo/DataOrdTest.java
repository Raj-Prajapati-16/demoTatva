package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.dtos.EmployeeFinacialReportDto;
import com.example.demo.dtos.MonthlyAmountReleasedDto;
import com.example.demo.dtos.MonthlySalaryReportDto;
import com.example.demo.entity.EmployeeRecord;
import com.example.demo.repository.EmployeeReportRepo;

@SpringBootTest
public class DataOrdTest {
	
	@Autowired
	private EmployeeReportRepo employeeReportRepo;

	@BeforeEach
	void tearDown() {
		System.out.println("Testing Started");
	}
	@Test
	@Order(1)
	void countTotalEmployeesTest() {
		int countTotalEmployees = employeeReportRepo.getToatalEmployeeCount();
		assertThat(countTotalEmployees).isPositive();
	}
	@Test
	@Order(2)
	void getEmployeeByJoingMonthTest() {
		List<EmployeeRecord> countTotalEmployees = employeeReportRepo.getEmployeeByJoingMonth(LocalDate.of(2023, 02, 1),LocalDate.of(2023, 02, 28));
		assertThat(countTotalEmployees).isNotNull();
	}
	@Test
	@Order(3)
	void getEmployeeByExitMonthTest() {
		List<EmployeeRecord> countTotalEmployees = employeeReportRepo.getEmployeeByJoingMonth(LocalDate.of(2023, 02, 1),LocalDate.of(2023, 02, 28));
		assertThat(countTotalEmployees).isNotNull();
	}
	@Test
	@Order(4)
	void getYearlyReportTest() {
		List<EmployeeRecord> testResult = employeeReportRepo.getEmployeeByExitMonth(LocalDate.of(2022, 01, 01),LocalDate.of(2022, 12, 31));
		assertThat(testResult).isNotNull();
	}
	@Test
	@Order(4)
	void getMonthlySalaryReportTest() {
		List<MonthlySalaryReportDto> testResult = employeeReportRepo.getMonthlySalaryReport
				(Date.valueOf(LocalDate.of(2022, 11, 01)),Date.valueOf(LocalDate.of(2022, 11, 30)));
		assertThat(testResult).isNotNull();
	}
	@Test
	@Order(4)
	void getEmployeeFinancialReportTest() {
		List<EmployeeFinacialReportDto> countTotalEmployees = employeeReportRepo.getEmployeeFinancialReport
				(Date.valueOf(LocalDate.of(2022, 04, 01)),Date.valueOf(LocalDate.of(2023, 03, 31)));
		assertThat(countTotalEmployees).isNotNull();
	}
	@Test
	@Order(4)
	void getMonthlyAmountReleasedReportTest() {
		List<MonthlyAmountReleasedDto> countTotalEmployees = employeeReportRepo.getMonthlyAmountReleasedReport
				(Date.valueOf(LocalDate.of(2022, 11, 01)),Date.valueOf(LocalDate.of(2023, 11, 30)));
		assertThat(countTotalEmployees).isNotNull();
	}
	@AfterEach
	void setup() {
		System.out.println("Testeing Completed");
	}
	
}
