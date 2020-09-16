import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import foo.InternationalPhone;
import foo.User;
import org.junit.Test;

import java.io.IOException;

public class UserSerializerTest {

  @Test
  public void testSerialize() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    User p = new User("john", 18);
    p.phone = new InternationalPhone(886, 2, 1234567);

    String json = objectMapper.writeValueAsString(p);
    System.out.println("json = " + json);
  }

  @Test
  public void testDeserialize() throws JsonProcessingException {
    String json = "{\"name\":\"john\",\"age\":18,\"phone\":{\"@class\":\"foo.InternationalPhone\",\"areaCode\":2,\"local\":1234567,\"countryCode\":886}}";

    ObjectMapper objectMapper = new ObjectMapper();
    User u = objectMapper.readValue(json, User.class);

    System.out.println("deserialized user = " + u);
  }

  @Test
  public void testDeserializeDangerous() throws Exception {
    String json = "{\"name\":\"john\",\"age\":18,\"phone\":{\"@class\":\"foo.DangerousPhone\",\"areaCode\":2,\"local\":1234567}}";

    ObjectMapper objectMapper = new ObjectMapper();
    User u = objectMapper.readValue(json, User.class);

    System.out.println("deserialized user with dangerous phone = " + u);
  }

  @Test
  public void testPreventDeserializeDangerous() throws Exception {
    String json = "{\"name\":\"john\",\"age\":18,\"phone\":{\"@class\":\"foo.DangerousPhone\",\"areaCode\":2,\"local\":1234567}}";

    PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
      .build();

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.activateDefaultTyping(ptv , ObjectMapper.DefaultTyping.NON_FINAL);

    User u = objectMapper.readValue(json, User.class);

    System.out.println("deserialized user with dangerous phone = " + u);
  }
}
