import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.Payload
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.MACVerifier
import mu.KotlinLogging
import kotlin.test.Test

/**
 * Created by kevin.huang on 2020-06-29.
 */
class JoseTest {

  private val logger = KotlinLogging.logger { }

  val sharedKey = "0123456789ABCDEF0123456789ABCDEF".toByteArray() //  ByteArray(32)


  @Test
  fun testEncode() {
    val jwsHeader = JWSHeader(JWSAlgorithm.HS256)
    val jwsObject = JWSObject(jwsHeader, Payload("Kotlin 許功蓋碁"))

    logger.info("sharedKey(length = {}) = {}", sharedKey.size, sharedKey)

    jwsObject.sign(MACSigner(sharedKey))
    logger.info("jwsObject = '{}'", jwsObject.serialize())
  }

  @Test
  fun testParse() {
    val jwsObject = JWSObject.parse("eyJhbGciOiJIUzI1NiJ9.S290bGluIOioseWKn-iTi-eigQ.z54DHKK-py_POWCpx4BZ265ifwsw1RKaSJanBzxPJQI")
    logger.info("payload = {}", jwsObject.payload)
    val verifier = MACVerifier(sharedKey)
    val verified = jwsObject.verify(verifier)
    logger.info("verified = {}", verified)
  }
}