package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.Friends;
import bg.codeacademy.spring.gossiptalks.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface FriendService
{
  List<Friends> findFriendsByUserId(Integer userId);

  void save(Friends f);

  Optional<Friends> findFriendsByUserIdAndFriendId(Integer userId, Integer friendsId);

  void remove(Friends f);

}
