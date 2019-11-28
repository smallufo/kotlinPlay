import org.apache.commons.beanutils.BeanUtilsBean;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class BeanUtilsTest {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Test
  public void testBean() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
    final BeanUtilsBean utilsBean = new BeanUtilsBean();
    final AlphaBean bean = new AlphaBean();

    Map<String,  String> map = utilsBean.describe(bean);
    map.forEach( (k,v) -> {
      logger.info("{} = {}" , k , v);
    });

//    String nameProp = utilsBean.getProperty(bean, "name");
//    logger.info("nameProp = {}", nameProp);
//
//    String classProp = utilsBean.getProperty(bean, "class");
//    logger.info("classProp = {}", classProp);

  }
}
