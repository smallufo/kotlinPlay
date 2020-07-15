/**
 * Created by kevin.huang on 2020-07-15.
 */
package foo


class HelloImpl : IHello {

  override fun hello(name: String) {
    println("Hello $name")
  }
}