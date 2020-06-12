package bg.codeacademy.spring.gossiptalks.controller;


import bg.codeacademy.spring.gossiptalks.Main;
import bg.codeacademy.spring.gossiptalks.model.Friends;
import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.service.FriendService;
import bg.codeacademy.spring.gossiptalks.service.GossipService;
import bg.codeacademy.spring.gossiptalks.service.UserService;
import io.restassured.RestAssured;
import io.restassured.authentication.BasicAuthScheme;
import io.restassured.http.ContentType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.*;

import java.util.Optional;

import static io.restassured.RestAssured.given;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Main.class)
// Extend AbstractTestNGSpringContextTests to use estNG tests in Spring Boot
public class UserControllerTest extends AbstractTestNGSpringContextTests
{

  @LocalServerPort
  private int port;
  @Autowired
  private GossipService gossipService;
  @Autowired
  private UserService   userService;
  @Autowired
  private FriendService friendService;

  private User    u2;
  private User    u3;
  private Friends f;
  private Gossip  g;

  @BeforeClass(alwaysRun = true, dependsOnMethods = "springTestContextPrepareTestInstance")
  protected void setupRestAssured()
  {
    RestAssured.port = port;

  }

  @BeforeMethod
  private void createData()
  {
    u2 = new User()
        .setUserName("user2")
        .setPassword(new BCryptPasswordEncoder().encode("654321"))
        .setEmail("a@a.a")
        .setName("u");
    userService.save(u2);

    u3 = new User()
        .setUserName("user3")
        .setPassword(new BCryptPasswordEncoder().encode("654321"))
        .setEmail("a3@a.a")
        .setName("uuu");
    userService.save(u3);

    g = new Gossip()
        .setText("aaaaa")
        .setUser(u3);
    gossipService.save(g);

    f = new Friends();
    f.setUser(u2);
    f.setFriends(u3);
    friendService.save(f);

    BasicAuthScheme b = new BasicAuthScheme();
    b.setUserName("user2");
    b.setPassword("654321");
    RestAssured.authentication = b;
  }

  @AfterMethod
  private void deleteData()
  {
    gossipService.delete(g);
    friendService.remove(f);
    userService.delete(u3);
    userService.delete(u2);


  }


  @Test
  public void get_all_users_request_verify_200()
  {

    given().when().get("/api/v1/users").then().assertThat().statusCode(200).and().
        contentType(ContentType.JSON);


  }

  @Test// without authorization
  public void test_create_user()
  {

    given()
        .multiPart("password", "123456")
        .multiPart("username", "username")
        .multiPart("name", "User")
        .multiPart("email", "user@abv.bg")
        .multiPart("passwordConfirmation", "123456")
        .contentType("multipart/form-data") // "multipart/form-data"
        .when()
        .post("/api/v1/users")
        .then()
        .assertThat()
        .statusCode(200);
    Optional<User> u = userService.findUserByUsername("username");
    userService.delete(u.get());

  }

  @Test
  public void test_create__invalid_username_user()
  {
    given()
        .multiPart("password", "123456")
        .multiPart("username", "kiro.kirov.petrov") // more ten symbols
        .multiPart("name", "User")
        .multiPart("email", "user@abv.bg")
        .multiPart("passwordConfirmation", "123456")
        .contentType("multipart/form-data") // "multipart/form-data"
        .when()
        .post("/api/v1/users")
        .then()
        .assertThat()
        .statusCode(500);
  }

  @Test
  public void test_create__invalid_email_user()
  {
    RestAssured
        .given()
        .multiPart("password", "123456")
        .multiPart("username", "kiro.1")
        .multiPart("name", "User")
        .multiPart("email", "11") // invalid email
        .multiPart("passwordConfirmation", "123456")
        .contentType("multipart/form-data") // "multipart/form-data"
        .when()
        .post("/api/v1/users")
        .then()
        .assertThat()
        .statusCode(500);


  }


  @Test
  public void test_change_password()
  {
    RestAssured
        .given()
        .multiPart("password", "usereeee987")
        .multiPart("oldPassword", "654321")
        .multiPart("passwordConfirmation", "usereeee987")
        .contentType("multipart/form-data") // "multipart/form-data"
        .when()
        .post("/api/v1/users/me")
        .then()
        .assertThat()
        .statusCode(200).and().contentType(ContentType.JSON);


  }

  @Test
  public void test_change_incorrect_password()
  {
    RestAssured
        .given()
        .multiPart("password", "123456")
        .multiPart("oldPassword", "111111") // incorrect password
        .multiPart("passwordConfirmation", "123456")
        .contentType("multipart/form-data") // "multipart/form-data"
        .when()
        .post("/api/v1/users/me")
        .then()
        .assertThat()
        .statusCode(400);
  }

  @Test
  public void test_change_incorrect_password_confirmation()
  {
    RestAssured
        .given()
        .multiPart("password", "123456")
        .multiPart("oldPassword", "654321")
        .multiPart("passwordConfirmation", "1234566") // incorrect
        .contentType("multipart/form-data") // "multipart/form-data"
        .when()
        .post("/api/v1/users/me")
        .then()
        .assertThat()
        .statusCode(400);
  }

  @Test
  public void test_get_information_about_me()
  {
    RestAssured
        .given()
        .when()
        .get("/api/v1/users/me")
        .then()
        .assertThat()
        .statusCode(200).and().contentType(ContentType.JSON);
  }

  @Test
  public void test_follow_user()
  {
    User user4 = new User()
        .setUserName("user4")
        .setPassword(new BCryptPasswordEncoder().encode("35435400"))
        .setName("")
        .setEmail("user4@ht.de");
    userService.save(user4);
    RestAssured
        .given()
        .pathParam("username", "user4")
        .multiPart("follow", "true")
        .contentType("multipart/form-data") // "multipart/form-data"
        .when()
        .post("/api/v1/users/{username}/follow")
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .contentType(ContentType.JSON);

    Friends f = friendService.findFriendsByUserIdAndFriendId(u2.getId(), user4.getId()).get();
    friendService.remove(f);
    userService.delete(user4);
  }

  @Test
  public void test_un_follow_user()
  {
    User user4 = new User()
        .setUserName("user4")
        .setPassword(new BCryptPasswordEncoder().encode("35435400"))
        .setName("")
        .setEmail("user4@ht.de");
    userService.save(user4);

    RestAssured
        .given()
        .pathParam("username", "user4")
        .multiPart("follow", "false") // user4 is not following a.k.a already not subscribed.
        .contentType("multipart/form-data") // "multipart/form-data"
        .when()
        .post("/api/v1/users/{username}/follow")
        .then()
        .assertThat()
        .statusCode(400);
    userService.delete(user4);
  }
}
