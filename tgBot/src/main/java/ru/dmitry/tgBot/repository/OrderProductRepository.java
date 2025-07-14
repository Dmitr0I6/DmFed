package ru.dmitry.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.dmitry.tgBot.entity.OrderProduct;
import ru.dmitry.tgBot.entity.Product;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "order-products", path = "order-products")
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    @Query("SELECT op.product FROM OrderProduct op WHERE op.clientOrder.client.id = :clientId")
    Optional<List<Product>> findClientProductsByClientId(Long clientId);
}
