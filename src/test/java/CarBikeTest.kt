/**
 * Created by smallufo on 2018-11-02.
 */

import kotlin.test.Test


class CarBikeTest {

  enum class CarVendor { Audi, BMW, Benz }

  enum class CarType { Sedan, Coupe, HatchBack }

  data class Car(val vendor: CarVendor,
                 val model: String,
                 val type: CarType = CarType.Sedan,
                 val doors: Int = 4)


  enum class BikeVendor { Giant, Merida }
  data class Bike(val vendor: BikeVendor, val model: String)

  data class Garage(val address: String,
                    val cars: List<Car>,
                    val bikes: List<Bike>)


  class CarBuilder(private val vendor: CarVendor, private val model: String) {
    var type = CarType.Sedan
    var doors = 4
    fun build() = Car(vendor, model, type, doors)
  }

  class GarageBuilder(private val address: String) {
    private var cars = mutableListOf<Car>()
    private var bikes = mutableListOf<Bike>()

    fun car(vendor: CarVendor, model: String, block: CarBuilder.() -> Unit = {}) {
      cars.add(CarBuilder(vendor, model).apply(block).build())
    }

    fun bike(vendor: BikeVendor, model: String) {
      bikes.add(Bike(vendor, model))
    }

    fun build() = Garage(address, cars , bikes)
  }

  companion object {
    fun garage(address: String, block: GarageBuilder.() -> Unit)
      = GarageBuilder(address).apply(block).build()
  }

  @Test
  fun carsMixedWithBikes() {
    val garage1 = garage("台北市忠孝東路一段1號") {
      car(CarVendor.Audi, "A4")
      bike(BikeVendor.Merida , "Crossway 100")
      car(CarVendor.BMW , "440i") {
        type = CarType.Coupe
        doors = 2
      }
      bike(BikeVendor.Giant , "Glory 27.5")
      car(CarVendor.Benz , "A250") {
        type = CarType.HatchBack
        doors = 5
      }
    }

    println(garage1)
  }

}
