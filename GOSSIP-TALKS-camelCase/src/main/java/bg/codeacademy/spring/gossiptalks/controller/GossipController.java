package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.dto.GossipDTO;
import bg.codeacademy.spring.gossiptalks.dto.GossipList;
import bg.codeacademy.spring.gossiptalks.model.Friends;
import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.service.FriendService;
import bg.codeacademy.spring.gossiptalks.service.GossipService;
import bg.codeacademy.spring.gossiptalks.service.UserService;
import okhttp3.MultipartBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Multipart;
import retrofit2.http.Part;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api/v1/gossips")
public class GossipController
{
  private UserService   userService;
  private GossipService gossipService;
  private FriendService friendService;

  @Autowired
  public GossipController(UserService userService, GossipService gossipService, FriendService friendService)
  {
    this.userService = userService;
    this.gossipService = gossipService;
    this.friendService = friendService;
  }

  @GetMapping()
  @ResponseBody
  public ResponseEntity<GossipList> getGossips(Principal p,
                                               @RequestParam(value = "pageNo", defaultValue = "0", required = false) @PositiveOrZero Integer pageNo,
                                               @RequestParam(value = "pageSize", defaultValue = "20", required = false) @PositiveOrZero Integer pageSize)
  {
    User u = userService.findUserByUsername(p.getName()).get();
// load friends gossips
    List<Friends> friends = friendService.findFriendsByUserId(u.getId());
    List<GossipDTO> gossips = new ArrayList<>();
    for (Friends f : friends) {
      List<Gossip> g = gossipService.findAllByUserId(f.getFriends().getId());
      for (Gossip g1 : g) {
        GossipDTO gt = new GossipDTO();
        gt.setText(g1.getText())
            .setId(Integer.toString(g1.getId(), 32))
            .setDatetime(g1.getDatetime())
            .setUsername(g1.getUser().getUserName());
        gossips.add(gt);

      }

    }
    // sort gossips by time
    Comparator<GossipDTO> compByDate = (a, b) -> b.getDatetime().compareTo(a.getDatetime());
    gossips.sort(compByDate);


// build page
    int start = pageNo * pageSize; //2*10
    int end = Math.min(start + pageSize, gossips.size()); // 19
    GossipList g = new GossipList()
        .setTotalElements(gossips.size());
    if (checkPages(pageNo, pageSize, gossips) || gossips.size() == 0) {
      g.setContent(new ArrayList<>());
    }
    else {
      g.setContent(gossips.subList(start, end));

    }

    return ResponseEntity.ok(g);
  }

  @Multipart
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<GossipDTO> postGossip(@Valid @Part String text, Principal username)
  {
    Optional<String> contentz = Optional.ofNullable(text);
    if (contentz.isPresent()) {
      Gossip g = new Gossip();
      User u = userService.findUserByUsername(username.getName()).get();
      g.setUser(u);
      g.setText(text);
      gossipService.save(g);
      GossipDTO g1 = new GossipDTO();
      g1.setUsername(g.getUser().getUserName())
          .setDatetime(g.getDatetime())
          .setId(Integer.toString(g.getId(), 32))
          .setText(g.getText());
      return ResponseEntity.ok(g1);
    }

    return ResponseEntity.notFound().build();
  }


  private boolean checkPages(int pageNo, int pageSize, List list)
  {
    return pageNo * pageSize > list.size() - 1;
  }

}
