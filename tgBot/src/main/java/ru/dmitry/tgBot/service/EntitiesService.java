package ru.dmitry.tgBot.service;

import ru.dmitry.tgBot.entity.*;

import java.util.List;

public interface EntitiesService {

    Double calculateOrderTotal(ClientOrder order);

    void closeOrder(ClientOrder order);

    List<OrderProduct> getOrderProducts(ClientOrder order);

    Product getProductById(Long productId);

    Client getClientById(Long clientId);

    Client getOrCreateClient(Long externalId, String fullName, String phoneNumber, String address);

    ClientOrder getOrCreateActiveOrder(Client client);

    void addProductToOrder(ClientOrder order, Long productId);

    List<Category> getCategoriesByParentId(Long parentId);

    List<Product> getProductsByCategoryIdForDisplay(Long categoryId);


}
