import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import foo.DangerousPhone;
import foo.DomesticPhone;
import foo.InternationalPhone;
import foo.User;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UserSerializerTest {

  @Test
  public void testSerialize() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    User p = new User("john", 18);
    p.phone = new DomesticPhone(2, 1234567);
    //p.phone = new InternationalPhone(886, 2, 1234567);

    String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(p);
    System.out.println(json);
  }


  @Test
  public void testDeserializeByClass_Domestic() throws JsonProcessingException {
    String json = """
      {
         "name" : "john",
         "age" : 18,
         "phone" : {
           "@class" : "foo.DomesticPhone",
           "areaCode" : 2,
           "local" : 1234567
         }
       }
      """;

    ObjectMapper objectMapper = new ObjectMapper();
    User u = objectMapper.readValue(json, User.class);

    System.out.println("user = " + u);
    assertTrue(u.phone instanceof DomesticPhone);
  }

  @Test
  public void testDeserializeByClass_International() throws JsonProcessingException {
    String json = """
      {
        "name" : "john",
        "age" : 18,
        "phone" : {
          "@class" : "foo.InternationalPhone",
          "areaCode" : 2,
          "local" : 1234567,
          "countryCode" : 886
        }
      }
      """;

    ObjectMapper objectMapper = new ObjectMapper();
    User u = objectMapper.readValue(json, User.class);

    System.out.println("user = " + u);
    assertTrue(u.phone instanceof InternationalPhone);
  }


  @Test
  public void testDeserializeByClass_Dangerous() throws JsonProcessingException {
    String json = """
      {
         "name" : "john",
         "age" : 18,
         "phone" : {
           "@class" : "foo.DangerousPhone",
           "areaCode" : 2,
           "local" : 1234567
         }
       }
      """;

    ObjectMapper objectMapper = new ObjectMapper();
    User u = objectMapper.readValue(json, User.class);

    System.out.println("user = " + u);
    assertTrue(u.phone instanceof DangerousPhone);
  }

  // ===========================================================================


  @Test
  public void testDeserializeByTypeName_domestic() throws JsonProcessingException {
    String json = """
      {
         "name" : "john",
         "age" : 18,
         "phone" : {
           "@type" : "domestic",
           "areaCode" : 2,
           "local" : 1234567
         }
       }
      """;
    ObjectMapper objectMapper = new ObjectMapper();
    User u = objectMapper.readValue(json, User.class);

    System.out.println("user = " + u);
    assertTrue(u.phone instanceof DomesticPhone);
  }


  @Test
  public void testDeserializeByTypeName_international() throws JsonProcessingException {
    String json = """
      {
        "name" : "john",
        "age" : 18,
        "phone" : {
          "@type" : "international",
          "areaCode" : 2,
          "local" : 1234567,
          "countryCode" : 886
        }
      }
      """;
    ObjectMapper objectMapper = new ObjectMapper();
    User u = objectMapper.readValue(json, User.class);

    System.out.println("user = " + u);
    assertTrue(u.phone instanceof InternationalPhone);
  }


  @Test
  public void testDeserializeByTypeName_dangerous() {
    String json = """
      {
         "name" : "john",
         "age" : 18,
         "phone" : {
           "@type" : "dangerous",
           "areaCode" : 2,
           "local" : 1234567
         }
       }
      """;
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      objectMapper.readValue(json, User.class);
      fail();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }



  @Test
  public void testPreventDeserializeDangerous() throws Exception {
    String json = "{\"name\":\"john\",\"age\":18,\"phone\":{\"@class\":\"foo.DangerousPhone\",\"areaCode\":2,\"local\":1234567}}";

    PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder().build();

    ObjectMapper objectMapper = new ObjectMapper();
    //objectMapper.activateDefaultTyping(ptv , ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT);  // success
    //objectMapper.activateDefaultTyping(ptv , ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE);  // success
    //objectMapper.activateDefaultTyping(ptv , ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS); // success
    //objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.EVERYTHING);// failed
    objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL); // failed

    User u = objectMapper.readValue(json, User.class);

    System.out.println("deserialized user with dangerous phone = " + u);
  }
}
