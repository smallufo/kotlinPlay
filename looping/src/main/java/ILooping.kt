interface INext<T> {

  fun next(n: Int): T

  val T.next
    get() = next(1)
}

/**
 * https://youtrack.jetbrains.com/issue/KT-35043
 */
object IntNext : INext<Int> {

//  override val Int.next: Int
//    get() {
//      return this + 1
//    }

  override fun next(n: Int): Int {
    return n + 1
  }

}
