/**
 * Created by smallufo on 2018-11-02.
 */


import CarBikeTest3.Bike.Vendor.Giant
import CarBikeTest3.Bike.Vendor.Merida
import CarBikeTest3.Car.Type.Coupe
import CarBikeTest3.Car.Type.HatchBack
import CarBikeTest3.Car.Vendor.*
import kotlin.test.Test

class CarBikeTest3 {


  data class Car(val vendor: Vendor,
                 val model: String,
                 val type: Type = Type.Sedan,
                 val doors: Int = 4) {

    enum class Vendor { Audi, BMW, Benz }

    enum class Type { Sedan, Coupe, HatchBack }

    class CarBuilder(private val vendor: Vendor, private val model: String) {
      var type = Type.Sedan
      var doors = 4
      fun build() = Car(vendor, model, type, doors)
    }
  }


  data class Bike(val vendor: Vendor, val model: String) {
    enum class Vendor { Giant, Merida }
  }

  data class Garage(val address: String,
                    val cars: List<Car>,
                    val bikes: List<Bike>) {

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
  }


  class Cars : ArrayList<Car>() {
    fun car(vendor: Car.Vendor, model: String, block: Car.CarBuilder.() -> Unit = {}) {
      add(Car.CarBuilder(vendor, model).apply(block).build())
    }
  }

  class Bikes : ArrayList<Bike>() {
    fun bike(vendor: Bike.Vendor, model: String) {
      add(Bike(vendor, model))
    }
  }


  companion object {
    fun garage(address: String, block: Garage.GarageBuilder.() -> Unit) =
      Garage.GarageBuilder(address).apply(block).build()
  }

  @Test
  fun carsSeparatedFromBikes() {
    val garage1 = garage("台北市忠孝東路一段1號") {
      cars {
        car(Audi, "A4")
        car(BMW, "440i") {
          type = Coupe
          doors = 2
        }
        car(Benz, "A250") {
          type = HatchBack
          doors = 5
        }
      }

      bikes {
        bike(Merida, "Crossway 100")
        bike(Giant, "Glory 27.5")
      }
    }

    println(garage1)
  }
}
