/**
 * Created by kevin.huang on 2020-09-16.
 */
package foo;


import com.fasterxml.jackson.annotation.JsonSubTypes;

@JsonSubTypes.Type(value = DomesticPhone.class , name = "domestic")
public class DomesticPhone extends Phone {

  public DomesticPhone() {
  }

  @Override
  public String toString() {
    return "[DomesticPhone " + "areaCode=" + areaCode + ", local=" + local + ']';
  }

  @Override
  public void call() {

  }
}
