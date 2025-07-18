package ru.dmitry.tgBot.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.dmitry.tgBot.entity.*;
import ru.dmitry.tgBot.repository.*;
import ru.dmitry.tgBot.service.EntitiesService;

import java.util.*;

@Service
@Transactional
public class EntitiesServiceImpl implements EntitiesService {

    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final ClientOrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final CategoryRepository categoryRepository;

    public EntitiesServiceImpl(ProductRepository productRepository,
                               ClientRepository clientRepository,
                               ClientOrderRepository orderRepository,
                               OrderProductRepository orderProductRepository,
                               CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @Override
    public Client getClientById(Long clientId) {
        return clientRepository.findById(clientId).orElse(null);
    }

    @Override
    public void addProductToOrder(ClientOrder order, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        Optional<OrderProduct> existingOrderProduct = orderProductRepository.findByClientOrderAndProduct(order, product);

        if (existingOrderProduct.isPresent()) {
            OrderProduct op = existingOrderProduct.get();
            op.setCountProduct(op.getCountProduct() + 1);
            orderProductRepository.save(op);
        } else {
            OrderProduct newOrderProduct = new OrderProduct();
            newOrderProduct.setClientOrder(order);
            newOrderProduct.setProduct(product);
            newOrderProduct.setCountProduct(1);
            orderProductRepository.save(newOrderProduct);
        }

        order.setTotal(calculateOrderTotal(order));
        orderRepository.save(order);
    }

    @Override
    public Client getOrCreateClient(Long externalId, String fullName, String phoneNumber, String address) {
        return clientRepository.findByExternalId(externalId)
                .orElseGet(() -> {
                    Client newClient = new Client();
                    newClient.setExternalId(externalId);
                    newClient.setFullName(fullName);
                    newClient.setPhoneNumber(phoneNumber != null ? phoneNumber : "N/A");
                    newClient.setAddress(address != null ? address : "N/A");
                    return clientRepository.save(newClient);
                });
    }

    @Override
    public ClientOrder getOrCreateActiveOrder(Client client) {
        Optional<ClientOrder> activeOrder = orderRepository.findByClientAndStatus(client, 1);
        if (activeOrder.isPresent()) {
            return activeOrder.get();
        } else {
            ClientOrder newOrder = new ClientOrder();
            newOrder.setClient(client);
            newOrder.setStatus(1);
            newOrder.setTotal(0.0);
            return orderRepository.save(newOrder);
        }
    }

    @Override
    public List<Category> getCategoriesByParentId(Long parentId) {
        if (parentId == null) {
            return categoryRepository.findByParentIsNull();
        } else {
            return categoryRepository.findByParentId(parentId);
        }
    }

    @Override
    public Double calculateOrderTotal(ClientOrder order) {
        return orderProductRepository.findByClientOrder(order).stream()
                .mapToDouble(op -> op.getProduct().getPrice() * op.getCountProduct())
                .sum();
    }

    @Override
    public void closeOrder(ClientOrder order) {
        if (orderProductRepository.findByClientOrder(order).isEmpty()) {
            throw new IllegalStateException("Невозможно закрыть пустой заказ.");
        }
        order.setStatus(2);
        orderRepository.save(order);
    }

    @Override
    public List<Product> getProductsByCategoryIdForDisplay(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }


    @Override
    public List<OrderProduct> getOrderProducts(ClientOrder order) {
        return orderProductRepository.findByClientOrder(order);
    }
}
