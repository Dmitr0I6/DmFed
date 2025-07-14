package ru.dmitry.tgBot.service;

import ru.dmitry.tgBot.entity.Product;

import java.util.List;

public interface OrderProductService {
    public List<Product> findClientProducts(Long clientId);
}
