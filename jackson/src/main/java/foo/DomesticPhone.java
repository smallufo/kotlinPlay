/**
 * Created by kevin.huang on 2020-09-16.
 */
package foo;


//@JsonSubTypes.Type(value = DomesticPhone.class , name = "domestic")
public class DomesticPhone extends Phone {

  public DomesticPhone() {

  }

  public DomesticPhone(int areaCode , int local) {
    this.areaCode = areaCode;
    this.local = local;
  }

  @Override
  public String toString() {
    return "[DomesticPhone " + "areaCode=" + areaCode + ", local=" + local + ']';
  }

  @Override
  public void call() {

  }
}
