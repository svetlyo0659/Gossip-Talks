package bg.codeacademy.spring.gossiptalks.model;

import bg.codeacademy.spring.gossiptalks.validation.NotHtml;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
public class Gossip
{
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer       id;
  @NotNull
  @ManyToOne(targetEntity = User.class)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User          user;
  @NotNull
  private LocalDateTime datetime;
  @Size(min = 1, max = 255)
  @NotHtml
  private String        text;


  public Gossip()
  {
    this.datetime = LocalDateTime.now();
  }


  public Integer getId()
  {
    return id;
  }

  public Gossip setId(Integer id)
  {
    this.id = id;
    return this;
  }

  public User getUser()
  {
    return user;
  }

  public Gossip setUser(User user)
  {
    this.user = user;
    return this;
  }

  public String getText()
  {
    return text;
  }


  public Gossip setText(
      String text)
  {
    this.text = text;
    return this;
  }

  public LocalDateTime getDatetime()
  {
    return datetime;
  }


  public Gossip setDatetime(LocalDateTime datetime)
  {
    this.datetime = datetime;
    return this;
  }
}
