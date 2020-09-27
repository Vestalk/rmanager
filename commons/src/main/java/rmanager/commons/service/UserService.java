package rmanager.commons.service;

import rmanager.commons.entity.User;

import javax.servlet.ServletException;
import java.util.List;

public interface UserService {

    String login(User inputUser) throws ServletException;
    boolean logout(String token);
    void updateUserInfo(User user);
    void updateUserInfoWithPassword(User user);

    User getUserByToken(String token);
    User getUser(int userId);
    User getUser(String login);

    List<User> getAllUserList();
    List<User> getUserList();

    void deleteUser(int userId);
    void createUser(User user);

    void softDeleteUser(int userId);
    void clearTokensForUser(User user);

}
