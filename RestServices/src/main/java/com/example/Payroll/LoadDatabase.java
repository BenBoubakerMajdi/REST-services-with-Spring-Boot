package com.example.Payroll;

import com.example.Payroll.employee.Employee;
import com.example.Payroll.employee.EmployeeRepository;
import com.example.Payroll.order.Order;
import com.example.Payroll.order.Status;
import com.example.Payroll.order.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {
   private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

   @Bean
   CommandLineRunner initDatabase(EmployeeRepository empRepo, OrderRepository orderRepo) {
      return args -> {
         empRepo.save(new Employee("Majdi", "Benboubaker", "Software Engineer"));
         empRepo.save(new Employee("Hazem", "Guefrech", "Software Tester"));

         empRepo.findAll().forEach(employee -> log.info("Preloaded " + employee));

         orderRepo.save(new Order("Chevrolet Corvette", Status.COMPLETED));
         orderRepo.save(new Order("Lamborghini Aventador", Status.IN_PROGRESS));

         orderRepo.findAll().forEach(order -> log.info("Preloaded " + order));
      };
   }
}
