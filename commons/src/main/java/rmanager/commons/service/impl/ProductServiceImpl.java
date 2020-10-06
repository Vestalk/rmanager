package rmanager.commons.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import rmanager.commons.entity.Product;
import rmanager.commons.repository.filter.ProductFilter;
import rmanager.commons.repository.product.ProductListRepository;
import rmanager.commons.repository.product.ProductRepository;
import rmanager.commons.service.ProductService;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private ProductListRepository productListRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              ProductListRepository productListRepository) {
        this.productRepository = productRepository;
        this.productListRepository = productListRepository;
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
    public List<Product> getByFilter(ProductFilter filter) {
        return productListRepository.getByFilter(filter);
    }

    @Override
    public List<Product> getByFilter(ProductFilter filter, PageRequest pageable) {
        Page page =  productListRepository.getByFilter(filter, pageable);
        return page.getContent();
    }

    @Override
    public Long countAll() {
        return productRepository.count();
    }

    @Override
    public Long countByFilter(ProductFilter filter) {
        return productListRepository.countByFilter(filter);
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
