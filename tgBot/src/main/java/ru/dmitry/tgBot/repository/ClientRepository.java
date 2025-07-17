package ru.dmitry.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.dmitry.tgBot.entity.Client;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "clients", path = "clients")
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT c FROM Client c WHERE LOWER(c.fullName) LIKE LOWER(concat('%', :name,'%'))")
    List<Client> findClientsByName(String name);
}
