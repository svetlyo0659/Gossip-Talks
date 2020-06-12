package bg.codeacademy.spring.gossiptalks.model;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

public class GossipTest
{
  private static ValidatorFactory validatorFactory;
  private static Validator        validator;

  @BeforeClass
  public static void createValidator()
  {
    validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
  }

  @AfterClass
  public static void close()
  {
    validatorFactory.close();
  }

  @Test
  public void test_gossip_null()
  {
    Gossip gossip = new Gossip();
    gossip
        .setUser(null)// added @NOT NULL TO USER FIELD IN GOSSIPS
        .setText(null) //  added @NotNull in Gossip Entity
        .setDatetime(null); //  added @NotNull in Gossip Entity

    Set<ConstraintViolation<Gossip>> violations = validator.validate(gossip);
    Assert.assertFalse(violations.isEmpty());
    Assert.assertEquals(violations.size(), 3);
  }

  @Test
  public void test_gossip_Invalid()
  {
    User userForTest = new User();
    Gossip gossip = new Gossip();
    gossip.setText("<TAG>one<TAG>two</TAG>one</TAG>") // @Size(min = 1, max = 255) @NotHTML
        .setUser(userForTest)
        .setDatetime(LocalDateTime.now());

    Set<ConstraintViolation<Gossip>> violations = validator.validate(gossip);
    Assert.assertFalse(violations.isEmpty());
    Assert.assertEquals(violations.size(), 1);
  }
}
