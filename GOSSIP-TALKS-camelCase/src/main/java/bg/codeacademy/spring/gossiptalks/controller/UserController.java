package bg.codeacademy.spring.gossiptalks.controller;

import java.security.Principal;
import java.util.*;


import bg.codeacademy.spring.gossiptalks.dto.GossipDTO;
import bg.codeacademy.spring.gossiptalks.dto.UserDTOWithOutPass;
import bg.codeacademy.spring.gossiptalks.dto.UserDTOWithBase32;
import bg.codeacademy.spring.gossiptalks.dto.UserRegistration;
import bg.codeacademy.spring.gossiptalks.model.Friends;
import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.service.FriendService;
import bg.codeacademy.spring.gossiptalks.service.GossipService;
import bg.codeacademy.spring.gossiptalks.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Multipart;
import retrofit2.http.Part;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;


@RestController
@RequestMapping("/api/v1/users")
public class UserController
{

  private UserService userService;

  private FriendService friendService;

  private GossipService gossipService;

  @Autowired
  public UserController(UserService userService, FriendService friendService, GossipService gossipService)
  {
    this.userService = userService;
    this.friendService = friendService;
    this.gossipService = gossipService;
  }

  @GetMapping()
  @ResponseBody
  public List<UserDTOWithOutPass> getUsers(Principal p,
                                           @RequestParam(name = "f", required = false, defaultValue = "false") boolean f,
                                           @RequestParam(name = "name", required = false, defaultValue = "*") String name,
                                           @RequestParam(value = "pageNo", defaultValue = "0", required = false) Integer pageNo,
                                           @RequestParam(value = "pageSize", defaultValue = "20", required = false) Integer pageSize)
  {
    if ("*".equals(name)) {
      name = "";
    }

    List<User> allUsers = userService.getUsers(name);
    List<UserDTOWithBase32> usersWithComments = new ArrayList<>();
    List<UserDTOWithBase32> friends = new ArrayList<>();
    for (User u : allUsers) {
      User me = userService.findUserByUsername(p.getName()).get();
      List<Gossip> commentOfUser = gossipService.findAllByUserId(u.getId());
      UserDTOWithBase32 u1 = new UserDTOWithBase32();
      u1.setCountOfComments(commentOfUser.size())
          .setEmail(u.getEmail())
          .setName(u.getName())
          .setUsername(u.getUserName());
      Optional<Friends> f1 = friendService.findFriendsByUserIdAndFriendId(me.getId(), u.getId());
      if (f1.isPresent()) {
        u1.setFollowing(true);
        friends.add(u1);
      }
      else {
        u1.setFollowing(false);
      }
      usersWithComments.add(u1);
    }

    if (!f) {
      Collections.sort(usersWithComments, (a, b) -> Integer.compare(b.getCountOfComments(), a.getCountOfComments()));
      List<UserDTOWithOutPass> userWithCommentsWithoutComments = getUsersWithOutPass(usersWithComments);
      return userWithCommentsWithoutComments;
    }
    else {
      Collections.sort(friends, (a, b) -> Integer.compare(b.getCountOfComments(), a.getCountOfComments()));
      List<UserDTOWithOutPass> friendsWithCommentsWithoutComments = getUsersWithOutPass(friends);
      return friendsWithCommentsWithoutComments;
    }
  }

