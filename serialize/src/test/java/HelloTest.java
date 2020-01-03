import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kevin.huang on 2020-01-03.
 */

public class HelloTest {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Test
  public void testLogger1a() {
    logger.error(String.format("This is error , message1 = [%s]" , "1") , new IllegalArgumentException("some error"));
  }

  @Test
  public void testLogger1b() {
    logger.error("This is error , message1 = [{}]" , "1" , new IllegalArgumentException("some error"));
  }

  @Test
  public void testLogger2a() {
    logger.error(String.format("This is error , msg1 = [%s] , msg2 = [%s]" , "1" , "2") , new IllegalArgumentException("some error"));
  }

  @Test
  public void testLogger2b() {
    logger.error("This is error , msg1 = [{}] , msg2 = [{}]" , "1"  , "2", new IllegalArgumentException("some error"));
  }


  @Test
  public void testLogger3a() {
    logger.error(String.format("This is error , msg1 = [%s] , msg2 = [%s] , exception = %s" , "1" , "2" , new IllegalArgumentException("some error")) );
  }

  @Test
  public void testLogger3b() {
    logger.error("This is error , msg1 = [{}] , msg2 = [{}] , exception = {}" , "1"  , "2", new IllegalArgumentException("some error"));
  }









//  @Test
//  public void testLoggerJustException1() {
//    logger.error("This is exception {}" , new IllegalArgumentException("some error"));
//  }
//
//  @Test
//  public void testLoggerJustException2() {
//    logger.error("This is exception" , new IllegalArgumentException("some error"));
//  }



}
