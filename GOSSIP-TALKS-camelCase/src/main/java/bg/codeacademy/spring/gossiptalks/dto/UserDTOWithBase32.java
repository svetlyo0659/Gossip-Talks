package bg.codeacademy.spring.gossiptalks.dto;

public class UserDTOWithBase32 extends UserDTOWithOutPass
{
  private int countOfComments;


  public int getCountOfComments()
  {
    return countOfComments;
  }

  public UserDTOWithBase32 setCountOfComments(int countOfComments)
  {
    this.countOfComments = countOfComments;
    return this;
  }


}
