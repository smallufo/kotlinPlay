/**
 * Created by kevin.huang on 2019-12-30.
 */
package easy;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("")
public class EasyApp extends Application {

  @Override
  public Set<Object> getSingletons() {
    HashSet<Object> set = new HashSet<>();
		set.add(new HelloController());
    return set;
  }
}
