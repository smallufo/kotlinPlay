/**
 * Created by kevin.huang on 2020-09-25.
 */
package foo

import mu.KotlinLogging
import org.tribuo.Model
import org.tribuo.MutableDataset
import org.tribuo.Trainer
import org.tribuo.classification.Label
import org.tribuo.classification.LabelFactory
import org.tribuo.classification.evaluation.LabelEvaluation
import org.tribuo.classification.evaluation.LabelEvaluator
import org.tribuo.classification.sgd.linear.LogisticRegressionTrainer
import org.tribuo.data.csv.CSVLoader
import org.tribuo.evaluation.TrainTestSplitter
import java.net.URL
import java.nio.file.Paths

fun main(args: Array<String>) {
  println("Hello World!")

  val logger = KotlinLogging.logger { }

  val labelFactory = LabelFactory()
  val csvLoader = CSVLoader(labelFactory)

  val irisHeaders = arrayOf("sepalLength", "sepalWidth", "petalLength", "petalWidth", "species")

  val res: URL = object {}.javaClass.classLoader.getResource("irisData.csv")!!

  val irisesSource = csvLoader.loadDataSource(Paths.get(res.toURI()), "species", irisHeaders)
  val irisSplitter = TrainTestSplitter(irisesSource, 0.7, 1L)

  val trainingDataset = MutableDataset(irisSplitter.train)
  val testingDataset = MutableDataset(irisSplitter.test)


  logger.info("Training data size = {} , number of features = {} , number of classes = {}",
              trainingDataset.size(), trainingDataset.featureMap.size(), trainingDataset.outputInfo.size())

  logger.info("Testing data size = {} , number of features = {} , number of classes = {}",
              testingDataset.size(), testingDataset.featureMap.size(), testingDataset.outputInfo.size())
  val trainer: Trainer<Label> = LogisticRegressionTrainer()

  logger.info { "trainer = $trainer" }

  val irisModel: Model<Label> = trainer.train(trainingDataset)

  val evaluator = LabelEvaluator()
  val evaluation: LabelEvaluation = evaluator.evaluate(irisModel, testingDataset)
  logger.info {
    "================ result ================ \n" +
    "n = tn = trueNegative , tp = truePositive , fn = falseNegative , fp = falsePositive ,  \n" +
    evaluation.toString()
  }
}