package ru.dmitry.tgBot.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.dmitry.tgBot.entity.Product;
import ru.dmitry.tgBot.service.OrderProductService;

import java.util.List;

@RestController
public class OrderProductsRestController {

    private final OrderProductService orderProductService;

    public OrderProductsRestController(OrderProductService orderProductService) {
        this.orderProductService = orderProductService;
    }

    @GetMapping(value = "/rest/clients/{id}/products")
    public List<Product> getClientProducts(@PathVariable Long id) {
        return orderProductService.findClientProducts(id);
    }
}
