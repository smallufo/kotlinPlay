/**
 * Created by kevin.huang on 2020-07-15.
 */
package foo

import mu.KotlinLogging
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy


inline fun <reified T : IHello> T.proxy(): T {
  return Proxy.newProxyInstance(javaClass.classLoader, arrayOf(IHello::class.java), LogHandler(this)) as T
}

class LogHandler(private val delegate: Any) : InvocationHandler {

  private val logger = KotlinLogging.logger { }

  override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any? {
    logger.info("Enter invoke... proxy name = {} ", proxy.javaClass.name)
    logger.info("Enter invoke... method = {} ", method)
    logger.info("Enter invoke... args = {} ", args)
    val result = method.invoke(delegate, *args)
    //val result = method.invoke(delegate, *(args ?: emptyArray()))
    logger.info("Exit invoke...")
    return result
  }
}