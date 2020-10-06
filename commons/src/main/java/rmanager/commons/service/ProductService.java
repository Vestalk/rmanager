package rmanager.commons.service;

import org.springframework.data.domain.PageRequest;
import rmanager.commons.entity.Product;
import rmanager.commons.repository.filter.ProductFilter;

import java.util.List;

public interface ProductService {

    Product getById(Integer id);
    Product getByName(String name);

    List<Product> getAll();
    List<Product> getByFilter(ProductFilter filter);
    List<Product> getByFilter(ProductFilter filter, PageRequest pageable);

    Long countAll();
    Long countByFilter(ProductFilter filter);

    Product save(Product product);
    void delete(Product product);

}
