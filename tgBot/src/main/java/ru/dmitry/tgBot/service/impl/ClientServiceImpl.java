package ru.dmitry.tgBot.service.impl;

import org.springframework.stereotype.Service;
import ru.dmitry.tgBot.entity.Client;
import ru.dmitry.tgBot.repository.ClientRepository;
import ru.dmitry.tgBot.service.ClientService;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> findClientsByName(String name) {
        return clientRepository.findClientsByName(name);
    }
}
