/**
 * Created by smallufo on 2021-02-27.
 */
package foo

import java.time.LocalDate

/**
 * https://medium.com/google-developer-experts/companion-objects-kotlins-most-unassuming-power-feature-fb5c0451fbd0
 */
fun interface Validation : (LocalDate) -> Boolean {
  companion object {
    //val future: Validation get() = Validation { it.isAfter(LocalDate.now()) }
  }
}

val Validation.Companion.future get() = Validation { it.isAfter(LocalDate.now()) }

fun Validation.Companion.between(start: LocalDate, end: LocalDate) =
  Validation { it.isAfter(start) && it.isBefore(end) }


fun main() {
  // for this data...
  val jan1 = LocalDate.of(2021, 1, 1)
  val dec31 = LocalDate.of(2021, 12, 31)

  // we can use the Validation like so...
  require(!Validation.future(jan1))
  val year2021 = Validation.between(jan1, dec31)
  require(year2021(LocalDate.of(2021, 2, 27)))
}
