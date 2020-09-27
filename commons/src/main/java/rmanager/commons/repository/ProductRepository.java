package rmanager.commons.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmanager.commons.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
