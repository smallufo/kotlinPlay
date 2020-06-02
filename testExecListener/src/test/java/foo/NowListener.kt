/**
 * Created by kevin.huang on 2020-06-02.
 */
package foo

import mu.KotlinLogging
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener
import org.springframework.util.ReflectionUtils
import java.time.ZoneId
import java.time.ZonedDateTime

class NowListener : AbstractTestExecutionListener() {

  override fun prepareTestInstance(testContext: TestContext) {
    super.prepareTestInstance(testContext)

    val testObj = testContext.testInstance
    val testClass = testContext.testClass
    ReflectionUtils.doWithFields(testClass) { field ->
      field.getAnnotation(Now::class.java)?.also { now ->
        val originalCanAccess = field.canAccess(testObj)
        field.isAccessible = true
        logger.debug("found @Now , set time to the field...")

        val zoneId = try {
          ZoneId.of(now.timeZone)
        } catch (ignored: Exception) {
          ZoneId.systemDefault()
        }

        val localDateTime = ZonedDateTime.now().withZoneSameInstant(zoneId).toLocalDateTime()

        field.set(testObj, localDateTime)
        field.isAccessible = originalCanAccess
      }
    }
  }

  companion object {
    val logger = KotlinLogging.logger { }
  }
}