import mu.KotlinLogging
import org.junit.Test

/**
 * Created by kevin.huang on 2020-01-03.
 */
class HelloTest {
  private val logger = KotlinLogging.logger {}

  @Test
  fun testLogger1a() {
    logger.error(String.format("This is error , message1 = [%s]", "1"), IllegalArgumentException("some error"))
  }

  @Test
  fun testLogger1b() {
    logger.error("This is error , message1 = [{}]", "1", IllegalArgumentException("some error"))
  }

  @Test
  fun testLogger2a() {
    logger.error(
      String.format("This is error , msg1 = [%s] , msg2 = [%s]", "1", "2"),
      IllegalArgumentException("some error")
                )
  }

  @Test
  fun testLogger2b() {
    logger.error("This is error , msg1 = [{}] , msg2 = [{}]", "1", "2", IllegalArgumentException("some error"))
  }

  @Test
  fun testLogger3a() {
    logger.error(
      String.format(
        "This is error , msg1 = [%s] , msg2 = [%s] , exception = %s",
        "1",
        "2",
        IllegalArgumentException("some error")
                   )
                )
  }

  @Test
  fun testLogger3b() {
    logger.error(
      "This is error , msg1 = [{}] , msg2 = [{}] , exception = {}",
      "1",
      "2",
      IllegalArgumentException("some error")
                )
  } //  @Test
  //  public void testLoggerJustException1() {
  //    logger.error("This is exception {}" , new IllegalArgumentException("some error"));
  //  }
  //
  //  @Test
  //  public void testLoggerJustException2() {
  //    logger.error("This is exception" , new IllegalArgumentException("some error"));
  //  }
}