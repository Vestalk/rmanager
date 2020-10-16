package rmanager.adminapi.service;

import rmanager.adminapi.dto.*;
import rmanager.commons.entity.*;

public interface ConvertService {
    UserDTO convertUserToDTO(User user);
    User convertUserFromDTO(UserDTO userDTO);

    ProductCategoryDTO convertProductCategoryToDTO(ProductCategory category);

    ProductDTO convertProductToDTO(Product product);
    Product convertProductFromDTO(ProductDTO productDTO);

    ImgDTO convertImgToDTO(Img img);

    OrderDTO convertOrderToDTO(Order order);

    OrderItemDTO convertOrderItemToDTO(OrderItem orderItem);

    TelegramUserDTO convertTelegramUserToDTO(TelegramUser telegramUser);
}
