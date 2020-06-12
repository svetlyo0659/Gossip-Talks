package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.repository.GossipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GossipServiceImpl implements GossipService
{

  private GossipRepository gossipRepository;

  @Autowired
  public GossipServiceImpl(GossipRepository gossipRepository)
  {
    this.gossipRepository = gossipRepository;
  }

  public List<Gossip> findAllByUserId(int userId)
  {

    return gossipRepository.findAllByUserId(userId);

  }


  public void save(Gossip g)
  {
    gossipRepository.save(g);
  }


  @Override
  public List<Gossip> getAll()
  {
    return gossipRepository.findAll();
  }

  @Override
  public void delete(Gossip g)
  {
    gossipRepository.delete(g);
  }
}
