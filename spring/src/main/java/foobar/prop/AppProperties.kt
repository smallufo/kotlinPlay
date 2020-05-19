/**
 * Created by smallufo on 2020-05-14.
 */
package foobar.prop

//@Component
class AppProperties : ReloadableProperties() {

  fun getUserAlias() : String? {
    return environment.getProperty("user.alias")
  }

  override fun propertiesReloaded() {
    TODO("Not yet implemented")
  }
}
