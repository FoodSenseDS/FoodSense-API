package com.foodsense.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodsense.demo.dao.CustomerDAO;
import com.foodsense.demo.dao.OrderDAO;
import com.foodsense.demo.dao.ProductDAO;
import com.foodsense.demo.dto.request.OrderRequestDTO;
import com.foodsense.demo.dto.response.OrderResponseDTO;
import com.foodsense.demo.helper.DateHelper;
import com.foodsense.demo.model.Customer;
import com.foodsense.demo.model.Order;
import com.foodsense.demo.model.OrderItem;
import com.foodsense.demo.model.Product;

@RestController
@RequestMapping("/foodsense/api/v0.0.1/order")
public class OrderController {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private ProductDAO productDAO;

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @GetMapping(value="/get", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> getAllOrder() {
        log.info("Fetching order...");
        List<Order> orders = orderDAO.findAllOrder();
        if (!orders.isEmpty()) {
            log.info("Found all orders: {}", orders.size());
            return ResponseEntity.ok(orders);
        } else {
            log.warn("No order found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(orders);
        }
    }

    @GetMapping(value="/get/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> getOrderRequested(@PathVariable long id){
        log.info("Searching order data with id: {}", id);
        Order order = orderDAO.findOrderDetail(id);
        if (order != null) {
            log.info("Found order with ID: {}", id);
            return ResponseEntity.ok(order);
        } else {
            log.warn("Order not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(order);
        }
    }

    @PostMapping(value="/placeOrder", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponseDTO> placeOrder(@RequestBody OrderRequestDTO order, @RequestParam("customerId") long id){
        log.info("Request payload...");
        OrderResponseDTO response = new OrderResponseDTO();
        double price = orderDAO.getTotal(order.getOrderItems());

        Customer customer = customerDAO.findCustomerByID(id);
        long customer_id = customer.getCustomerId();

        log.info("Existing customer {}", customer_id);
        customer.setCustomerId(customer_id);

        Order orderInit = new Order(customer, order.getOrderItems());
        orderInit = orderDAO.insertOrder(orderInit);

        if (orderInit != null) {
            log.info("Push order {}!!!", orderInit.getOrderId());

            response.setPrice(price);
            response.setOrderId(orderInit.getOrderId());
            response.setDate(DateHelper.getPresentTime());

            log.info("customer {} successfully take an order", customer_id);
            return ResponseEntity.ok(response);
        } else {
            log.warn("Failed take an order");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
