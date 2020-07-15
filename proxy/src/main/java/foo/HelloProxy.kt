/**
 * Created by kevin.huang on 2020-07-15.
 */
package foo

import mu.KotlinLogging


class HelloProxy(private val helloImpl: IHello) : IHello {

  override fun hello(name: String) {
    logger.info("Entering helloImpl ...")
    helloImpl.hello(name)
    logger.info("Exit helloImpl ...")
  }

  companion object {
    val logger = KotlinLogging.logger { }
  }
}