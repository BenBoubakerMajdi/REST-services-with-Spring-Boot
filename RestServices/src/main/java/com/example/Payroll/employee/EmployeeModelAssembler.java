package com.example.Payroll.employee;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
public class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {
   public EntityModel<Employee> toModel(Employee employee) {
      return EntityModel.of(
              employee,
              linkTo(methodOn(EmployeeController.class).getEmployee(employee.getId())).withSelfRel(),
              linkTo(methodOn(EmployeeController.class).getAllEmployees()).withRel("employees")
      );
   }
}
