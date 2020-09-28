package rmanager.adminapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rmanager.adminapi.annotation.AuthRequired;
import rmanager.adminapi.dto.UserDTO;
import rmanager.adminapi.service.ConvertService;
import rmanager.commons.entity.User;
import rmanager.commons.entity.other.UserRole;
import rmanager.commons.service.UserService;
import rmanager.commons.util.PasswordHasherUtil;

import javax.servlet.ServletException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private UserService userService;
    private ConvertService convertService;

    @Autowired
    public UserController(UserService userService, ConvertService convertService) {
        this.userService = userService;
        this.convertService = convertService;
    }

    @ResponseBody
    @AuthRequired({UserRole.ADMIN})
    @PostMapping(value = "/updateUserInfo", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity postUserInfoForm(@RequestHeader(name = "Authentication") String token,
                                           @RequestBody UserDTO userDTO) {
        User user = userService.getUser(userDTO.getUserId());
        if (user == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        user.setLogin(userDTO.getLogin());
        user.setUserRole(UserRole.valueOf(userDTO.getUserRole()));
        userService.updateUserInfo(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @AuthRequired({UserRole.ADMIN})
    @GetMapping(value = "/all-roles")
    public ResponseEntity getAllUserRole(@RequestHeader(name = "Authentication") String token) {
        List<String> userRoles = Arrays.stream(UserRole.values()).map(Enum::name).collect(Collectors.toList());
        return new ResponseEntity<>(userRoles, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/role")
    @AuthRequired({UserRole.ADMIN})
    public ResponseEntity<Map<String, String>> getUserRole(@RequestHeader(name = "Authentication") String token) throws ServletException {
        User user = userService.getUserByToken(token);
        Map<String, String> wrapper = new HashMap<>();
        wrapper.put("role", user.getUserRole().toString());
        wrapper.put("userLogin", user.getLogin());
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @ResponseBody
    @AuthRequired({UserRole.ADMIN})
    @DeleteMapping(value = "/delete")
    public ResponseEntity deleteUser(@RequestHeader(name = "Authentication") String token,
                                     @RequestParam(value = "userId") Integer userId) {
        userService.softDeleteUser(userId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @AuthRequired({UserRole.ADMIN})
    @PostMapping(value = "/createUser")
    public ResponseEntity createUser(@RequestHeader(name = "Authentication") String token,
                                     @RequestBody UserDTO userDTO) {
        User findedUserByLogin = userService.getUser(userDTO.getLogin());
        if (findedUserByLogin != null) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        User userInfo = convertService.convertUserFromDTO(userDTO);
        userInfo.setPassword(PasswordHasherUtil.encode(userInfo.getPassword()));
        userService.createUser(userInfo);
        userInfo.setPassword("");

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @ResponseBody
    @AuthRequired({UserRole.ADMIN, UserRole.USER})
    @GetMapping(value = "/userInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserInfoForm(@RequestHeader(name = "Authentication") String token) {
        User user = userService.getUserByToken(token);
        user = userService.getUser(user.getUserId());
        user.setPassword("");
        UserDTO userDTO = convertService.convertUserToDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @AuthRequired({UserRole.ADMIN})
    @PostMapping(value = "/refreshPassword")
    public ResponseEntity refreshPassword(@RequestHeader(name = "Authentication") String token,
                                          @RequestParam(value = "login") String userLogin,
                                          @RequestParam(value = "password") String userPassword) {
        User updatedUser = userService.getUser(userLogin);
        userService.clearTokensForUser(updatedUser);
        updatedUser.setPassword(PasswordHasherUtil.encode(userPassword));
        userService.updateUserInfoWithPassword(updatedUser);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @ResponseBody
    @AuthRequired({UserRole.ADMIN})
    @GetMapping(value = "/userList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserList(@RequestHeader(name = "Authentication") String token) {
        List<User> users = userService.getUserList();
        List<UserDTO> userDTOS = users
                .stream()
                .map(userFromStream -> {
                    UserDTO userDTO = convertService.convertUserToDTO(userFromStream);
                    userDTO.setPassword("");
                    return userDTO;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOS);
    }

}
