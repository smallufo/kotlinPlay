/**
 * Created by kevin.huang on 2020-09-16.
 */
package foo;

//@JsonSubTypes.Type(value = InternationalPhone.class , name = "international")
public class InternationalPhone extends Phone {

  private int countryCode;

  public InternationalPhone() {

  }

  public InternationalPhone(int countryCode, int areaCode, int local) {
    this.countryCode = countryCode;
    this.areaCode = areaCode;
    this.local = local;
  }

  public int getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(int countryCode) {
    this.countryCode = countryCode;
  }

  @Override
  public String toString() {
    return "[InternationalPhone " + "countryCode=" + countryCode + ", areaCode=" + areaCode + ", local=" + local + ']';
  }

  @Override
  public void call() {

  }
}
