package bg.codeacademy.spring.gossiptalks.dto;

import bg.codeacademy.spring.gossiptalks.validation.NotHtml;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class GossipDTO
{
  private String        id;
  @Pattern(regexp = "^[a-z0-8\\\\.\\\\-]+$")
  @Size(min = 3, max = 10)
  @NotNull(message = "Please Enter your username")
  private String        username;
  private LocalDateTime datetime;
  @Size(min = 1, max = 255)
  @NotHtml
  private String        text;


  public String getId()
  {
    return id;
  }

  public GossipDTO setId(String id)
  {
    this.id = id;
    return this;
  }

  public String getUsername()
  {
    return username;
  }

  @Pattern(regexp = "^[a-z0-8\\\\.\\\\-]+$")
  @Size(min = 3, max = 10)
  @NotNull(message = "Please Enter your username")
  public GossipDTO setUsername(String username)
  {
    this.username = username;
    return this;
  }

  public LocalDateTime getDatetime()
  {
    return datetime;
  }

  public GossipDTO setDatetime(LocalDateTime datetime)
  {
    this.datetime = datetime;
    return this;
  }

  public String getText()
  {
    return text;
  }

  @Size(min = 1, max = 255)
  @NotHtml
  public GossipDTO setText(String text)
  {
    this.text = text;
    return this;
  }
}
