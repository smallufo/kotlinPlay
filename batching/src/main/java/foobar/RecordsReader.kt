/**
 * Created by kevin.huang on 2019-12-06.
 */
package foobar

data class Record(val region: String,
                  val country: String,
                  val itemType: String,
                  val salesChannel: String,
                  val orderPriority: String ,
                  val orderDate : String,
                  val shipDate : String,
                  val unitsSold : Int,
                  val unitPrice : Double ,
                  val unitCost : Double,
                  val totalCost : Double,
                  val totalProfit : Double)

class RecordsReader {

}