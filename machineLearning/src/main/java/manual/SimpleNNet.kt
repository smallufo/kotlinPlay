package manual

import org.la4j.Matrix
import java.util.*
import kotlin.math.exp

/**
 * http://popcornylu.github.io/2015/07/25/java-neural-netowrk.html
 *
 * The code was inspired by http://iamtrask.github.io/2015/07/12/basic-python-network/
 */
object SimpleNNet {

  @JvmStatic
  fun main(args: Array<String>) {
    val x = Matrix.from2DArray(
      arrayOf(doubleArrayOf(0.0, 0.0, 1.0),
              doubleArrayOf(0.0, 1.0, 1.0),
              doubleArrayOf(1.0, 0.0, 1.0),
              doubleArrayOf(1.0, 1.0, 1.0))
                              )
    val y = Matrix.from2DArray(arrayOf(doubleArrayOf(0.0, 1.0, 1.0, 0.0))).transpose()
    val random = Random()
    var syn0 = Matrix.random(3, 4, random)
      .multiply(2.0)
      .subtract(1.0)

    var syn1 = Matrix.random(4, 1, random)
      .multiply(2.0)
      .subtract(1.0)

    var x1: Matrix? = null
    var x2: Matrix? = null

    for (loop in 1..50000) {
      // Forward
      // s1 = x0 . syn0
      // x1 = sigmoid(s1)
      val s1 = x.multiply(syn0)
      x1 = s1.transform { i: Int, j: Int, value: Double -> sigmoid(value) }

      // s2 = x1 . syn1
      // x2 = sigmoid(x1 . syn1)
      val s2 = x1.multiply(syn1)
      x2 = s2.transform { i: Int, j: Int, value: Double -> sigmoid(value) }

      // Backward
      // l2_delta = 2*(y - x2)*sigmoid'(s2)
      val l2Delta = y.subtract(x2).multiply(2.0).hadamardProduct(
        s2.transform { i: Int, j: Int, value: Double -> sigmoid_deriv(value) }
                                                                )

      // l1_delta = l2_delta.dot(syn1.T) * sigmoid'(s1)
      val l1Delta = l2Delta.multiply(syn1.transpose()).hadamardProduct(
        s1.transform { i: Int, j: Int, value: Double -> sigmoid_deriv(value) }
                                                                      )

      // Update the weights
      // syn1 += x1.T.dot(l2_delta)
      syn1 = syn1.add(x1.transpose().multiply(l2Delta))
      // syn0 += X.T.dot(l1_delta)
      syn0 = syn0.add(x.transpose().multiply(l1Delta))
    }
    println(x2)
  }

  /**
   * Sigmoid Function: https://en.wikipedia.org/wiki/Sigmoid_function
   */
  private fun sigmoid(x: Double): Double {
    return 1 / (1 + exp(-x))
  }

  /**
   * Derivation of Sigmoid Function: https://en.wikipedia.org/wiki/Sigmoid_function
   */
  private fun sigmoid_deriv(x: Double): Double {
    return sigmoid(x) * (1 - sigmoid(x))
  }
}
