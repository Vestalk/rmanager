package rmanager.commons.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rmanager.commons.entity.ProductCategory;

import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

    Optional<ProductCategory> findByName(String name);

}
