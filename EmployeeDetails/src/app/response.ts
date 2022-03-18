import { Employee } from "./employee";

export class Response {
    statusCode: number;
    message: string;
    employees: Employee[];
    employeeDetails: Employee;
}
