package com.neosoft.service;

import com.neosoft.dto.Response;
import com.neosoft.model.Employee;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeService {

    Response getALlEmployees();
    Response getEmployeeById(long id);
    Response saveEmployee(Employee employee);
    Response deleteEmployee(long employeeId);
    Response updateEmployee(long employeeId, Employee employeeDetails) ;
    Response getEmployeesByFirstNameAndLastName(String firstName, String lastName);
    Response getEmployeeDetails(String firstName, String lastName, String emailId, String ageRange);

}
