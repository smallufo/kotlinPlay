/**
 * Created by kevin.huang on 2019-12-09.
 */
package foobar

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.format.DateTimeFormatter


@Configuration
open class BatchConfig {

  private val df = DateTimeFormatter.ofPattern("M/d/y")


  @Bean
  open fun readStep(stepBuilderFactory: StepBuilderFactory, reader: ItemReader<Record>): Step {
//    FlatFileItemReaderBuilder<Record>()
//      .name("reader")
//      .resource(ClassPathResource("100_Sales_Records.csv"))
//      .lineMapper { line, index ->
//        val tokens = line.split(",")
//          val region = tokens[0]
//          val country = tokens[1]
//          val itemType = tokens[2]
//          val salesChannel = tokens[3]
//          val orderPriority = tokens[4][0]
//          val orderDate = LocalDate.parse(tokens[5], df)
//          val orderId = tokens[6].toLong()
//          val shipDate = LocalDate.parse(tokens[7], df)
//          val unitsSold = tokens[8].toInt()
//          val unitPrice = tokens[9].toDouble()
//          val unitCost = tokens[10].toDouble()
//          val totalRevenue = tokens[11].toDouble()
//          val totalCost = tokens[12].toDouble()
//          val totalProfit = tokens[13].toDouble()
//          Record(region, country, itemType, salesChannel, orderPriority, orderDate, orderId, shipDate, unitsSold,
//                 unitPrice, unitCost, totalRevenue, totalCost, totalProfit)
//      }
    return stepBuilderFactory.get("readStep").chunk<Record , Record>(10)
      .reader(reader)
      .build()
  }
}