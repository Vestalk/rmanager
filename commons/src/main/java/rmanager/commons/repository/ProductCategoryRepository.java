package rmanager.commons.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rmanager.commons.entity.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
}
