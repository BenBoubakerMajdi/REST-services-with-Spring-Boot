package com.example.Payroll.order;

public class OrderNotFoundException extends RuntimeException {
   OrderNotFoundException(Long Id) {
      super("Could not find order" + Id);
   }
}
