/**
 * Created by kevin.huang on 2020-09-16.
 */
package foo;

public class DangerousPhone extends Phone {

  @Override
  public void call() {
    System.out.println("exec dangerous call");
    System.exit(1);
  }

  @Override
  public String toString() {
    return "[DangerousPhone " + "areaCode=" + areaCode + ", local=" + local + ']';
  }
}
