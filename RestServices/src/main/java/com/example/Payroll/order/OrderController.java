package com.example.Payroll.order;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OrderController {
   private final OrderRepository orderRepo;
   private final OrderModelAssembler assembler;

   OrderController(OrderRepository orderRepo, OrderModelAssembler assembler) {
      this.orderRepo = orderRepo;
      this.assembler = assembler;
   }

   @GetMapping("/orders")
   CollectionModel<EntityModel<Order>> getAllOrders() {
      List<EntityModel<Order>> orders = orderRepo.findAll().stream()
              .map(assembler::toModel)
              .collect(Collectors.toList());
      return CollectionModel.of(
              orders,
              linkTo(methodOn(OrderController.class).getAllOrders()).withSelfRel()
      );
   }

   @GetMapping("/order/{Id}")
   EntityModel<Order> getOrder(@PathVariable Long Id) {
      Order order = orderRepo.findById(Id)
              .orElseThrow(() -> new OrderNotFoundException(Id));
      return assembler.toModel(order);
   }

   @PostMapping("/order")
   ResponseEntity<?> createOrder(@RequestBody Order order) {
      order.setStatus(Status.IN_PROGRESS);
      Order neworder = orderRepo.save(order);
      return ResponseEntity
              .created(linkTo(methodOn(OrderController.class).getOrder(neworder.getId())).toUri())
              .body(neworder);
   }

   @DeleteMapping("/order/{Id}/cancel")
   ResponseEntity<?> cancel(@PathVariable Long Id) {
      Order order = orderRepo.findById(Id)
              .orElseThrow(() -> new OrderNotFoundException(Id));

      if (order.getStatus() == Status.IN_PROGRESS) {
         order.setStatus(Status.CANCELLED);
         return ResponseEntity.ok(assembler.toModel(orderRepo.save(order)));
      }
      return ResponseEntity
              .status(HttpStatus.METHOD_NOT_ALLOWED)
              .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
              .body(Problem.create()
                      .withTitle("Method not allowed")
                      .withDetail("You can't cancel an order that is in the " + order.getStatus() + " status.")
              );
   }

   @PutMapping("/order/{Id}/complete")
   ResponseEntity<?> complete(@PathVariable Long Id) {
      Order order = orderRepo.findById(Id)
              .orElseThrow(() -> new OrderNotFoundException(Id));

      if (order.getStatus() == Status.IN_PROGRESS) {
         order.setStatus(Status.COMPLETED);
         return ResponseEntity.ok(assembler.toModel(orderRepo.save(order)));
      }

      return ResponseEntity
              .status(HttpStatus.METHOD_NOT_ALLOWED)
              .header(HttpHeaders.CONTENT_TYPE,
                      MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
              .body(Problem.create().withTitle("Method not allowed")
                      .withDetail("You can't complete an order that is in the " + order.getStatus() + " status."));
   }
}
