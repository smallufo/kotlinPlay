/**
 * Created by kevin.huang on 2019-12-11.
 */

/**
 * https://www.slideshare.net/DavidBuck7/java-concurrency-another-peek-under-the-hood-code-one-2019
 */
public class LongValueTest {

  static long val = 0;

  public static void main(String[] args) {
    new Thread( () -> { while(true) val = -1L; }).start();
    new Thread( () -> { while(true) val = 0L; }).start();

    while(true) {
      long temp = val;
      if (temp != -1 && temp != 0) {
        System.out.println("temp (DEC) : " + temp);
        System.out.println("temp (BIN) : " + Long.toBinaryString(temp));
        System.exit(-1);
      }
    }
  }
}
