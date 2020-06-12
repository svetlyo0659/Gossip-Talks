package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService
{

  private UserRepository userRepository;


  @Autowired
  public UserServiceImpl(UserRepository userRepository)
  {
    this.userRepository = userRepository;
  }


  public void save(User u)
  {
    userRepository.save(u);
  }


  public List<User> getUsers(String name)
  {
    return userRepository.findByNameContainingIgnoreCase(name);

  }

  public Optional<User> findUserByUsername(String username)
  {

    return userRepository.findUserByUsername(username);
  }

  public Optional<User> findByName(String name)
  {
    return userRepository.findByName(name);
  }

  @Override
  public void delete(User u)
  {
    userRepository.delete(u);
  }
}
