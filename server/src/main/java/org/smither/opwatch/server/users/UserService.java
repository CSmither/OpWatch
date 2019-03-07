package org.smither.opwatch.server.users;

import org.smither.opwatch.server.exceptions.InputInvalid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.UUID;

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
            Authority admin=createAuthority("ROLE_ADMIN");
            user.addAuth(admin);
            userDAO.save(user);
        }
    }

    public User createUser(String username, String password) {
        User user = new User(username);
        user.setPassword(password);
        user.setEnabled(true);
        return userDAO.save(user);
    }

    public void addauthority(UUID userId, UUID authId) {
        Optional<User> optUser = userDAO.findById(userId);
        Optional<Authority> optAuth = authDAO.findById(authId);
        if (optUser.isEmpty()) {
            throw new MissingResourceException(String.format("User %s not found", userId), User.class.getName(), userId.toString());
        }
        if (optAuth.isEmpty()) {
            throw new MissingResourceException(String.format("Authority %s not found", authId), Authority.class.getName(), authId.toString());
        }
        Authority auth = optAuth.get();
        User user = optUser.get();
        user.getAuthorities().add(optAuth.get());
        auth.getUsers().add(user);
        authDAO.save(auth);
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

    public void delAuthority(UUID userId, UUID authId) {
        Optional<User> optUser = userDAO.findById(userId);
        Optional<Authority> optAuth = authDAO.findById(authId);
        if (optUser.isEmpty() || optAuth.isEmpty()) {
            throw new MissingResourceException(String.format("User %s not found", userId), User.class.getName(), userId.toString());
        }
        User user = optUser.get();
        if (!user.getAuthorities().contains(optAuth.get())) {
            throw new InputInvalid(String.format("User %s does not have authority %s", userId, authId));
        }
        user.delAuthority(optAuth.get());
        userDAO.save(user);
    }
}
