package bg.codeacademy.spring.gossiptalks.service;

import bg.codeacademy.spring.gossiptalks.model.Gossip;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


public interface GossipService
{
  List<Gossip> findAllByUserId(int userId);

  void save(Gossip g);

  List<Gossip> getAll();

  void delete(Gossip g);


}
