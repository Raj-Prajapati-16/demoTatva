package com.example.demo.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.dtos.EmployeeFinacialReportDto;
import com.example.demo.dtos.MonthlyAmountReleasedDto;
import com.example.demo.dtos.MonthlySalaryReportDto;
import com.example.demo.entity.EmployeeRecord;

/**
* @author Raj.Prajapati
* @since 02-02-2023
*/
@Repository
public interface EmployeeReportRepo extends JpaRepository<EmployeeRecord, Long> {

	@Query("select count(distinct(empId)) from EmployeeRecord")
	Integer getToatalEmployeeCount();

	@Query("select employeeRecord "
			+ "from EmployeeRecord employeeRecord "
			+ "where event = 'ONBORD' and str_to_date(value,'%d-%m-%Y') between :startDate and :endDate "
			+ "group by emp_id  order by emp_id")
	List<EmployeeRecord> getEmployeeByJoingMonth(@Param("startDate")LocalDate monthBegin,@Param("endDate") LocalDate monthEnd);
	
	@Query("select employeeRecord "
			+ "from EmployeeRecord employeeRecord "
			+ "where event = 'EXIT' and str_to_date(value,'%d-%m-%Y') between :startDate and :endDate"
			+ " group by emp_id  order by emp_id")
	List<EmployeeRecord> getEmployeeByExitMonth(@Param("startDate")LocalDate monthBegin,@Param("endDate")LocalDate monthEnd);

	@Query("select employeeRecord "
			+ "from EmployeeRecord employeeRecord "
			+ "where eventDate between :startDate and :endDate")
	List<EmployeeRecord> getYearlyReport(@Param("startDate")Date dateBegin, @Param("endDate")Date dateEnd);
	
	@Query("select new com.example.demo.dtos.MonthlySalaryReportDto(MONTH(eventDate),SUM(e.value), count(distinct(e.empId))) "
			+ "from EmployeeRecord e "
			+ "where eventDate between :startDate and :endDate "
			+ "and lower(event) in ('salary', 'bonus', 'reimbursement')")
	List<MonthlySalaryReportDto> getMonthlySalaryReport(@Param("startDate")Date dateBegin, @Param("endDate")Date dateEnd);
	
	@Query("SELECT new com.example.demo.dtos.EmployeeFinacialReportDto(empId,empFName,empLName,sum(value)) FROM EmployeeRecord "
			+ "where event in ('SALARY', 'BONUS', 'REIMBURSEMENT')and eventDate between :startDate and :endDate GROUP BY empId")
	List<EmployeeFinacialReportDto> getEmployeeFinancialReport(@Param("startDate")Date dateBegin, @Param("endDate")Date dateEnd);
	
	@Query("SELECT new com.example.demo.dtos.MonthlyAmountReleasedDto(MONTH(eventDate),sum(value),count(distinct(empId))) FROM EmployeeRecord where lower(event) in ('salary', 'bonus', 'reimbursement') "
			+ "and eventDate between :startDate and :endDate")
	List<MonthlyAmountReleasedDto> getMonthlyAmountReleasedReport(@Param("startDate")Date dateBegin, @Param("endDate")Date dateEnd);
}
