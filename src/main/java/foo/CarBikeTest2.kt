/**
 * Created by smallufo on 2018-11-02.
 */
package foo

import kotlin.test.Test

class CarBikeTest2 {

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

    fun cars(block: Cars.() -> Unit) {
      cars.addAll(Cars().apply(block))
    }

    fun bikes(block: Bikes.() -> Unit) {
      bikes.addAll(Bikes().apply(block))
    }

    fun build() = Garage(address, cars, bikes)
  }

  class Cars : ArrayList<Car>() {
    fun car(vendor: CarVendor, model: String, block: CarBuilder.() -> Unit = {}) {
      add(CarBuilder(vendor, model).apply(block).build())
    }
  }

  class Bikes : ArrayList<Bike>() {
    operator fun Bike.unaryPlus() {
      add(this)
    }
  }


  companion object {
    fun garage(address: String, block: GarageBuilder.() -> Unit) = GarageBuilder(address).apply(block).build()
  }

  @Test
  fun carsSeparatedFromBikes() {
    val garage1 = garage("台北市忠孝東路一段1號") {
      cars {
        car(CarVendor.Audi, "A4")
        car(CarVendor.BMW, "440i") {
          type = CarType.Coupe
          doors = 2
        }
        car(CarVendor.Benz, "A250") {
          type = CarType.HatchBack
          doors = 5
        }
      }

      bikes {
        +Bike(BikeVendor.Merida, "Crossway 100")
        +Bike(BikeVendor.Giant, "Glory 27.5")
      }
    }

    println(garage1)
  }
}