  @Multipart
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<User> createUser(@RequestParam(name = "email") String email,
                                         @Valid @RequestParam(name = "username") String username,
                                         @Valid @RequestParam(name = "name", required = false, defaultValue = "") String name,
                                         @RequestParam(name = "following", required = false, defaultValue = "false") boolean following,
                                         @Valid @RequestParam(name = "password") String password,
                                         @Valid @RequestParam(name = "passwordConfirmation") String passwordConfirmation)
  {
    Optional<User> uFromDb = userService.findUserByUsername(username);

    if (!uFromDb.isPresent() && password.equals(passwordConfirmation)) {
      User u2 = new User()
          .setEmail(email)
          .setPassword(new BCryptPasswordEncoder().encode(password))
          .setUserName(username)
          .setName(name)
          .setFollowing(following);
      userService.save(u2);
      return ResponseEntity.ok().build();
    }
    else {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/me")
  @ResponseBody
  public ResponseEntity<UserDTOWithOutPass> getCurrentUser(Principal p) //should be a Principal
  {
    Optional<User> u = userService.findUserByUsername(p.getName());// not sure to check if exists ????
    User u2 = u.get();
    UserDTOWithOutPass u1 = new UserDTOWithOutPass();
    u1.setEmail(u2.getEmail())
        .setName(u2.getName())
        .setUsername(u2.getUserName());

    return ResponseEntity.ok(u1);
  }

  @Multipart
  @PostMapping("/me")
  public ResponseEntity<UserDTOWithOutPass> changeCurrentUserPassword(@Part("oldPassword") String oldPassword,
                                                                      @Valid @Part("password") String password,
                                                                      @Valid @Part("passwordConfirmation") String passwordConfirmation,
                                                                      Principal p)

  {
    Optional<User> u = userService.findUserByUsername(p.getName());

    if (u.isPresent()) {
      User u2 = u.get();
      if (new BCryptPasswordEncoder().matches(oldPassword, u2.getPassword())) {
        if (password.equals(passwordConfirmation)) {
          u2.setPassword(new BCryptPasswordEncoder().encode(password));
          userService.save(u2);
          UserDTOWithOutPass u1 = new UserDTOWithOutPass();
          u1.setFollowing(u2.isFollowing())
              .setEmail(u2.getEmail())
              .setName(u2.getName())
              .setUsername(u2.getUserName());
          return ResponseEntity.ok(u1);
        }
      }
    }
    return ResponseEntity.badRequest().build();
  }

  @Multipart
  @PostMapping("/{username}/follow")
  public ResponseEntity<UserDTOWithOutPass> followUser(@PathVariable("username") String username,
                                                       Principal p,
                                                       @Part boolean follow)
  {
    Optional<User> u = userService.findUserByUsername(p.getName());// IT SME
    Optional<User> availableUser = userService.findUserByUsername(username);
    if (!u.isPresent() || !availableUser.isPresent()) {
      return ResponseEntity.notFound().build();
    }
    User me = u.get();
    User friend = availableUser.get();
    if (me.getId() == friend.getId()) {
      return ResponseEntity.status(HttpStatus.valueOf("You cannot follow self ")).build();
    }
    UserDTOWithOutPass friendDTO = new UserDTOWithOutPass()
        .setFollowing(friend.isFollowing())
        .setUsername(friend.getUserName())
        .setEmail(friend.getEmail())
        .setName(friend.getName());

    Optional<Friends> f = friendService.findFriendsByUserIdAndFriendId(me.getId(), friend.getId());
    if (f.isPresent()) {
      if (!follow) {
        friendService.remove(f.get());
      }
      else {
        return ResponseEntity.badRequest().build();
      }
      List<Friends> u1 = friendService.findFriendsByUserId(me.getId());
      if (u1.size() == 0) {
        me.setFollowing(false);
      }
    }
    else {
      if (follow) {
        friend.setFollowing(true);
        Friends f1 = new Friends();
        f1.setUser(me);
        f1.setFriends(friend);
        friendService.save(f1);
      }
      else {
        return ResponseEntity.badRequest().build();
      }
    }
    userService.save(friend);
    return ResponseEntity.ok(friendDTO);
  }

  @GetMapping("/{username}/gossips")
  @ResponseBody
  ResponseEntity<List<GossipDTO>> getUserGossips(@PathVariable String username,
                                                 @RequestParam(value = "pageNo", defaultValue = "0", required = false) @PositiveOrZero Integer pageNo,
                                                 @RequestParam(value = "pageSize", defaultValue = "20", required = false) @PositiveOrZero Integer pageSize)
  {
    Optional<User> u = userService.findUserByUsername(username);
    if (u.isPresent()) {
      User u2 = u.get();
      List<Gossip> b = gossipService.findAllByUserId(u2.getId());
      List<GossipDTO> gossipDTOS = new ArrayList<>();
      for (Gossip g : b) {
        GossipDTO gossipDTO = new GossipDTO();
        gossipDTO.setId(Integer.toString(g.getId(), 32))
            .setText(g.getText())
            .setDatetime(g.getDatetime())
            .setUsername(g.getUser().getUserName());
        gossipDTOS.add(gossipDTO);
      }
      //sort by date
      Collections.sort(gossipDTOS, (a, z) -> z.getDatetime().compareTo(a.getDatetime()));

      if (checkPages(pageNo, pageSize, gossipDTOS) || gossipDTOS.size() == 0) {
        return ResponseEntity.ok(new ArrayList<>());
      }

      //list with pages
      List<GossipDTO> returnedList = (List<GossipDTO>) listPaged(pageNo, pageSize, gossipDTOS);
      return ResponseEntity.ok(returnedList);
    }
    return ResponseEntity.ok(new ArrayList<>());
  }

  private List<?> listPaged(int pageNo, int pageSize, List list)
  {
    int start = pageNo * pageSize;
    int end = Math.min(start + pageSize, list.size());

    return list.subList(start, end);
  }

  private boolean checkPages(int pageNo, int pageSize, List list)
  {
    return pageNo * pageSize > list.size() - 1;
  }


  public Optional<User> getUser(String u)
  {
    return userService.findByName(u);
  }

  private List<UserDTOWithOutPass> getUsersWithOutPass(List<UserDTOWithBase32> u)
  {
    List<UserDTOWithOutPass> users = new ArrayList<>();
    for (UserDTOWithBase32 u1 : u) {
      UserDTOWithOutPass u2 = new UserDTOWithOutPass()
          .setUsername(u1.getUsername())
          .setName(u1.getName())
          .setEmail(u1.getEmail())
          .setFollowing(u1.isFollowing());
      users.add(u2);

    }
    return users;
  }
}
