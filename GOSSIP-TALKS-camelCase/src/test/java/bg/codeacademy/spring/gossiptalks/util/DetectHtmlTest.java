package bg.codeacademy.spring.gossiptalks.util;

import bg.codeacademy.spring.gossiptalks.util.DetectHtml;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DetectHtmlTest
{
  @DataProvider(name = "html_text")
  public Object[][] htmlTests()
  {
    return new Object[][]{
        {"Hello world", false},
        {"Contains single tag <tag/>", true},
        {"<body id=\"wpdiscuz_5.3.5\">This is a body</body>", true}
    };
  }

  @Test(dataProvider = "html_text")
  public void detectHtml(String text, boolean expectedResult)
  {
    boolean result = DetectHtml.isHtml(text);
    Assert.assertEquals(result, expectedResult);

    /**
     * <openTag></openTag>
     * <singletag/>
     * <tagWithAttributes attr1="fdf'></tagWithAttributes>
     * <singleTagWithAttr attr="alabala"/>
     */
  }
}
