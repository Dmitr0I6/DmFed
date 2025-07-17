package ru.dmitry.tgBot.service;

import ru.dmitry.tgBot.entity.Client;

import java.util.List;

public interface ClientService {
    List<Client> findClientsByName(String name);
}
