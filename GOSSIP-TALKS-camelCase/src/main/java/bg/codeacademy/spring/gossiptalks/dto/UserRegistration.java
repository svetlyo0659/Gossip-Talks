package bg.codeacademy.spring.gossiptalks.dto;

import bg.codeacademy.spring.gossiptalks.model.User;

import javax.validation.constraints.NotNull;



public class UserRegistration extends User
{
  @NotNull
  private String passwordConfirmation;

  public String getPasswordConfirmation()
  {
    return passwordConfirmation;
  }


  public UserRegistration setPasswordConfirmation(String passwordConfirmation)
  {
    this.passwordConfirmation = passwordConfirmation;
    return this;
  }
}
