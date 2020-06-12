package bg.codeacademy.spring.gossiptalks.model;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class UserTest
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
  public void test_user_Null()
  {
    User user = new User();
    user.setEmail(null)
        .setUserName(null)
        .setPassword(null)
        .setName(null); // w should delete @NOTNULL at USER name!

    Set<ConstraintViolation<User>> violations = validator.validate(user);
    Assert.assertFalse(violations.isEmpty());
    Assert.assertEquals(violations.size(), 4); // must enter the count of violations expected
  }

  @Test
  public void test_user_invalid()
  {
    User user = new User();
    user
        .setUserName("user 1234 ") //@Pattern(regexp = "[a-z]+") (here lowercase more than 10 char.!)
        .setName("USER NAME !2313") //@Pattern(regexp = "[A-z]+") ( user name must be a-z+ or A-Z+)
        .setPassword(null) // Added @NotNull!!!
        .setEmail("email");//@Email(message = "Email should be valid")

    Set<ConstraintViolation<User>> violations = validator.validate(user);
    Assert.assertFalse(violations.isEmpty());
    Assert.assertEquals(violations.size(), 4);
  }


}
