import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Response } from './response';
import { Observable } from 'rxjs';
import { Employee } from './employee';
import { HttpHeaders } from '@angular/common/http';
@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  constructor(private httpClient: HttpClient) { }
  headers = new HttpHeaders({
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*',
    'Access-Control-Allow-Headers': 'Content-Type',
    'Access-Control-Allow-Methods': 'GET,POST,OPTIONS,DELETE,PUT',
    'Authorization': 'Bearer szdp79a2kz4wh4frjzuqu4sz6qeth8m3',
  });
  loalhostPath:String = "http://localhost:8100/api/v1/employees";
  getAllEmployees():Observable<Response>{
    return this.httpClient.get<Response>(`${this.loalhostPath}`);
  }

  saveEmployee(employee:Object):Observable<Response>{
     return this.httpClient.post<Response>(`${this.loalhostPath}`,employee);
  }

  updateEmployee(employee:Employee,id:number):Observable<Response>{
    return this.httpClient.put<Response>(`${this.loalhostPath}/${id}`,employee);
  }

  deleteEmployee(id:number):Observable<Response>{
    return this.httpClient.delete<Response>(`${this.loalhostPath}/${id}`);
  }

  getEmployeeDetails(firstName:String,lastName:String,emailId:String,ageRange:String):Observable<Response>{
    return this.httpClient.get<Response>(`${this.loalhostPath}/${firstName}/${lastName}/${emailId}/${ageRange}`);
  }
}
