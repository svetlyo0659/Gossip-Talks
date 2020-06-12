package bg.codeacademy.spring.gossiptalks.DTO;

import bg.codeacademy.spring.gossiptalks.dto.UserDTOWithBase32;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class UserDTOWithBase32Test
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
  public void test_Invalid_UserDTO()
  {
    UserDTOWithBase32 user = new UserDTOWithBase32();

    // added @NotNull , @Email and so on to UserDTOWithBase32 Entity!
    user
        .setName("AAD!QQ @!@@@@")
        .setUsername("USERNAME123")
        .setEmail("email@test.ru");


    Set<ConstraintViolation<UserDTOWithBase32>> violations = validator.validate(user);
    Assert.assertFalse(violations.isEmpty());
    Assert.assertEquals(violations.size(), 2);

  }
}
