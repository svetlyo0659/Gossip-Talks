package bg.codeacademy.spring.gossiptalks.model;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity

public class User
{
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  @Column(unique = true)
  @Email(message = "{user.email.invalid}")
  @NotNull(message = "Please Enter your email")
  private String  email;
  @Column(unique = true)
  @Pattern(regexp = "^[a-z0-8\\\\.\\\\-]+$")
  @Size(min = 3, max = 10)
  @NotNull(message = "Please Enter your username")
  private String  username;
  @Pattern(regexp = "^\\D*$")
  @NotNull(message = "Please Enter your name")
  private String  name;

  private boolean following;//default false;
  @NotNull
  private String  password;


  public Integer getId()
  {
    return id;
  }

  public User setId(Integer id)
  {
    this.id = id;
    return this;
  }

  public String getEmail()
  {
    return email;
  }


  public User setEmail(String email)
  {
    this.email = email;
    return this;
  }

  public String getUserName()
  {
    return username;
  }


  public User setUserName(String username)
  {
    this.username = username;
    return this;
  }

  public String getName()
  {
    return name;
  }


  public User setName(String name)
  {
    this.name = name;
    return this;
  }

  public boolean isFollowing()
  {
    return following;
  }

  public User setFollowing(boolean following)
  {
    this.following = following;
    return this;
  }

  public String getPassword()
  {
    return password;
  }


  public User setPassword(String password)
  {
    this.password = password;
    return this;
  }


}
