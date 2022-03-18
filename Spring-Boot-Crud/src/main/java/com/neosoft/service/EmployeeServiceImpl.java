package com.neosoft.service;

import com.neosoft.dto.Response;
import com.neosoft.exception.ResourceNotFoundException;
import com.neosoft.model.Employee;
import com.neosoft.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    private final static String EMPLOYEE_FOUND_MSG = "Employees Retrieved Successfully Having ";
    private final static String NO_EMPLOYEE_FOUND_MSG = "No Employees Were Found Having";

    @Override
    public Response getALlEmployees() {
        Response getAllEmployeeResponse = new Response();
        try{
            List<Employee> employeeList = employeeRepository.findAll();
            getAllEmployeeResponse.setStatusCode(200);
            getAllEmployeeResponse.setMessage("List of Employees Successfully Retrieved");
            getAllEmployeeResponse.setEmployees(employeeList);
        }catch(Exception e){
            getAllEmployeeResponse.setStatusCode(400);
            getAllEmployeeResponse.setMessage("Error While Retrieving Employee's List");
        }
        return getAllEmployeeResponse;
    }

    @Override
    public Response getEmployeeById(long employeeId)  {
        Response getEmployeeResponse = new Response();
        try {
            employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new ResourceNotFoundException(400, "Employee not found for this id :: " + employeeId));
            getEmployeeResponse.setStatusCode(200);
            getEmployeeResponse.setMessage("Employee Successfully Retrieved For Id: " + employeeId);
        }catch (ResourceNotFoundException e){
            getEmployeeResponse.setStatusCode(e.getStatusCode());
            getEmployeeResponse.setMessage(e.getMessage());
        }
            return getEmployeeResponse;
    }

    @Override
    public Response saveEmployee(Employee employee) {
        try{
            if(inputValidation(employee)) {
                Response saveEmployeeResponse = new Response();
                employeeRepository.save(employee);
                saveEmployeeResponse.setStatusCode(200);
                saveEmployeeResponse.setMessage("Employee Created Successfully");
                return saveEmployeeResponse;
            }else{
                return validationResponseBody();
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            return exceptionResponseBody();
        }
    }

    @Override
    public Response deleteEmployee(long employeeId)  {
        Response deleteEmployeeResponse = new Response();
        try{
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new ResourceNotFoundException(400,"Employee not found for this id :: " + employeeId));
                employeeRepository.delete(employee);
                deleteEmployeeResponse.setStatusCode(200);
                deleteEmployeeResponse.setMessage("Employee With ID: " + employeeId + " Deleted Successfully");
            }
        catch(ResourceNotFoundException e){
            deleteEmployeeResponse.setStatusCode(e.getStatusCode());
            deleteEmployeeResponse.setMessage(e.getMessage());
        }
        return deleteEmployeeResponse;
    }

    @Override
    public Response updateEmployee(long employeeId, Employee employeeDetails)  {
        Response updateEmployeeResponse = new Response();
            try{
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new ResourceNotFoundException(400,"Employee not found for this id :: " + employeeId));
            employee.setEmailId(employeeDetails.getEmailId());
            employee.setLastName(employeeDetails.getLastName());
            employee.setFirstName(employeeDetails.getFirstName());
            employee.setAge(employeeDetails.getAge());
            employeeRepository.save(employee);
            updateEmployeeResponse.setStatusCode(200);
            updateEmployeeResponse.setMessage("Employee For ID: " + employeeId+" Updated Successfully");
            }catch (ResourceNotFoundException e){
              updateEmployeeResponse.setStatusCode(e.getStatusCode());
              updateEmployeeResponse.setMessage(e.getMessage());
            }
        return updateEmployeeResponse;

    }

    @Override
    public Response getEmployeesByFirstNameAndLastName(String firstName, String lastName) {

        try{
            String message;
            Response employeeList = new Response();
            Query query = new Query();
          query.addCriteria(Criteria.where("firstName").regex(firstName,"i").and("lastName").regex(lastName,"i"));
          List<Employee> employeeData = mongoTemplate.find(query, Employee.class);
          employeeList.setStatusCode(200);
          String dataMessage = "FirstName: "+firstName+" and LastName: "+lastName;
          message =  employeeData.size()!=0 ? EMPLOYEE_FOUND_MSG + dataMessage
                    :  NO_EMPLOYEE_FOUND_MSG+ dataMessage;
          employeeList.setEmployees(employeeData);
          employeeList.setMessage(message);
            return employeeList;
        }catch(Exception e){
            return exceptionResponseBody();
        }

    }
    public Response getEmployeeByAge(String ageRange) {
        try {
            String message;
            Query query = new Query();
            Response getEmployeeListByAge = new Response();
            if (ageRange.contains("-")) {
                String startRange = ageRange.split("-")[0];
                String endRange = ageRange.split("-")[1];
                query.addCriteria(Criteria.where("age").
                        gte(Integer.parseInt(startRange)).lte(Integer.parseInt(endRange)));
            } else {
                query.addCriteria(Criteria.where("age").is(Integer.parseInt(ageRange)));
            }
            List<Employee> employeeDetails = mongoTemplate.find(query, Employee.class);
            getEmployeeListByAge.setStatusCode(200);
            String dataMessage = "Age :" + ageRange;
            message =  employeeDetails.size()!=0 ? EMPLOYEE_FOUND_MSG + dataMessage
                    :  NO_EMPLOYEE_FOUND_MSG+" Age: " + ageRange ;
            getEmployeeListByAge.setMessage(message);
            getEmployeeListByAge.setEmployees(mongoTemplate.find(query, Employee.class));
            return getEmployeeListByAge;
        } catch (Exception e) {
            return exceptionResponseBody();
        }
    }
    @Override
    public Response getEmployeeDetails(String firstName, String lastName , String emailId, String ageRange){
        if(!firstName.equalsIgnoreCase("-") && lastName.equalsIgnoreCase("-") &&
                emailId.equalsIgnoreCase("-") && ageRange.equalsIgnoreCase("-")){
            return  getEmployeeByFirstName(firstName);
        }
        if(!firstName.equalsIgnoreCase("-") && !lastName.equalsIgnoreCase("-")
                && emailId.equalsIgnoreCase("-") && ageRange.equalsIgnoreCase("-")){
            return getEmployeesByFirstNameAndLastName(firstName,lastName);
        }
        if(!firstName.equalsIgnoreCase("-") && !lastName.isBlank() &&
                !emailId.isBlank() && ageRange.equalsIgnoreCase("-")){
            return getEmployeesByFirstNameAndLastNameAndEmailId(firstName, lastName, emailId);
        }
        if(!firstName.equalsIgnoreCase("-") && !lastName.equalsIgnoreCase("-")
                && emailId.equalsIgnoreCase("-") && !ageRange.equalsIgnoreCase("-")){
            return getEmployeesByFirstNameAndLastNameAndAge(firstName, lastName, ageRange);
        }
        if(!firstName.equalsIgnoreCase("-") && !lastName.equalsIgnoreCase("-") &&
                !emailId.equalsIgnoreCase("-") && !ageRange.equalsIgnoreCase("-")){
            return getEmployeesByFirstNameAndLastNameAndEmailIdAndAge(firstName, lastName, emailId, ageRange);
        }
        if(firstName.equalsIgnoreCase("-") && !lastName.equalsIgnoreCase("-")
                && emailId.equalsIgnoreCase("-") && ageRange.equalsIgnoreCase("-")){
            return getEmployeesByLastName(lastName);
        }
        if(firstName.equalsIgnoreCase("-") && !lastName.equalsIgnoreCase("-") &&
                !emailId.equalsIgnoreCase("-") && ageRange.equalsIgnoreCase("-")){
           return getEmployeesByLastNameEmailId(lastName, emailId);
        }
        if(firstName.equalsIgnoreCase("-") && !lastName.equalsIgnoreCase("-")
                && !emailId.equalsIgnoreCase("-") && !ageRange.equalsIgnoreCase("-")){
            return getEmployeesByLastNameAndEmailIDAndAge(lastName,emailId,ageRange);
        }
        if(firstName.equalsIgnoreCase("-") && !lastName.equalsIgnoreCase("-")
                && emailId.equalsIgnoreCase("-") && !ageRange.equalsIgnoreCase("-")){
           return  getEmployeesByLastNameAndAge(lastName,ageRange);
        }
        if(firstName.equalsIgnoreCase("-") && lastName.equalsIgnoreCase("-")
                && !emailId.equalsIgnoreCase("-") && ageRange.equalsIgnoreCase("-")){
            return getEmployeeByEmailId(emailId);
        }
        if(firstName.equalsIgnoreCase("-") && lastName.equalsIgnoreCase("-") &&
                !emailId.equalsIgnoreCase("-") && !ageRange.equalsIgnoreCase("-")){
            return getEmployeesByEmailIdAndAge(emailId,ageRange);
        }
        if(firstName.equalsIgnoreCase("-") && lastName.equalsIgnoreCase("-")
                && emailId.equalsIgnoreCase("-") && !ageRange.equalsIgnoreCase("-")){
            return getEmployeeByAge(ageRange);
        }else{
            return getALlEmployees();
        }
    }

    private Response getEmployeeByFirstName(String firstName){

        try{
            String message;
            Response employeesByFirstName = new Response();
            List<Employee> employeesList = employeeRepository.findByFirstName(firstName);
            employeesByFirstName.setStatusCode(200);
            String dataMessage = "FirstName: " + firstName;
            employeesByFirstName.setEmployees(employeesList);
            message = employeesList.size()!=0 ? EMPLOYEE_FOUND_MSG+dataMessage
                    :  NO_EMPLOYEE_FOUND_MSG+"FirstName: " + firstName ;
            employeesByFirstName.setMessage(message);
            return employeesByFirstName;
        }catch(Exception e) {
            return exceptionResponseBody();
        }
    }
    public Response getEmployeesByFirstNameAndLastNameAndAge(String firstName, String lastName,
                                                                       String ageRange){
        try{
            Response employeeList = new Response();
            String message;
            List<Employee> employees;
            Query query = new Query();
            if(ageRange.contains("-")) {
                query.addCriteria(Criteria.where("firstName").is(firstName).
                        and("lastName").is(lastName).and("age").
                        gte(Integer.parseInt(ageRange.split("-")[0])).lte(Integer.parseInt(ageRange.split("-")[1])));
            }
            else{
                query.addCriteria(Criteria.where("firstName").is(firstName).
                        and("lastName").is(lastName).and("age").is(Integer.parseInt(ageRange)));
            }
            employees = mongoTemplate.find(query, Employee.class);
            employeeList.setStatusCode(200);
            String dataMessage = "FirstName: " + firstName + " and LastName: " + lastName+" And AgeRange: "+ageRange;
            message = employees.size()!=0 ? EMPLOYEE_FOUND_MSG + dataMessage
                    : NO_EMPLOYEE_FOUND_MSG+" FirstName: " + firstName + " and LastName: " + lastName+" And Age: "+ageRange;
            employeeList.setEmployees(mongoTemplate.find(query, Employee.class));
            employeeList.setMessage(message);
            return employeeList;
        }catch(Exception e){
            return exceptionResponseBody();
        }
    }
    public Response getEmployeesByFirstNameAndLastNameAndEmailId(String firstName, String lastName,
                                                                 String emailId){
        try {
            String message;
            Response employeeDetails = new Response();
            List<Employee> employeeList = employeeRepository.findByFirstNameAndLastNameAndEmailId(firstName, lastName, emailId);
            employeeDetails.setStatusCode(200);
            String dataMessage = "FirstName: " + firstName + " " +
                    "and LastName: " + lastName+" and EmailID: "+emailId;
            message = employeeList.size()!=0 ? EMPLOYEE_FOUND_MSG + dataMessage
                    :  NO_EMPLOYEE_FOUND_MSG+"FirstName: " + firstName + " " +
            "and LastName: " + lastName+" and EmailID: "+emailId;
            employeeDetails.setEmployees(employeeList);
            employeeDetails.setMessage(message);
            return employeeDetails;
        }catch (Exception e){
            return exceptionResponseBody();
        }
    }
    public Response getEmployeesByFirstNameAndLastNameAndEmailIdAndAge(String firstName, String lastName,
                                                                 String emailId, String ageRange) {
        try{
            Response employeeList = new Response();
            Query query = new Query();
            String message;
            if(ageRange.contains("-")) {
                query.addCriteria(Criteria.where("firstName").regex(firstName,"i").
                        and("lastName").regex(lastName,"i").and("emailId").regex(emailId,"i").and("age").
                        gte(Integer.parseInt(ageRange.split("-")[0]))
                        .lte(Integer.parseInt(ageRange.split("-")[1]))
                );
            }
            else{
                query.addCriteria(Criteria.where("firstName").regex(firstName,"i").
                        and("lastName").regex(lastName,"i").and("emailId").regex(emailId,"i").
                        and("age").is(Integer.parseInt(ageRange)));
            }
                List<Employee> employeeData = mongoTemplate.find(query, Employee.class);
                employeeList.setStatusCode(200);
                String dataMessage =" FirstName: " + firstName + " and LastName: " + lastName
                    +"and Age: "+ageRange+" and EmailID: "+emailId;
                message = employeeData.size()!=0 ? EMPLOYEE_FOUND_MSG+ dataMessage
                        :  NO_EMPLOYEE_FOUND_MSG+ dataMessage;
            employeeList.setMessage(message);
                employeeList.setEmployees(employeeData);
                return  employeeList;
            }catch(Exception e){
             return exceptionResponseBody();
        }
    }

    public  Response getEmployeesByLastName(String lastName){
        try {
            String message;
            Response getEmployees = new Response();
            List<Employee> employeeList = employeeRepository.findByLastName(lastName);
            getEmployees.setStatusCode(200);
            String dataMessage ="LastName: " + lastName;
            message = employeeList.size()!=0 ? EMPLOYEE_FOUND_MSG + dataMessage
                    : NO_EMPLOYEE_FOUND_MSG+" LastName: "+lastName;
            getEmployees.setMessage(message);
            getEmployees.setEmployees(employeeList);
            return getEmployees;
        }catch (Exception e){
            return exceptionResponseBody();
        }
    }

    public Response getEmployeesByLastNameEmailId(String lastName, String emailId){
        try{
            String message;
            Response employeeList = new Response();
            List<Employee> employees = employeeRepository.findByLastNameAndEmailId(lastName, emailId);
            String dataMessage = "LastName: " + lastName + " and EmailId: " +emailId;
            employeeList.setStatusCode(200);
            employeeList.setEmployees(employees);
            message = employees.size()!=0 ? EMPLOYEE_FOUND_MSG + dataMessage
                    : NO_EMPLOYEE_FOUND_MSG+ dataMessage;
            employeeList.setMessage(message);
            return employeeList;
        }catch (Exception e){
            return exceptionResponseBody();
        }
    }

    public Response exceptionResponseBody(){
        Response exceptionResponse = new Response();
        exceptionResponse.setStatusCode(400);
        exceptionResponse.setMessage("Something Went Wrong!!!");
        return exceptionResponse;
    }

    public Response validationResponseBody(){
        Response invalidInputResponse = new Response();
        invalidInputResponse.setStatusCode(400);
        invalidInputResponse.setMessage("Error!! Invalid Input's Provided");
        return invalidInputResponse;
    }
    public boolean inputValidation(Employee employee){
       String regexName = "[a-zA-Z]+";
        String regexMail = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
        return employee.getFirstName().matches(regexName) && employee.getLastName().matches(regexName)
                && employee.getEmailId().matches(regexMail);

    }

    public Response getEmployeesByLastNameAndEmailIDAndAge(String lastName, String emailId, String ageRange){
        try {
            String message;
            Response employeesList = new Response();
            List<Employee> employees;
            if(ageRange.contains("-")){
                employees = employeeRepository.findByLastNameAndEmailIdAndAge(lastName,emailId,
                        Integer.parseInt(ageRange.split("-")[0]),Integer.parseInt(ageRange.split("-")[1]));
            }else{
                employees = employeeRepository.findByLastNameAndEmailIdAndAge(lastName,
                        emailId,Integer.parseInt(ageRange));
            }
            employeesList.setEmployees(employees);
            String dataMessage = "LastName: " + lastName + " and Age: " + ageRange+
            "and EmailID: "+emailId;
            employeesList.setStatusCode(200);
            message = employees.size()!=0 ? EMPLOYEE_FOUND_MSG + dataMessage
                    : NO_EMPLOYEE_FOUND_MSG+ dataMessage;
            employeesList.setMessage(message);
            return employeesList;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return exceptionResponseBody();
        }
}
    public Response getEmployeesByLastNameAndAge(String lastName, String ageRange){
        try{
            String message;
            Response getEmployeeResponse = new Response();
            List<Employee> employees;
            if(ageRange.contains("-")){
                Query query = new Query();
                query.addCriteria(Criteria.where("lastName").regex(lastName,"i").and("age").
                        gte(Integer.parseInt(ageRange.split("-")[0])).lte(Integer.parseInt(ageRange.split("-")[1])));
               employees = mongoTemplate.find(query, Employee.class);
            }else{
               employees = employeeRepository.findByLastNameAndAge(lastName,Integer.parseInt(ageRange));
            }
            getEmployeeResponse.setEmployees(employees);
            String dataMessage = "LastName: " + lastName + " and Age: " + ageRange;
            getEmployeeResponse.setStatusCode(200);
            message = employees.size()!=0 ? EMPLOYEE_FOUND_MSG + dataMessage
                    : NO_EMPLOYEE_FOUND_MSG+ dataMessage;
            getEmployeeResponse.setMessage(message);
            return getEmployeeResponse;
        }catch (Exception e){
            return exceptionResponseBody();
        }
}

    public Response getEmployeesByEmailIdAndAge(String emailId, String ageRange){
        try{
            String message;
            Response getEmployeeResponse = new Response();
            List<Employee> employees;
            if(ageRange.contains("-")){
                Query query = new Query();
                query.addCriteria(Criteria.where("emailId").regex(emailId,"i").and("age").
                        gte(Integer.parseInt(ageRange.split("-")[0])).lte(Integer.parseInt(ageRange.split("-")[1])));
                employees = mongoTemplate.find(query, Employee.class);
            }else{
                employees = employeeRepository.findByEmailIdAndAge(emailId,Integer.parseInt(ageRange));
            }
            getEmployeeResponse.setEmployees(employees);
            getEmployeeResponse.setStatusCode(200);
            String dataMessage  = "EmailId: " + emailId + " and Age: " + ageRange;
            message = employees.size()!=0 ? EMPLOYEE_FOUND_MSG + dataMessage
                    : NO_EMPLOYEE_FOUND_MSG+ dataMessage;
            getEmployeeResponse.setMessage(message);
            return getEmployeeResponse;
        }catch (Exception e){
            return exceptionResponseBody();
        }
    }
    public Response getEmployeeByEmailId(String emailId){
        try{
            String message;
            Response employeeData = new Response();
            Query query = new Query();
            query.addCriteria(Criteria.where("emailId").regex(emailId,"i"));
            List<Employee> employeeList = mongoTemplate.find(query, Employee.class);
            employeeData.setStatusCode(200);
            String dataMessage = "EmailId: " + emailId;
            message = employeeList.size()!=0 ? EMPLOYEE_FOUND_MSG+ "EmailId: " + dataMessage
                    : NO_EMPLOYEE_FOUND_MSG+ dataMessage;
            employeeData.setMessage(message);
            employeeData.setEmployees(employeeList);
            return employeeData;
        }catch (Exception e){
            return exceptionResponseBody();
        }

    }
}
