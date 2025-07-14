package ru.dmitry.tgBot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "categories",path = "categories")
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
