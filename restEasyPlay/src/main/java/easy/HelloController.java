package easy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/hello")
@Produces(MediaType.TEXT_PLAIN)
public class HelloController {

  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * mvn clean compile install jetty:run
   *
   * http://localhost:8080/hello/kevin/18
   * http://localhost:8080/hello/xxx%40xxx.xxx%27%20OR%201%20%3D%201%20LIMIT%201%20--%20%27%20%5D/18
   */
  @GET
  @Path("/{username}/{age}")
  public Response sayHello(@PathParam("username") String username , @PathParam("age") Integer age) {

    logger.info("username = {} , age = {}" , username , age);

    String result =  "Hello " + username + " , your age is " + age;
    return Response.status(200).entity(result).build();
  }
}
