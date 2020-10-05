package rmanager.commons.service;

import rmanager.commons.entity.Product;
import rmanager.commons.entity.ProductCategory;

import java.util.List;

public interface ProductService {

    Product getById(Integer id);
    Product getByName(String name);

    List<Product> getAll();
    List<Product> getByCategory(ProductCategory category);

    Product save(Product product);
    void delete(Product product);

}
