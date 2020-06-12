package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.model.User;
import bg.codeacademy.spring.gossiptalks.repository.GossipRepository;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class GossipServiceTest
{

  private GossipRepository gossipRepositoryMock = mock(GossipRepository.class);
  private GossipService    gossipService        = new GossipServiceImpl(gossipRepositoryMock);
  private User             u;
  private Gossip           g;

  {
    u = new User()
        .setId(1);
    g = new Gossip()
        .setUser(u)
        .setId(1)
        .setDatetime(LocalDateTime.now())
        .setText("This is a test gossip!");
  }

  @Test
  public void test_getAll_gossips_by_userId()
  {

    List<Gossip> allUserGossips = gossipService.findAllByUserId(u.getId());

    Mockito.verify(gossipRepositoryMock, times(1)).findAllByUserId(u.getId());
  }

  @Test
  public void test_save_a_gossip()
  {


    gossipService.save(g);

    Mockito.verify(gossipRepositoryMock, times(1)).save(g);

  }

  @Test
  public void test_get_all_gossips()
  {
    Gossip gossip1 = new Gossip();
    Gossip gossip2 = new Gossip();
    gossipService.save(gossip1);
    gossipService.save(gossip2);

    List<Gossip> allGossips = gossipService.getAll();

    Mockito.verify(gossipRepositoryMock, times(1)).findAll();
  }


}

