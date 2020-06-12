package bg.codeacademy.spring.gossiptalks.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public class GossipList
{
  private int             numberOfElements;
  private int             totalElements;
  private List<GossipDTO> content;

  public int getNumberOfElements()
  {
    return numberOfElements;
  }

  public GossipList setNumberOfElements(int numberOfElements)
  {
    this.numberOfElements = numberOfElements;
    return this;
  }

  public int getTotalElements()
  {
    return totalElements;
  }

  public GossipList setTotalElements(int totalElements)
  {
    this.totalElements = totalElements;
    return this;
  }

  public List<GossipDTO> getContent()
  {
    return content;
  }

  public GossipList setContent(List<GossipDTO> content)
  {
    this.content = content;
    setNumberOfElements(content.size());
    return this;
  }
}
