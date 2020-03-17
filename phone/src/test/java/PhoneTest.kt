import com.google.i18n.phonenumbers.PhoneNumberUtil
import mu.KotlinLogging
import kotlin.test.Test


/**
 * Created by kevin.huang on 2020-03-17.
 */

class PhoneTest {

  val logger = KotlinLogging.logger { }

  @Test
  fun testTW() {
    val phoneUtil = PhoneNumberUtil.getInstance()
    val global = phoneUtil.format(phoneUtil.parse("0912345678", "TW"), PhoneNumberUtil.PhoneNumberFormat.E164);
    logger.info("result = {}", global)
  }

  @Test
  fun testHK() {
    val phoneUtil = PhoneNumberUtil.getInstance()
    val global = phoneUtil.format(phoneUtil.parse("85298000123", "HK"), PhoneNumberUtil.PhoneNumberFormat.E164);
    logger.info("result = {}", global)
  }
}