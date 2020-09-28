package rmanager.adminapi.service;

import rmanager.adminapi.dto.UserDTO;
import rmanager.commons.entity.User;

public interface ConvertService {
    UserDTO convertUserToDTO(User user);
    User convertUserFromDTO(UserDTO userDTO);
}
