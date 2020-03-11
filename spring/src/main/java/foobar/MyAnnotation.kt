package foobar

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class MyAnnotation(val key: String, val value: String)

data class User(
  @MyAnnotation(key = "default", value = "default")
  val username: String
)

@Configuration
open class UserConfig {

  @Bean
  open fun userKevin(): User {
    return User("Kevin")
  }
}

fun main() {
  val user = User("kevin")
  println(user)
  user::class.memberProperties.forEach { prop: KProperty1<out User, *> ->
    prop.annotations.forEach { anno ->
      println("$anno")
    }
  }
}
