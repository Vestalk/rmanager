package rmanager.adminapi.controller;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rmanager.commons.exception.SaveImgException;
import rmanager.adminapi.annotation.AuthRequired;
import rmanager.adminapi.dto.ProductDTO;
import rmanager.adminapi.service.ConvertService;
import rmanager.commons.entity.Img;
import rmanager.commons.entity.Product;
import rmanager.commons.entity.ProductCategory;
import rmanager.commons.entity.other.UserRole;
import rmanager.commons.repository.filter.ProductFilter;
import rmanager.commons.service.ImgService;
import rmanager.commons.service.ProductCategoryService;
import rmanager.commons.service.ProductService;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    private ImgService imgService;
    private ConvertService convertService;
    private ProductService productService;
    private ProductCategoryService productCategoryService;

    @Autowired
    public ProductController(ImgService imgService,
                             ConvertService convertService,
                             ProductService productService,
                             ProductCategoryService productCategoryService) {
        this.imgService = imgService;
        this.convertService = convertService;
        this.productService = productService;
        this.productCategoryService = productCategoryService;
    }

    @AuthRequired({UserRole.ADMIN})
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity createNewProduct(@RequestHeader(name = "Authentication") String token,
                                         @RequestParam(value = "image", required = false) MultipartFile multipartFile,
                                         ProductDTO productDTO) {
        try {
            Product product = convertService.convertProductFromDTO(productDTO);

            if (multipartFile == null) {
                return new ResponseEntity(getErrAsJson("Does not contain an image"), HttpStatus.BAD_REQUEST);
            }
            product.setImg(saveNewImg(multipartFile));

            if (productDTO.getCategoryId() != null) {
                ProductCategory category = productCategoryService.getById(productDTO.getCategoryId());
                if (category != null) {
                    product.setProductCategory(category);
                }
            }
            product.setIsArchived(false);
            productDTO = convertService.convertProductToDTO(productService.save(product));
            return ResponseEntity.ok(productDTO);
        } catch (SaveImgException e) {
            return new ResponseEntity(getErrAsJson(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @AuthRequired({UserRole.ADMIN})
    @PostMapping(value = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity editProduct(@RequestHeader(name = "Authentication") String token,
                                         @RequestParam(value = "image", required = false) MultipartFile multipartFile,
                                         ProductDTO productDTO) {
        try {
            Product product = productService.getById(productDTO.getProductId());
            if (product == null) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }

            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setCost(productDTO.getCost());
            product.setIsAvailable(productDTO.getIsAvailable());

            if (multipartFile != null) {
                Img oldImg = product.getImg();
                deleteOldImgFile(oldImg);
                product.setImg(saveNewImg(multipartFile));
                imgService.delete(oldImg);
            }

            if (productDTO.getCategoryId() != null) {
                ProductCategory category = productCategoryService.getById(productDTO.getCategoryId());
                if (category != null) {
                    product.setProductCategory(category);
                }
            }
            productDTO = convertService.convertProductToDTO(productService.save(product));
            return ResponseEntity.ok(productDTO);
        } catch (SaveImgException e) {
            return new ResponseEntity(getErrAsJson(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    private Img saveNewImg(MultipartFile multipartFile) throws SaveImgException {
        String imgPath = imgService.saveImg(multipartFile);
        Img img = new Img();
        img.setImageLink(imgPath);
        return imgService.save(img);
    }

    private Boolean deleteOldImgFile(Img img) {
        File imgFile = new File(img.getImageLink());
        return imgFile.delete();
    }

    @GetMapping
    @AuthRequired({UserRole.ADMIN})
    public ResponseEntity getProductPage(@RequestHeader(name = "Authentication") String token,
                                         @RequestParam(name = "page") Integer page,
                                         @RequestParam(name = "pageSize") Integer pageSize,
                                         ProductFilter productFilter) {
        if (productFilter.getProductCategoryId() != null) {
            ProductCategory category = productCategoryService.getById(productFilter.getProductCategoryId());
            if (category == null) {
                return new ResponseEntity(getErrAsJson("Category not found"), HttpStatus.NOT_FOUND);
            }
        }
        PageRequest pageable = PageRequest.of(page, pageSize);
        List<Product> productList = productService.getByFilter(productFilter, pageable);
        List<ProductDTO> productDTOList = productList.stream().map(product -> convertService.convertProductToDTO(product)).collect(Collectors.toList());
        return ResponseEntity.ok(productDTOList);
    }

    @GetMapping(value = "/count")
    @AuthRequired({UserRole.ADMIN})
    public ResponseEntity countProduct(@RequestHeader(name = "Authentication") String token,
                                         ProductFilter productFilter) {
        if (productFilter.getProductCategoryId() != null) {
            ProductCategory category = productCategoryService.getById(productFilter.getProductCategoryId());
            if (category == null) {
                return new ResponseEntity(getErrAsJson("Category not found"), HttpStatus.NOT_FOUND);
            }
        }
        Long countProducts = productService.countByFilter(productFilter);
        return ResponseEntity.ok(countProducts);
    }

    @DeleteMapping(value = "/{productId}")
    public ResponseEntity archiveProduct(@RequestHeader(name = "Authentication") String token,
                                         @PathVariable(name = "productId") Integer productId) {
        Product product = productService.getById(productId);
        if (product != null) {
            product.setIsArchived(true);
            productService.save(product);
        }
        return ResponseEntity.ok().build();
    }

    private String getErrAsJson(String err) {
        JSONObject errObj = new JSONObject();
        errObj.put("err", err);
        return errObj.toJSONString();
    }

}
