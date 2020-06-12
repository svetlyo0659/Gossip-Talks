package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.Friends;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.FriendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendServiceImpl implements FriendService
{

  private FriendRepository friendRepository;

  @Autowired
  public FriendServiceImpl(FriendRepository friendRepository)
  {
    this.friendRepository = friendRepository;
  }

  public List<Friends> findFriendsByUserId(Integer userId)
  {
    return friendRepository.findFriendsByUserId(userId);

  }

  public void save(Friends f)
  {
    friendRepository.save(f);


  }

  public Optional<Friends> findFriendsByUserIdAndFriendId(Integer userId, Integer friendsId)
  {
    return friendRepository.findFriendByUserIdAndFriendsId(userId, friendsId);

  }

  public void remove(Friends f)
  {
    friendRepository.delete(f);
  }


}
