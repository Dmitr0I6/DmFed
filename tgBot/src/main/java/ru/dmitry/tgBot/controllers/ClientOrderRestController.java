package ru.dmitry.tgBot.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dmitry.tgBot.entity.ClientOrder;
import ru.dmitry.tgBot.service.ClientOrderService;

import java.util.List;

@RestController
@RequestMapping("/rest/clients")
public class ClientOrderRestController {

    private final ClientOrderService clientOrderService;

    public ClientOrderRestController( ClientOrderService clientOrderService){
        this.clientOrderService = clientOrderService;
    }

    @GetMapping(value = "/{id}/orders")
    public List<ClientOrder> getClientOrders(@PathVariable Long id){
        return clientOrderService.getClientOrders(id);
    }


}
