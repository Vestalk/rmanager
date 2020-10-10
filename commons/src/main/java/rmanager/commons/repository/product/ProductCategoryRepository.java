package rmanager.commons.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rmanager.commons.entity.ProductCategory;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

    Optional<ProductCategory> findByName(String name);

    @Query(value = "select * from product_categories as pc where 0 < (select count(*) from products as p where p.category_id = pc.category_id)", nativeQuery = true)
    List<ProductCategory> getAllAvailable();

}
