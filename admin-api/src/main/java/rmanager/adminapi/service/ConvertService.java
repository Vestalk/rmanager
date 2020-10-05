package rmanager.adminapi.service;

import rmanager.adminapi.dto.ImgDTO;
import rmanager.adminapi.dto.ProductCategoryDTO;
import rmanager.adminapi.dto.ProductDTO;
import rmanager.adminapi.dto.UserDTO;
import rmanager.commons.entity.Img;
import rmanager.commons.entity.Product;
import rmanager.commons.entity.ProductCategory;
import rmanager.commons.entity.User;

public interface ConvertService {
    UserDTO convertUserToDTO(User user);
    User convertUserFromDTO(UserDTO userDTO);

    ProductCategoryDTO convertProductCategoryToDTO(ProductCategory category);

    ProductDTO convertProductToDTO(Product product);
    Product convertProductFromDTO(ProductDTO productDTO);

    ImgDTO convertImgToDTO(Img img);
}
