package rmanager.commons.service.impl;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rmanager.commons.entity.Product;
import rmanager.commons.repository.UserRepository;
import rmanager.commons.repository.UserTokenRepository;
import rmanager.commons.repository.product.ProductListRepository;
import rmanager.commons.repository.product.ProductRepository;
import rmanager.commons.service.ProductService;
import rmanager.commons.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ProductServiceImplTest {

    private final Integer PRODUCT_ID = 1;
    private final String NAME = "test";
    private final Product PRODUCT;
    private final List<Product> PRODUCT_LIST;

    {
        PRODUCT = Product
                .builder()
                .productId(PRODUCT_ID)
                .name(NAME)
                .build();

        PRODUCT_LIST = Arrays.asList(
                Product.builder().productId(1).name("test1").build(),
                Product.builder().productId(2).name("test2").build(),
                Product.builder().productId(3).name("test3").build()
        );
    }

    @Autowired
    private ProductService productService;


    //Mocked beans
    @MockBean
    private ProductRepository mockProductRepository;

    @MockBean
    private ProductListRepository mockProductListRepository;


    @Test
    public void testGetByIdIfNotFound(){
        when(mockProductRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());
        assertThat(productService.getById(PRODUCT_ID)).isNull();
    }

    @Test
    public void testGetByIdIfFound(){
        when(mockProductRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(PRODUCT));
        Product expected = productService.getById(PRODUCT_ID);
        assertThat(expected).isEqualTo(PRODUCT);
    }

    @Test
    public void testGetByNameIfNotFound() {
        when(mockProductRepository.findByName(NAME)).thenReturn(Optional.empty());
        assertThat(productService.getByName(NAME)).isNull();
    }

    @Test
    public void testGetByNameIfFound(){
        when(mockProductRepository.findByName(NAME)).thenReturn(Optional.of(PRODUCT));
        Product expected = productService.getByName(NAME);
        assertThat(expected).isEqualTo(PRODUCT);
    }
}
