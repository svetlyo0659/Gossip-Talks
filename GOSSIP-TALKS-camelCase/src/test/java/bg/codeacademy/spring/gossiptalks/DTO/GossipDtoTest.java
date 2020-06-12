package bg.codeacademy.spring.gossiptalks.DTO;

import bg.codeacademy.spring.gossiptalks.dto.GossipDTO;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class GossipDtoTest
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
  public void test_createInvalid_GossipDto()
  {
    GossipDTO gossipDTO = new GossipDTO();
    gossipDTO
        .setText("<address>\n" +
            "Written by <a href=\"mailto:webmaster@example.com\">Jon Doe</a>.<br>\n" +
            "Visit us at:<br>\n" +
            "Example.com<br>\n" +
            "Box 564, Disneyland<br>\n" +
            "USA\n" +
            "</address>") // min 1 symbol , and No HTML
        .setUsername("DADAD !@# DAD");// add @NotNull,@Size,@Pattern(regexp = "[a-z]+") in DTO entity
    Set<ConstraintViolation<GossipDTO>> violations = validator.validate(gossipDTO);
    Assert.assertFalse(violations.isEmpty());
    Assert.assertEquals(violations.size(), 3);
  }


}
