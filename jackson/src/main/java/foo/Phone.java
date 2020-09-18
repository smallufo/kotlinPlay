/**
 * Created by kevin.huang on 2020-09-16.
 */
package foo;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
  @JsonSubTypes.Type(value = InternationalPhone.class , name = "international"),
  @JsonSubTypes.Type(value = DomesticPhone.class , name = "domestic")
})
public abstract class Phone {

  public int areaCode;

  public int local;

  public abstract void call();
}
