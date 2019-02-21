package org.smither.opwatch.server.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private UserDAO userDAO;
    private AuthorityDAO authDAO;

    @Autowired
    UserService(UserDAO userDAO, AuthorityDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        if (userDAO.count() == 0) {
            User user = new User("admin");
            user.setPassword("admin");
            user.setEnabled(true);
            userDAO.save(user);
        }
    }

    public User createUser(String username, String password) {
        User user = new User(username);
        user.setPassword(password);
        user.setEnabled(true);
        return userDAO.save(user);
    }

    public void addauthority(UUID userID, UUID authority) {
        Optional<User> optUser = userDAO.findById(userID);
        Optional<Authority> optAuth = authDAO.findById(authority);
        if (optUser.isEmpty() || optAuth.isEmpty()) {
            throw new MissingResourceException(String.format("User %s not found", userID), User.class.getName(), userID.toString());
        }
        User user = optUser.get();
        user.addAuthority(optAuth.get());
        userDAO.save(user);
    }

    public Authority createAuthority(String authority) {
        return authDAO.save(new Authority(authority));
    }

    public Optional<User> findById(UUID id) {
        return userDAO.findById(id);
    }

    public List<User> findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

}
