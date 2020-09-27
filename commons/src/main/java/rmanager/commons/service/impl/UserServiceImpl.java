package rmanager.commons.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rmanager.commons.entity.User;
import rmanager.commons.entity.UserToken;
import rmanager.commons.repository.UserRepository;
import rmanager.commons.repository.UserTokenRepository;
import rmanager.commons.service.UserService;
import rmanager.commons.util.PasswordHasherUtil;

import javax.servlet.ServletException;
import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final String BEARER = "Bearer";
    private Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;
    private UserTokenRepository userTokenRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserTokenRepository userTokenRepository) {
        this.userRepository = userRepository;
        this.userTokenRepository = userTokenRepository;
    }

    @Override
    @Transactional
    public String login(User inputUser) throws ServletException {
        User existedUser = userRepository.getUserByLogin(inputUser.getLogin()).orElse(null);
        try {
            if (existedUser == null) {
                throw new ServletException("User Not Found : " + inputUser.getLogin());
            }

            if (existedUser.getDeleted() != null && existedUser.getDeleted().equals(true)) {
                throw new ServletException("User deleted : " + inputUser.getLogin());
            }

            String password = inputUser.getPassword();
            if (PasswordHasherUtil.matchPasswords(existedUser.getPassword(), password)) {
                return createToken(existedUser).getToken();
            }
            logger.info(inputUser.getLogin() + " entered wrong password");

        } catch (ServletException e) {
            logger.info(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean logout(String token) {
        boolean result = false;
        token = token.substring(BEARER.length());
        UserToken userToken = userTokenRepository.getUserTokenByToken(token);
        if (userToken != null) {
            deleteToken(userToken);
            result = true;
        }
        return result;
    }

    @Override
    public void updateUserInfo(User user) {
        userRepository.findById(user.getUserId()).ifPresent(selectedUser -> {
            String userPass = selectedUser.getPassword();
            user.setPassword(userPass);
            userRepository.saveAndFlush(user);
        });
    }

    @Override
    public void updateUserInfoWithPassword(User user) {
        userRepository.saveAndFlush(user);
    }

    @Override
    public User getUserByToken(String token) {
        User user = null;
        UserToken userToken = getUserTokenByToken(token);
        if (userToken != null) {
            user = userToken.getUser();
        }
        return user;
    }

    private UserToken getUserTokenByToken(String token) {
        token = getTokenValue(token);
        if (token != null) {
            return userTokenRepository.getUserTokenByToken(token);
        }
        return null;
    }

    private String getTokenValue(String token) {
        if (token != null) {
            token = token.substring(BEARER.length()).trim();
            if (!token.contains("null") && !token.equals("")) {
                return token;
            }
        }
        return null;
    }

    @Override
    public User getUser(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User getUser(String login) {
        return userRepository.getUserByLogin(login).orElse(null);
    }

    @Override
    public List<User> getAllUserList() {
        return userRepository.findAll()
                .stream()
                .map(user -> {
                    user.setPassword("");
                    return user;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getUserList() {
        return userRepository.findAllNotDeleted()
                .stream()
                .map(user -> {
                    user.setPassword("");
                    return user;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(int userId) {
        userRepository
                .findById(userId)
                .ifPresent(user -> userRepository.delete(user));
    }

    @Override
    public void createUser(User user) {
        userRepository.saveAndFlush(user);
    }

    @Override
    public void softDeleteUser(int userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setDeleted(true);
            userRepository.saveAndFlush(user);
        });
    }

    @Override
    public void clearTokensForUser(User user) {
        if (user != null) {
            userTokenRepository.clearTokensForUser(user.getUserId());
        }
    }


    private UserToken createToken(User user) {
        Key key = MacProvider.generateKey();
        String jwt = Jwts.builder()
                .setSubject(user.getLogin())
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        UserToken userToken = new UserToken(user);
        userToken.setToken(jwt);
        userTokenRepository.saveAndFlush(userToken);
        return userToken;
    }

    private void deleteToken(UserToken userToken) {
        userTokenRepository.delete(userToken);
    }
}
