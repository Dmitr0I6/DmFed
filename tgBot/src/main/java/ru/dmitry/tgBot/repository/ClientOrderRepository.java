package ru.dmitry.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.dmitry.tgBot.entity.ClientOrder;
import ru.dmitry.tgBot.entity.Product;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "client-orders", path = "client-orders")
public interface ClientOrderRepository extends JpaRepository<ClientOrder, Long> {
    @Query("SELECT order FROM ClientOrder order WHERE order.client.id = :clientId")
    Optional<List<ClientOrder>> findClientOrdersByClientId(Long clientId);
}
