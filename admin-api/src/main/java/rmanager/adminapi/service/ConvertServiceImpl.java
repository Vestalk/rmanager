package rmanager.adminapi.service;

import org.springframework.stereotype.Service;
import rmanager.adminapi.dto.UserDTO;
import rmanager.commons.entity.User;
import rmanager.commons.entity.other.UserRole;

@Service
public class ConvertServiceImpl implements ConvertService{

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

}
