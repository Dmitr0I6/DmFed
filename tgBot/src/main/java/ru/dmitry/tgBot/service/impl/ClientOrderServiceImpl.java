package ru.dmitry.tgBot.service.impl;

import org.springframework.stereotype.Service;
import ru.dmitry.tgBot.entity.ClientOrder;
import ru.dmitry.tgBot.repository.ClientOrderRepository;
import ru.dmitry.tgBot.service.ClientOrderService;

import java.util.List;

@Service
public class ClientOrderServiceImpl implements ClientOrderService {

    private final ClientOrderRepository clientOrderRepository;

    public ClientOrderServiceImpl(ClientOrderRepository clientOrderRepository) {
        this.clientOrderRepository = clientOrderRepository;
    }

    @Override
    public List<ClientOrder> getClientOrders(Long clientId) {
        return clientOrderRepository.findClientOrdersByClientId(clientId);
    }

}
