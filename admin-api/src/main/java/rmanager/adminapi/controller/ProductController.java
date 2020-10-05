package rmanager.adminapi.controller;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rmanager.commons.exception.SaveImgException;
import rmanager.adminapi.annotation.AuthRequired;
import rmanager.adminapi.dto.ImgDTO;
import rmanager.adminapi.dto.ProductDTO;
import rmanager.adminapi.service.ConvertService;
import rmanager.commons.entity.Img;
import rmanager.commons.entity.Product;
import rmanager.commons.entity.ProductCategory;
import rmanager.commons.entity.other.UserRole;
import rmanager.commons.service.ImgService;
import rmanager.commons.service.ProductCategoryService;
import rmanager.commons.service.ProductService;

import java.io.File;

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
                JSONObject errObj = new JSONObject();
                errObj.put("err", "Does not contain an image");
                return new ResponseEntity( errObj.toJSONString(), HttpStatus.BAD_REQUEST);
            }

            product.setImg(saveNewImg(multipartFile));

            if (productDTO.getCategory() != null) {
                ProductCategory category = productCategoryService.getById(productDTO.getCategory().getProductCategoryId());
                if (category != null) {
                    product.setProductCategory(category);
                }
            }
            productDTO = convertService.convertProductToDTO(productService.save(product));
            return ResponseEntity.ok(productDTO);
        } catch (SaveImgException e) {
            JSONObject errObj = new JSONObject();
            errObj.put("err", e.getMessage());
            return new ResponseEntity(errObj.toJSONString(), HttpStatus.BAD_REQUEST);
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

            if (productDTO.getCategory() != null) {
                ProductCategory category = productCategoryService.getById(productDTO.getCategory().getProductCategoryId());
                if (category != null) {
                    product.setProductCategory(category);
                }
            }
            productDTO = convertService.convertProductToDTO(productService.save(product));
            return ResponseEntity.ok(productDTO);
        } catch (SaveImgException e) {
            JSONObject errObj = new JSONObject();
            errObj.put("err", e.getMessage());
            return new ResponseEntity(errObj.toJSONString(), HttpStatus.BAD_REQUEST);
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

}
