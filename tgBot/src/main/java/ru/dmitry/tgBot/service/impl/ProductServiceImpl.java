package ru.dmitry.tgBot.service.impl;

import org.springframework.stereotype.Service;
import ru.dmitry.tgBot.entity.Product;
import ru.dmitry.tgBot.repository.ProductRepository;
import ru.dmitry.tgBot.service.ProductService;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).orElseThrow(() -> new RuntimeException("Нет продуктов с такой категорией" + categoryId));
    }

    @Override
    public List<Product> getMostPopularProducts(Integer limit) {
        if (limit <= 0) throw new RuntimeException("Передано отрицательное количество товаров");
        return productRepository.findMostPopularProducts(limit);
    }

    @Override
    public List<Product> searchProducts(String name, Long categoryId) {
        return productRepository.searchProducts(name, categoryId).orElseThrow(() -> new RuntimeException("никаких продуктов не найдено"));
    }
}
