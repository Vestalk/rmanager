package rmanager.adminapi.service;

import org.springframework.stereotype.Service;
import rmanager.adminapi.dto.ImgDTO;
import rmanager.adminapi.dto.ProductCategoryDTO;
import rmanager.adminapi.dto.ProductDTO;
import rmanager.adminapi.dto.UserDTO;
import rmanager.commons.entity.Img;
import rmanager.commons.entity.Product;
import rmanager.commons.entity.ProductCategory;
import rmanager.commons.entity.User;
import rmanager.commons.entity.other.UserRole;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

@Service
public class ConvertServiceImpl implements ConvertService{

    private static final String IMAGE_PREFIX = "data:image/jpeg;base64,";
    private final String JPG = ".jpg";

    @Override
    public UserDTO convertUserToDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .login(user.getLogin())
                .password(user.getPassword())
                .userRole(user.getUserRole().name())
                .build();
    }

    @Override
    public User convertUserFromDTO(UserDTO userDTO) {
        User user = new User();
        user.setUserRole(UserRole.valueOf(userDTO.getUserRole()));
        user.setLogin(userDTO.getLogin());
        user.setPassword(userDTO.getPassword());
        return user;
    }

    @Override
    public ProductCategoryDTO convertProductCategoryToDTO(ProductCategory category) {
        return ProductCategoryDTO.builder().productCategoryId(category.getProductCategoryId()).name(category.getName()).build();
    }

    @Override
    public ProductDTO convertProductToDTO(Product product) {
        return ProductDTO.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .cost(product.getCost())
                .isAvailable(product.getIsAvailable())
                .img(product.getImg() != null ? convertImgToDTO(product.getImg()) : null)
                .category(product.getProductCategory() != null ? convertProductCategoryToDTO(product.getProductCategory()) : null)
                .build();
    }

    @Override
    public Product convertProductFromDTO(ProductDTO productDTO) {
        Product product = new Product();
        product.setProductId(productDTO.getProductId());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setCost(productDTO.getCost());
        product.setIsAvailable(productDTO.getIsAvailable());
        return product;
    }

    @Override
    public ImgDTO convertImgToDTO(Img img) {
        return ImgDTO.builder()
                .imgId(img.getImgId())
                .imageContent(getImageContent(img))
                .build();
    }

    public String getImageContent(Img img) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            BufferedImage image = ImageIO.read(new File(img.getImageLink()));
            int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType();
            ImageIO.write(resizeImage(image, type, image.getWidth(), image.getHeight()), "jpg", baos);
        } catch (IOException e) {
            System.out.println("ERROR READING IMAGE" + img.getImageLink());
            System.out.println(e.getMessage());
        }
        return IMAGE_PREFIX + encodeToBase64(baos.toByteArray());
    }

    private static String encodeToBase64(byte[] inputByteArray){
        return Base64.getEncoder()
                .withoutPadding()
                .encodeToString(inputByteArray);
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type, Integer width, Integer height){
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }
}
