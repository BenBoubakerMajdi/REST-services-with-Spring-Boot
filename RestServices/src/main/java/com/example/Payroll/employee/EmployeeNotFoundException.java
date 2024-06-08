package com.example.Payroll.employee;

class EmployeeNotFoundException extends RuntimeException {
   EmployeeNotFoundException(Long Id) {
      super("Could not find Employee " + Id);
   }
}
