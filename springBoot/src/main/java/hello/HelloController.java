package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @RequestMapping("/")
  public String index() {
    return "Greetings from Spring Boot!";
  }

  /**
   * test
   * http://localhost:8080/hello/xxx%40xxx.xxx%27%20OR%201%20%3D%201%20LIMIT%201%20--%20%27%20%5D/18
   */
  @ResponseStatus(value = HttpStatus.OK)
  @GetMapping("/hello/{name}/{age}")
  public String hello(@PathVariable("name") String name , @PathVariable("age") Integer age) {
    logger.info("name = {} , age = {}" , name , age);
    return "Hello name = " + name + " , age = " + age;
  }
}