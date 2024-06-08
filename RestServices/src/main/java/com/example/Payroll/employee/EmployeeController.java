package com.example.Payroll.employee;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class EmployeeController {


   private final EmployeeRepository empRepo;
   private final EmployeeModelAssembler assembler;

   EmployeeController(EmployeeRepository empRepo, EmployeeModelAssembler assembler) {
      this.empRepo = empRepo;
      this.assembler = assembler;
   }

   @GetMapping("/employees")
   CollectionModel<EntityModel<Employee>> getAllEmployees() {
      List<EntityModel<Employee>> employees = empRepo.findAll().stream()
              .map(assembler::toModel)
              .collect(Collectors.toList());

      return CollectionModel.of(
              employees,
              linkTo(methodOn(EmployeeController.class).getAllEmployees()).withSelfRel());
   }

   @GetMapping("/employee/{Id}")
   EntityModel<Employee> getEmployee(@PathVariable Long Id) {
      Employee employee = empRepo.findById(Id)
              .orElseThrow(() -> new EmployeeNotFoundException(Id));

      return assembler.toModel(employee);
   }

   @PostMapping("/employee")
   ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) {
      EntityModel<Employee> entityModel = assembler.toModel(empRepo.save(newEmployee));

      return ResponseEntity
              .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
              .body(entityModel);
   }

   @PutMapping("/employee/{Id}")
   ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long Id) {
      Employee updatedEmployee = empRepo.findById(Id)
              .map(employee -> {
                 employee.setName(newEmployee.getName());
                 employee.setRole(newEmployee.getRole());
                 return empRepo.save(employee);
              })
              .orElseGet(() -> {
                 newEmployee.setId(Id);
                 return empRepo.save(newEmployee);
              });

      EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);
      return ResponseEntity
              .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
              .body(entityModel);
   }

   @DeleteMapping("/employee/{Id}")
   ResponseEntity<?> deleteEmployee(@PathVariable Long Id) {

      empRepo.deleteById(Id);
      return ResponseEntity.noContent().build();
   }

}
