package ru.dmitry.tgBot.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.dmitry.tgBot.entity.Product;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT prod FROM Product prod WHERE prod.category.id = :id")
    List<Product> findByCategoryId(Long id);

    @Query("SELECT p FROM Product p " +
            "JOIN OrderProduct op ON p.id = op.product.id " +
            "GROUP BY p.id " +
            "ORDER BY SUM(op.countProduct) DESC")
    List<Product> findMostPopularProducts(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR LOWER(p.name) LIKE LOWER(concat('%', :name, '%'))) AND " +
            "(:categoryId IS NULL OR p.category.id = :categoryId)")
    List<Product> searchProducts(String name, Long categoryId);

}
