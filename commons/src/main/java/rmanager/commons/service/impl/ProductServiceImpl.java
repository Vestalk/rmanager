package rmanager.commons.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmanager.commons.entity.Product;
import rmanager.commons.entity.ProductCategory;
import rmanager.commons.repository.ProductRepository;
import rmanager.commons.service.ProductService;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product getById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product getByName(String name) {
        return productRepository.findByName(name).orElse(null);
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getByCategory(ProductCategory category) {
        return productRepository.findAllByCategoryId(category.getProductCategoryId());
    }

    @Override
    public Product save(Product product) {
        return productRepository.saveAndFlush(product);
    }

    @Override
    public void delete(Product product) {
        productRepository.delete(product);
    }
}
