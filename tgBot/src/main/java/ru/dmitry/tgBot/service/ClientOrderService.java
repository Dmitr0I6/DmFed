package ru.dmitry.tgBot.service;

import ru.dmitry.tgBot.entity.ClientOrder;

import java.util.List;

public interface ClientOrderService {

    List<ClientOrder> getClientOrders(Long clientId);

}
