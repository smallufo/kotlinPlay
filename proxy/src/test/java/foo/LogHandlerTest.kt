/**
 * Created by kevin.huang on 2020-07-15.
 */
package foo

import kotlin.test.Test


class LogHandlerTest {

  @Test
  fun testHandler() {

    val proxiedHello = (HelloImpl() as IHello).proxy()

    proxiedHello.hello("Kevin")
  }
}