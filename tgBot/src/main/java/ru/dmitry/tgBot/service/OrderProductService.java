package ru.dmitry.tgBot.service;

import ru.dmitry.tgBot.entity.Product;

import java.util.List;

public interface OrderProductService {

    List<Product> getClientProducts(Long clientId);
}
