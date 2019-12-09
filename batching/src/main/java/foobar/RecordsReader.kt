/**
 * Created by kevin.huang on 2019-12-06.
 */
package foobar

import mu.KotlinLogging
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Record(val region: String,
                  val country: String,
                  val itemType: String,
                  val salesChannel: String,
                  val orderPriority: Char,
                  val orderDate: LocalDate,
                  val orderId : Long,
                  val shipDate: LocalDate,
                  val unitsSold: Int,
                  val unitPrice: Double,
                  val unitCost: Double,
                  val totalRevenue : Double,
                  val totalCost: Double,
                  val totalProfit: Double)

class RecordsReader {

  private val logger = KotlinLogging.logger {}

  private val df = DateTimeFormatter.ofPattern("M/d/y")

  fun read(): List<Record> {
    return javaClass.getResourceAsStream("100_Sales_Records.csv").bufferedReader().useLines { lines ->
      lines.drop(1).map { line ->
        val tokens = line.split(",")
        val region = tokens[0]
        val country = tokens[1]
        val itemType = tokens[2]
        val salesChannel = tokens[3]
        val orderPriority = tokens[4][0]
        val orderDate = LocalDate.parse(tokens[5] , df)
        val orderId = tokens[6].toLong()
        val shipDate = LocalDate.parse(tokens[7] , df)
        val unitsSold = tokens[8].toInt()
        val unitPrice = tokens[9].toDouble()
        val unitCost = tokens[10].toDouble()
        val totalRevenue = tokens[11].toDouble()
        val totalCost = tokens[12].toDouble()
        val totalProfit = tokens[13].toDouble()
        Record(region , country , itemType , salesChannel , orderPriority , orderDate , orderId , shipDate, unitsSold, unitPrice, unitCost, totalRevenue, totalCost, totalProfit)
      }.toList()
    }
  }
}