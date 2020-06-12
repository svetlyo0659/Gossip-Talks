package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.Main;
import bg.codeacademy.spring.gossiptalks.dto.UserRegistration;
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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Main.class)
public class GossipControllerTest extends AbstractTestNGSpringContextTests
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

  // If the user is   not following other users
  @Test
  public void test_valid_get_all_gossips()
  {
    String pageNumberAndPageSize = "?pageNo=0&pageSize=20";
    given()
        .when()
        .get("/api/v1/gossips" + pageNumberAndPageSize)
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .contentType(ContentType.JSON);

  }

  // If the user is following other users and they have posted gossips the TEST IS OK
  @Test
  public void test_valid_get_all_gossips_200()
  {


    String pageNumberAndPageSize = "?pageNo=0&pageSize=20";
    given()
        .when()
        .get("/api/v1/gossips" + pageNumberAndPageSize)
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .contentType(ContentType.JSON);
  }


  @Test
  public void test_incorrect_pageNo()
  {
    String pageNumberAndPageSize = "?pageNo=100&pageSize=20";// for now page number 100 is not reached
    given()
        .when()
        .get("/api/v1/gossips" + pageNumberAndPageSize)
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .contentType(ContentType.JSON);
  }

  @Test
  public void test_valid_get_user_gossips_200()
  {
    String pageNumberAndPageSize = "?pageNo=0&pageSize=20";
    given()
        .pathParam("username", "user3") //this user has a gossip
        .when()
        .get("/api/v1/users/{username}/gossips" + pageNumberAndPageSize)
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .contentType(ContentType.JSON);
  }

  @Test
  public void test_post_valid_gossip()
  {

    given()
        .multiPart("text", "gossip for test")
        .contentType("multipart/form-data") // "multipart/form-data"
        .when()
        .post("/api/v1/gossips")
        .then()
        .assertThat()
        .statusCode(200).and()
        .contentType(ContentType.JSON);

    User u = userService.findUserByUsername("user2").get();
    List<Gossip> gossips = gossipService.findAllByUserId(u.getId());
    Gossip g = gossips.get(gossips.size() - 1);
    gossipService.delete(g);
  }

  @Test
  public void test_post_invalid_gossip_with_html()
  {
    given()
        .multiPart("text", "<h2>HTML <mark>Marked</mark> Formatting</h2>") // HTML text not valid
        .contentType("multipart/form-data") // "multipart/form-data"
        .when()
        .post("/api/v1/gossips")
        .then()
        .assertThat()
        .statusCode(500); // no ResponseEntity for 400 / 500 not handled
  }

  @Test
  public void test_post_invalid_gossip_with_emptyString()
  {
    given()
        .multiPart("text", "") // empty String
        .contentType("multipart/form-data") // "multipart/form-data"
        .when()
        .post("/api/v1/gossips")
        .then()
        .assertThat()
        .statusCode(500); // no ResponseEntity for 400 / 500 not handled
  }

  @Test
  public void test_post_invalid_gossip_with_more255symbols()
  {
    given()
        .multiPart("text", "org.springframework.test.context.BootstrapUtils - Instantiating CacheAwareContextLoaderDelegate from class" +
            "org.springframework.test.context.BootstrapUtils - Instantiating CacheAwareContextLoaderDelegate from class" +
            "org.springframework.test.context.BootstrapUtils - Instantiating CacheAwareContextLoaderDelegate from class" +
            "org.springframework.test.context.BootstrapUtils - Instantiating CacheAwareContextLoaderDelegate from class" +
            "org.springframework.test.context.BootstrapUtils - Instantiating CacheAwareContextLoaderDelegate from class" +
            "org.springframework.test.context.BootstrapUtils - Instantiating CacheAwareContextLoaderDelegate from class" +
            "org.springframework.test.context.BootstrapUtils - Instantiating CacheAwareContextLoaderDelegate from class" +
            "org.springframework.test.context.BootstrapUtils - Instantiating CacheAwareContextLoaderDelegate from class")
        .contentType("multipart/form-data") // "multipart/form-data"
        .when()
        .post("/api/v1/gossips")
        .then()
        .assertThat()
        .statusCode(500); // no ResponseEntity for 400 / 500 not handled
  }
}
