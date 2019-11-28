/**
 * Created by kevin.huang on 2019-11-27.
 */

import org.junit.Test;

import static org.junit.Assert.*;

public class AlphaBeanNameTest {

  @Test
  public void testName() {
    AlphaBean bean = new AlphaBean();
    assertEquals("Kevin" , bean.getName());
  }
}