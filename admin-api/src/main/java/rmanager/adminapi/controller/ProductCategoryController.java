package rmanager.adminapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rmanager.adminapi.annotation.AuthRequired;
import rmanager.adminapi.dto.ProductCategoryDTO;
import rmanager.adminapi.service.ConvertService;
import rmanager.commons.entity.ProductCategory;
import rmanager.commons.entity.other.UserRole;
import rmanager.commons.service.ProductCategoryService;

@RestController
@RequestMapping(value = "/product-category")
public class ProductCategoryController {

    private ConvertService convertService;
    private ProductCategoryService productCategoryService;

    @Autowired
    public ProductCategoryController(ConvertService convertService,
                                     ProductCategoryService productCategoryService) {
        this.convertService = convertService;
        this.productCategoryService = productCategoryService;
    }

    @PostMapping
    @AuthRequired({UserRole.ADMIN})
    public ResponseEntity postNewProductCategory(@RequestHeader(name = "Authentication") String token,
                                                 @RequestBody String categoryName) {
        ProductCategory category = productCategoryService.getByName(categoryName);
        if (category == null) {
            category = new ProductCategory();
            category.setName(categoryName);
            category = productCategoryService.save(category);
            ProductCategoryDTO categoryDTO = convertService.convertProductCategoryToDTO(category);
            return ResponseEntity.ok(categoryDTO);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
