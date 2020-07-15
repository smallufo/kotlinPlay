/**
 * Created by kevin.huang on 2020-07-15.
 */
package foo

import kotlin.test.Test


class HelloProxyTest {

  @Test
  fun testProxy() {
    val helloImpl : IHello = HelloImpl()
    val helloProxy = HelloProxy(helloImpl)
    helloProxy.hello("proxy")
  }
}