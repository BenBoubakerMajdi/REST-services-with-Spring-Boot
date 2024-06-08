package com.example.Payroll.order;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "CUSTOMER_ORDER")
public class Order {
   public @Id
   @GeneratedValue Long Id;
   public String description;
   public Status status;

   public Order() {}

   public Order(String description, Status status) {
      this.description = description;
      this.status = status;
   }

   public Long getId() {
      return Id;
   }

   public void setId(Long id) {
      Id = id;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public Status getStatus() {
      return status;
   }

   public void setStatus(Status status) {
      this.status = status;
   }

   @Override
   public boolean equals(Object o) {

      if (this == o)
         return true;
      if (!(o instanceof Order))
         return false;
      Order order = (Order) o;
      return Objects.equals(this.Id, order.Id) && Objects.equals(this.description, order.description)
              && this.status == order.status;
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.Id, this.description, this.status);
   }

   @Override
   public String toString() {
      return "Order{" + "id=" + this.Id + ", description='" + this.description + '\'' + ", status=" + this.status + '}';
   }
}
