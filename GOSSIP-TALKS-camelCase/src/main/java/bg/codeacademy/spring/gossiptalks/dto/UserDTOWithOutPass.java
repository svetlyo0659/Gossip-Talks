package bg.codeacademy.spring.gossiptalks.dto;


import javax.validation.constraints.*;

public class UserDTOWithOutPass
{


  @Email(message = "{user.email.invalid}")
  @NotNull(message = "Please Enter your email")
  private String  email;

  @Pattern(regexp = "^[a-z0-8\\\\.\\\\-]+$")
  @Size(min = 3, max = 10)
  @NotNull(message = "Please Enter your username")
  private String  username;

  @NotNull(message = "Please Enter your name")
  @Pattern(regexp = "^\\D*$")
  private String  name;

  private boolean following;

  public String getEmail()
  {
    return email;
  }

  public UserDTOWithOutPass setEmail(String email)
  {
    this.email = email;
    return this;
  }

  public String getUsername()
  {
    return username;
  }


  public UserDTOWithOutPass setUsername(String username)
  {
    this.username = username;
    return this;
  }

  public String getName()
  {
    return name;
  }


  public UserDTOWithOutPass setName(String name)
  {
    this.name = name;
    return this;
  }

  public boolean isFollowing()
  {
    return following;
  }

  public UserDTOWithOutPass setFollowing(boolean following)
  {
    this.following = following;
    return this;
  }
}
