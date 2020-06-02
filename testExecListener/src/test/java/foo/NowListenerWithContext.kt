/**
 * Created by kevin.huang on 2020-06-02.
 */
package foo

import mu.KotlinLogging
import org.springframework.context.ApplicationContext
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener
import org.springframework.util.ReflectionUtils

class NowListenerWithContext : AbstractTestExecutionListener() {

  override fun prepareTestInstance(testContext: TestContext) {
    super.prepareTestInstance(testContext)

    val appContext: ApplicationContext = testContext.applicationContext
    logger.info("app context = {}", appContext)
    appContext.getBean("timeDefaultImpl").also { bean ->
      val timeImpl = (bean as ITime)
      logger.info("timeImpl = {}", timeImpl)


      val testObj = testContext.testInstance
      val testClass = testContext.testClass

      ReflectionUtils.doWithFields(testClass) { field ->
        field.getAnnotation(Now::class.java)?.also { now ->
          val originalCanAccess = field.canAccess(testObj)
          field.isAccessible = true

          val localDateTime = timeImpl.getTime(now.timeZone)


          field.set(testObj, localDateTime)
          field.isAccessible = originalCanAccess
        }
      }
    }
  }

  companion object {
    val logger = KotlinLogging.logger { }
  }
}