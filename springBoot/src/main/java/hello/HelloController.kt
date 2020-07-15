package hello

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
class HelloController {
  private val logger = LoggerFactory.getLogger(javaClass)

  @RequestMapping("/")
  fun index(): String {
    val now = LocalDateTime.now()
    return "Greetings from Spring Boot! Now , localDateTime = $now"
  }

  /**
   * test
   * http://localhost:8080/hello/xxx%40xxx.xxx%27%20OR%201%20%3D%201%20LIMIT%201%20--%20%27%20%5D/18
   */
  @ResponseStatus(value = HttpStatus.OK)
  @GetMapping("/hello/{name}/{age}")
  fun hello(@PathVariable("name") name: String, @PathVariable("age") age: Int): String {
    logger.info("name = {} , age = {}", name, age)
    return "Hello name = $name , age = $age"
  }
}