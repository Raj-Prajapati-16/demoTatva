package com.example.demo.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVFormat.Builder;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.EmployeeRecord;
import com.example.demo.exception.RequiredColumnUnavailableException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@SuppressWarnings("deprecation")
public class CsvUtils {
	
	public static String TYPE = "text/csv";
	
	/**
     * This method converts data to csv format.
     *
     * @param header
     * @param dataList
     * @param fileName
     * @return ResponseEntity
     * @author Raj.Prajapati
     * @since 02-02-2022
     */
	public ResponseEntity<InputStreamResource> downloadToCsv(List<String> header, List<List<String>> dataList, String fileName){
		log.info("Method downloadToCsv called for file :: {}",fileName);
		String[] csvHeader = header.stream().toArray(String[]::new);
        ByteArrayInputStream byteArrayOutputStream;
        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVPrinter csvPrinter = new CSVPrinter(
                        new PrintWriter(out),
                        CSVFormat.DEFAULT.withHeader(csvHeader)
                );
        ) {
            for (List<String> row : dataList) {
                csvPrinter.printRecord(row);
            }
            csvPrinter.flush();
            byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        InputStreamResource fileInputStream = new InputStreamResource(byteArrayOutputStream);
        // setting HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + System.currentTimeMillis() + ".csv");
        // defining the custom Content-Type
        headers.set(HttpHeaders.CONTENT_TYPE, TYPE);
		
		 return new ResponseEntity<>(
	                fileInputStream,
	                headers,
	                HttpStatus.OK
	        );
	}

	/**
     * This method converts csv raw data to cEmployeeRecord objects.
     *
     * @param file
     * @return ResponseEntity
     * @author Raj.Prajapati
     * @since 02-02-2022
     */
	public List<EmployeeRecord> readReportFromCsv(MultipartFile file){
		List<EmployeeRecord> employeeRecords = new ArrayList<>();
			try (
				Reader reader = new InputStreamReader(file.getInputStream());
				CSVParser csvParser = new CSVParser(reader, CSVFormat.EXCEL.withFirstRecordAsHeader().withNullString(""));) {
			for (CSVRecord csvRecord : csvParser) {
				log.info("{}",csvRecord.isMapped("EmpFName"));
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
				validationForRequiredHeaders(csvRecord);
				EmployeeRecord employeeRecord = new EmployeeRecord(
						Long.parseLong(csvRecord.get("SequenceNo")),
						Long.parseLong(csvRecord.get("EmpID")),
						csvRecord.isMapped("EmpFName")? csvRecord.get("EmpFName") : null,
						csvRecord.isMapped("EmpLName")?  csvRecord.get("EmpLName"): null,
						csvRecord.isMapped("Designation") ? csvRecord.get("Designation"):null,
						csvRecord.isMapped("Event")? csvRecord.get("Event"):null,
						csvRecord.isMapped("Value")?csvRecord.get("Value"):null,
						csvRecord.isMapped("EventDate")? formatter.parse(csvRecord.get("EventDate").trim()):null,
						csvRecord.isMapped("Notes")? csvRecord.get("Notes"):null);
				 employeeRecords.add(employeeRecord);
			}
			return employeeRecords;
		} 
			catch (ParseException e) {
				log.error("unable to parse string to other ");
				e.printStackTrace();
			}
			catch (IOException e) {
				log.error(" IOexception from csvUlil class");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
     * This method checks for required columns in csv file.
     *
     * @param file
     * @return boolean
     * @author Raj.Prajapati
     * @since 02-02-2022
     */
	private void validationForRequiredHeaders(CSVRecord csvRecord) {
		if(!csvRecord.isMapped("SequenceNo"))
			throw new RequiredColumnUnavailableException("SequenceNo");
		if(!csvRecord.isMapped("EmpID"))
			throw new RequiredColumnUnavailableException("EmpID");
	}
	
	/**
     * This method validate csv file.
     *
     * @param file
     * @return boolean
     * @author Raj.Prajapati
     * @since 02-02-2022
     */
	public boolean hasTypeCsv(MultipartFile file) {
		if(TYPE.equals(file.getContentType())) {
			return true;
		}
		return false;
	}
	
}
