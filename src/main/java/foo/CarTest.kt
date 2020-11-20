/**
 * Created by smallufo on 2018-11-02.
 */
package foo

import kotlin.test.Test


class CarTest {

  enum class Vendor { Audi, BMW, Benz }

  enum class Type { Sedan, Coupe, HatchBack }

  data class Car(val vendor: Vendor,
                 val model: String,
                 val type: Type = Type.Sedan,
                 val doors: Int = 4)


  data class Garage(val address: String,
                    val cars: List<Car>?)

  class CarBuilder(private val vendor: Vendor, private val model: String) {
    var type = Type.Sedan
    var doors = 4
    fun build() = Car(vendor, model, type, doors)
  }

  class GarageBuilder(private val address: String) {
    private var cars = mutableListOf<Car>()

    fun car(vendor: Vendor, model: String, block: CarBuilder.() -> Unit = {}) {
      cars.add(CarBuilder(vendor, model).apply(block).build())
    }
    fun build() = Garage(address, cars.takeIf { it.isNotEmpty() })
  }

  companion object {
    fun garage(address: String , block : GarageBuilder.() -> Unit)
      = GarageBuilder(address).apply(block).build()
  }

  @Test
  fun ooStyle() {
    val garage1 =
      Garage("台北市忠孝東路一段1號",
             listOf(
               Car(Vendor.Audi, "A4"),
               Car(Vendor.BMW, "440i", Type.Coupe, 2),
               Car(Vendor.Benz, "A250", Type.HatchBack, 5)
                   )
            )
    println(garage1)
  }

  @Test
  fun dslStyle() {
    val garage1 = garage("台北市忠孝東路一段1號") {
      car(Vendor.Audi , "A4")
      car(Vendor.BMW , "440i") {
        type = Type.Coupe
        doors = 2
      }
      car(Vendor.Benz , "A250") {
        type = Type.HatchBack
        doors = 5
      }
    }

    println(garage1)
  }

}
