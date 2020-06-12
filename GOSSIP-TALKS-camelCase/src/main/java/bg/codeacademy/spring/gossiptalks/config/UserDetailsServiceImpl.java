package bg.codeacademy.spring.gossiptalks.config;

import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserDetailsServiceImpl implements UserDetailsService
{

  private UserService userService;

  @Autowired
  public UserDetailsServiceImpl(UserService userService)
  {
    this.userService = userService;
  }

  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
  {
    String name = "";
    List<User> users = userService.getUsers(name);

    if (users.isEmpty()) {
      // first start, create default admin user
      User admin = new User();
      admin.setUserName("admin")
          .setName("admin")
          .setEmail("aa@abv.bg")
          .setPassword(new BCryptPasswordEncoder().encode("123456"));
      userService.save(admin);
      users.add(admin);
    }
    for (bg.codeacademy.spring.gossiptalks.model.User user : users) {
      if (user.getUserName().equals(username)) {
        org.springframework.security.core.userdetails.User.UserBuilder builder =
            org.springframework.security.core.userdetails.User.withUsername(username)
                .password(user.getPassword())
                .roles("user");
        return builder.build();
      }
    }
    throw new UsernameNotFoundException("User not found.");
  }
}