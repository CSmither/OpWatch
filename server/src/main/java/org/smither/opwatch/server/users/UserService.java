package org.smither.opwatch.server.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

  private UserDAO userDAO;

  @Autowired
  UserService( UserDAO userDAO ) {
    this.userDAO = userDAO;
    if (userDAO.count() == 0) {
      User user=new User("admin");
      user.setPassword("admin");
      user.setEnabled(true);
      userDAO.save(user);
    }
  }

  public User createUser( String username, String password ) {
    User user=new User(username);
    user.setPassword(password);
    user.setEnabled(true);
    return userDAO.save(user);
  }

  public Optional<User> findById( UUID id ) {
    return userDAO.findById(id);
  }

  public List<User> findByUsername(String username ) {
    return userDAO.findByUsername(username);
  }

}
