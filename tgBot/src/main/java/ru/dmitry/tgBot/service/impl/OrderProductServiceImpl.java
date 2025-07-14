package ru.dmitry.tgBot.service.impl;

import org.springframework.stereotype.Service;
import ru.dmitry.tgBot.entity.Product;
import ru.dmitry.tgBot.repository.OrderProductRepository;
import ru.dmitry.tgBot.service.OrderProductService;

import java.util.List;

@Service
public class OrderProductServiceImpl implements OrderProductService {

    private final OrderProductRepository orderProductRepository;

    public OrderProductServiceImpl(OrderProductRepository orderProductRepository){
        this.orderProductRepository = orderProductRepository;
    }

    @Override
    public List<Product> findClientProducts(Long clientId) {
        return orderProductRepository.findClientProductsByClientId(clientId).orElseThrow(() -> new RuntimeException("У клиента нет заказанных продуктов"));
    }
}
