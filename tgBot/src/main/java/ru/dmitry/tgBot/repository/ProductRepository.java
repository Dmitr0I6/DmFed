package ru.dmitry.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.dmitry.tgBot.entity.Product;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT prod FROM Product prod WHERE prod.category.id = :id")
    Optional<List<Product>> findByCategoryId(Long id);

    @Query(value = "SELECT p.* FROM product p " +
            "JOIN order_product op ON p.id = op.product_id " +
            "GROUP BY p.id " +
            "ORDER BY SUM(op.count_product) DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Product> findMostPopularProducts(@Param("limit") Integer limit);

    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR LOWER(p.name) LIKE LOWER(concat('%', :name, '%'))) AND " +
            "(:categoryId IS NULL OR p.category.id = :categoryId)")
    Optional<List<Product>> searchProducts(String name, Long categoryId);


    Optional<Product> findByName(String productName);
}
