package ru.dmitry.tgBot.service;

import ru.dmitry.tgBot.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getProductsByCategoryId(Long categoryId);
    List<Product> getMostPopularProducts(Integer limit);
    List<Product> searchProducts(String name, Long categoryId);
}
