package ru.dmitry.tgBot.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dmitry.tgBot.entity.Client;
import ru.dmitry.tgBot.service.ClientService;

import java.util.List;

@RestController
public class ClientRestController {

    private final ClientService clientService;

    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/rest/clients/search")
    public List<Client> findClientByName(@RequestParam String name) {
        return clientService.getClientByName(name);
    }
}
