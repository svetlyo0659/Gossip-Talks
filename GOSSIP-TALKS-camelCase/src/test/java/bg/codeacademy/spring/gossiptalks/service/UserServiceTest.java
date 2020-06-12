package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.dto.UserDTOWithOutPass;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.UserRepository;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class UserServiceTest
{
  private UserRepository userRepositoryMock = mock(UserRepository.class);

  private UserService userService = new UserServiceImpl(userRepositoryMock);
  private User        u;

  {
    u = new User()
        .setName("name")
        .setUserName("asdasd");
  }


  @Test
  public void test_save_user()
  {
  User z = new User();

    userService.save(z);

    Mockito.verify(userRepositoryMock, times(1)).save(z);

  }

  @Test
  public void test_get_all_users()
  {
    List<User> users = userRepositoryMock.findAll();
    Mockito.verify(userRepositoryMock, times(1)).findAll();
  }

  @Test
  public void test_get_user_by_criteria()
  {

    userService.save(u);

    List<User> savedUser = userService.getUsers("name");
    Mockito.verify(userRepositoryMock, times(1)).findByNameContainingIgnoreCase("name");

  }

  @Test
  public void test_get_user_by_username()
  {

    Optional<User> savedUser = userService.findUserByUsername(u.getUserName());

    Mockito.verify(userRepositoryMock, times(1)).findUserByUsername(u.getUserName());
  }

  @Test
  public void test_delete_user()
  {

    userService.delete(u);

    Mockito.verify(userRepositoryMock, times(1)).delete(u);
  }

  @Test
  public void test_get_user_by_username_invalid()
  {

    Optional<User> savedUser = userService.findUserByUsername("Nyama Takuv User");

    Mockito.verify(userRepositoryMock, times(0)).findUserByUsername("Ima takuv User");
  }

}
