package com.example.Payroll.order;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {
   public EntityModel<Order> toModel(Order order) {
      EntityModel<Order> orderModel = EntityModel.of(
              order,
              linkTo(methodOn(OrderController.class).getOrder(order.getId())).withSelfRel(),
              linkTo(methodOn(OrderController.class).getAllOrders()).withRel("orders"));

      if (order.getStatus() == Status.IN_PROGRESS) {
         linkTo(methodOn(OrderController.class).cancel(order.getId())).withRel("cancel");
         linkTo(methodOn(OrderController.class).complete(order.getId())).withRel("complete");
      }

      return orderModel;
   }
}
