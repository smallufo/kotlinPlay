/**
 * Created by kevin.huang on 2019-12-09.
 */
package foobar

import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.LineMapper
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class RecordItemReader : FlatFileItemReader<Record?>() {

  private val df = DateTimeFormatter.ofPattern("M/d/y")

  init {
    setResource(ClassPathResource("100_Sales_Records.csv"))
    setLineMapper(object : LineMapper<Record?> {
      override fun mapLine(line: String, index: Int): Record? {
        return if (index == 0) {
          null
        } else {
          val tokens = line.split(",")
          val region = tokens[0]
          val country = tokens[1]
          val itemType = tokens[2]
          val salesChannel = tokens[3]
          val orderPriority = tokens[4][0]
          val orderDate = LocalDate.parse(tokens[5], df)
          val orderId = tokens[6].toLong()
          val shipDate = LocalDate.parse(tokens[7], df)
          val unitsSold = tokens[8].toInt()
          val unitPrice = tokens[9].toDouble()
          val unitCost = tokens[10].toDouble()
          val totalRevenue = tokens[11].toDouble()
          val totalCost = tokens[12].toDouble()
          val totalProfit = tokens[13].toDouble()
          Record(region, country, itemType, salesChannel, orderPriority, orderDate, orderId, shipDate, unitsSold,
                 unitPrice, unitCost, totalRevenue, totalCost, totalProfit)
        }
      }
    })
  }


}