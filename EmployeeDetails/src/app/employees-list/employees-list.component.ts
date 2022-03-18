import { Component, OnInit } from '@angular/core';
import { Employee } from '../employee';
import { EmployeeService } from '../employee.service';
import {ModalDismissReasons, NgbModal} from '@ng-bootstrap/ng-bootstrap';
@Component({
  selector: 'app-employees-list',
  templateUrl: './employees-list.component.html',
  styleUrls: ['./employees-list.component.css']
})
export class EmployeesListComponent implements OnInit {

  constructor(private employeeService:EmployeeService, private modalService: NgbModal) { }
  employeeList:Employee[];
  isSuccess:boolean=false;
  isError:boolean=false;
  employeeMessage:string;
  employeeFirstName:String
  employee:Employee = new Employee();
  deleteMessage:String;
  closeResult:String;
  employeeId:number;
  firstName:String;
  lastName:String;
  emailId:String;
  ageRange:String;
  p: Number = 1;
  count: Number = 5;
  ngOnInit(): void {
    this.p =1;
    this.firstName="";
    this.lastName="";
    this.emailId="";
    this.ageRange="";
    this.getAllEmployees();
  }
private getAllEmployees(){
  this.employeeService.getAllEmployees().subscribe(
    data => {
      this.employeeList = data.employees;
      this.employee =new Employee();
      console.log(data);
    }
  )
}

saveEmployee(){
  console.log(this.employee);
  this.employeeService.saveEmployee(this.employee).subscribe(
    data =>{
      if(data.statusCode == 200){
        this.showSuccessAlert() ;
        this.employeeMessage="Employee "+this.employee.firstName+" Added Succesfully";
        this.ngOnInit();
    }
    else{
      this.showErrorAlert();
      this.employeeMessage=data.message;
      this.ngOnInit();
  }
    });
  this.modalService.dismissAll();
}

updateEmployee(){
  this.employeeFirstName = this.employee.firstName;
  this.employeeId = this.employee.id;
  this.employeeService.updateEmployee(this.employee,this.employee.id).subscribe(
    data => {
      if(data.statusCode == 200){
        this.showSuccessAlert() ;
        this.employeeMessage="Employee "+this.employeeFirstName+" Having ID: "+this.employeeId+" Updated Succesfully";
        console.log("Employee Having ID: "+this.employeeId+" Updated Successfully");
        this.ngOnInit();
      } else{
        this.showErrorAlert();
        this.employeeMessage=data.message;
        this.ngOnInit();
    }
    }
  );
  this.modalService.dismissAll();
  this.employee = new Employee();
}

deleteEmployee(){
  this.employeeService.deleteEmployee(this.employeeId).subscribe(
    data =>{
      if(data.statusCode == 200){
        this.showSuccessAlert() ;
        this.employeeMessage ="Employee Having ID: "+this.employeeId+" Deleted Successfully";
        this.ngOnInit();
      } else{
        this.showErrorAlert();
        this.employeeMessage=data.message;
        this.ngOnInit();
    }
    }, error =>
    console.log(error)
    );
    this.modalService.dismissAll();
}

showSuccessAlert() : void {
  if (this.isSuccess) { // if the alert is visible return
    return;
  } 
  this.isSuccess = true;
  setTimeout(()=> this.isSuccess = false,2500); // hide the alert after 2.5s
}

showErrorAlert() : void {
  if (this.isError) { // if the alert is visible return
    return;
  } 
  this.isError = true;
  setTimeout(()=> this.isError = false,2500); // hide the alert after 2.5s
}

openSaveEmployee(content) {
  this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
    this.closeResult = `Closed with: ${result}`;
  }, (reason) => {
    this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
  });
}

private getDismissReason(reason: any): string {
  if (reason === ModalDismissReasons.ESC) {
    return 'by pressing ESC';
  } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
    return 'by clicking on a backdrop';
  } else {
    return `with: ${reason}`;
  }
}

openUpdateEmployee(content,existingEmployee:Employee) {
  this.employeeId = existingEmployee.id;
  this.employee = existingEmployee;
  this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
    this.closeResult = `Closed with: ${result}`;
  }, (reason) => {
    this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
  });
}

openDeleteEmployee(content,id:any,firstName:any) {
  this.employeeId = id;
  this.deleteMessage = "Are You Sure You Want To Delete Employee "+firstName+" Having ID: "+id;
  this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
    this.closeResult = `Closed with: ${result}`;
  }, (reason) => {
    this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
  });
}

closeModal(){
  this.modalService.dismissAll();
  this.ngOnInit();
}

filterEmployeeDetails(){

  if(this.firstName==""){
    this.firstName = "-";
  }
  if(this.lastName==""){
    this.lastName = "-";
  }
  if(this.emailId==""){
    this.emailId = "-";
  }
  if(this.ageRange==""){
    this.ageRange = "-";
  }
  this.employeeService.getEmployeeDetails(this.firstName, this.lastName, this.emailId, this.ageRange).subscribe(
    data =>{
       if(data.statusCode == 200){
         this.p =1;
         this.employeeList = data.employees;
       }
    }
  )
}

clearEmployeeDetails(){
  this.ngOnInit();
}
}

