package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.User;


import java.util.List;
import java.util.Optional;


public interface UserService
{
  void save(User user);

  List<User> getUsers(String name);

  Optional<User> findUserByUsername(String username);

  Optional<User> findByName(String name);

  void delete(User u);


}
