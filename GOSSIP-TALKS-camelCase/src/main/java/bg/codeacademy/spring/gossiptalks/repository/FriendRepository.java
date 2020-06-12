package bg.codeacademy.spring.gossiptalks.repository;

import bg.codeacademy.spring.gossiptalks.model.Friends;
import bg.codeacademy.spring.gossiptalks.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "friends", path = "friends")
public interface FriendRepository extends JpaRepository<Friends, Integer>
{

  List<Friends> findFriendsByUserId(Integer userId);

  Optional<Friends> findFriendByUserIdAndFriendsId(Integer userId, Integer friendsId);


}
