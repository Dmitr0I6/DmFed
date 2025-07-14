package ru.dmitry.tgBot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "order-products",path = "order-products")
public interface OrderProductRepository extends JpaRepository<OrderProduct,Long> {

}
