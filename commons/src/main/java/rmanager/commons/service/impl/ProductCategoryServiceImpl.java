package rmanager.commons.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rmanager.commons.entity.ProductCategory;
import rmanager.commons.repository.product.ProductCategoryRepository;
import rmanager.commons.service.ProductCategoryService;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductCategoryServiceImpl(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    public ProductCategory getById(Integer id) {
        return productCategoryRepository.findById(id).orElse(null);
    }

    @Override
    public ProductCategory getByName(String name) {
        return productCategoryRepository.findByName(name).orElse(null);
    }

    @Override
    public List<ProductCategory> getAll() {
        return productCategoryRepository.findAll();
    }

    @Override
    @Transactional
    public ProductCategory save(ProductCategory category) {
        return productCategoryRepository.saveAndFlush(category);
    }

    @Override
    @Transactional
    public void delete(ProductCategory category) {
        productCategoryRepository.delete(category);
    }
}
