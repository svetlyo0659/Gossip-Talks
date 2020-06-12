package bg.codeacademy.spring.gossiptalks.model;

import javax.persistence.*;

@Entity

@Table(name = "friends", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "friend_id"})
})
public class Friends
{
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @ManyToOne(targetEntity = User.class)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;


  @ManyToOne(targetEntity = User.class)
  @JoinColumn(name = "friend_id", referencedColumnName = "id")
  private User friends;


  public int getId()
  {
    return id;
  }

  public Friends setId(int id)
  {
    this.id = id;
    return this;
  }

  public User getUser()
  {
    return user;
  }

  public Friends setUser(User user)
  {
    this.user = user;
    return this;
  }

  public User getFriends()
  {
    return friends;
  }

  public Friends setFriends(User friends)
  {
    this.friends = friends;
    return this;
  }
}
