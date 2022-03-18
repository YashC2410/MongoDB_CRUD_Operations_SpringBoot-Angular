package com.neosoft.controller;

import com.neosoft.dto.Response;
import com.neosoft.exception.ResourceNotFoundException;
import com.neosoft.model.Employee;
import com.neosoft.service.EmployeeService;
import com.neosoft.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins="http://localhost:4200")
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @GetMapping("/employees")
    public Response getAllEmployees(){
        return employeeService.getALlEmployees();
    }

    @PostMapping("/employees")
    public Response saveEmployee(@Valid @RequestBody Employee employee){
        employee.setId(sequenceGeneratorService.generateSequence(Employee.SEQUENCE_NAME));
        return employeeService.saveEmployee(employee);
    }

    @GetMapping("/employees/{id}")
    public Response getEmployeeById(@PathVariable(value = "id") Long employeeId)
            throws ResourceNotFoundException {
         return  employeeService.getEmployeeById(employeeId);
    }

    @PutMapping("/employees/{id}")
    public Response updateEmployee(@PathVariable(value = "id") Long employeeId,
                                                 @Valid  @RequestBody Employee employeeDetails) throws ResourceNotFoundException {
       return  employeeService.updateEmployee(employeeId, employeeDetails);
    }

    @DeleteMapping("/employees/{id}")
    public Response deleteEmployee(@PathVariable(value = "id") Long employeeId)
            throws ResourceNotFoundException {
       return employeeService.deleteEmployee(employeeId);
    }

    @GetMapping("/employees/{firstName}/{lastName}/{emailId}/{ageRange}")
    public Response test(@PathVariable(value="firstName") String firstName, @PathVariable(value="lastName") String lastName,
                         @PathVariable(value="emailId") String emailId, @PathVariable(value="ageRange") String ageRange){
        return employeeService.getEmployeeDetails(firstName,lastName,emailId,ageRange);
    }






}
