package ru.dmitry.tgBot.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dmitry.tgBot.entity.Product;
import ru.dmitry.tgBot.service.ProductService;

import java.util.List;

@RestController
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productServiceImpl) {
        this.productService = productServiceImpl;
    }

    @GetMapping(value = "/rest/products/popular")
    public List<Product> getMostPopularProducts(@RequestParam Integer limit) {
        return productService.getMostPopularProducts(limit);
    }

    @GetMapping(value = "/rest/products/search")
    public List<Product> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId) {

        return productService.searchProducts(name, categoryId);
    }
}
