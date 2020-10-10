package rmanager.commons.service;

import rmanager.commons.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService {

    ProductCategory getById(Integer id);
    ProductCategory getByName(String name);

    List<ProductCategory> getAll();
    List<ProductCategory> getAllAvailable();

    ProductCategory save(ProductCategory category);
    void delete(ProductCategory category);

}
