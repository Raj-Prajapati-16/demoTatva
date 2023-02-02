package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MonthlyAmountReleasedDto {

	Integer month;
	String totalAmount;
	Long totalEmployees;
}
