/**
 * Created by smallufo on 2020-04-30.
 */
package foobar

import mu.KotlinLogging
import net.sf.jasperreports.engine.*
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.support.ResourcePatternResolver
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.test.Test


class FirstJasperTest {

  private val logger = KotlinLogging.logger { }

  @Test
  fun findAllJrxmlFromResources() {
    val resolver = PathMatchingResourcePatternResolver()
    resolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "**/*.jrxml").forEach { r ->
      logger.info("r = {} , filename = {}", r, r.filename)
    }
  }

  @Test
  fun buildFilenameReportMap() {
    val resolver = PathMatchingResourcePatternResolver()
    val map: Map<String, JasperReport> = resolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "**/*.jrxml").map { r ->
      val jr: JasperReport = r.inputStream.use { iStream ->
        JasperCompileManager.compileReport(iStream)
      }
      r.filename to jr
    }.toMap()

    logger.info("map size = {}", map.size)
    map.forEach { (filename, jr) ->
      logger.info("{} = {}", filename, jr)
    }
  }

  @Test
  fun fillEmptyToBytes() {
    javaClass.getResourceAsStream("/Empty.jrxml").use { iStream ->
      val jr: JasperReport = JasperCompileManager.compileReport(iStream)
      logger.info("jr = {}", jr)

      val jasperPrint: JasperPrint = JasperFillManager
        .fillReport(jr, mutableMapOf(), JRBeanArrayDataSource(arrayOf()))

      ByteArrayOutputStream().use { bos ->
        JasperExportManager.exportReportToPdfStream(jasperPrint, bos)
        logger.info("bytes size = {}", bos.toByteArray().size)
      }
    }
  }

  @Test
  fun fillEmptyToFile() {
    javaClass.getResourceAsStream("/Empty.jrxml").use { iStream ->
      val jr: JasperReport = JasperCompileManager.compileReport(iStream)
      logger.info("jr = {}", jr)

      val jasperPrint: JasperPrint = JasperFillManager
        .fillReport(jr, mutableMapOf(), JRBeanArrayDataSource(arrayOf()))

      FileOutputStream(File("/tmp/empty.pdf")).use { fos ->
        JasperExportManager.exportReportToPdfStream(jasperPrint, fos)
      }
    }
  }

  @Test
  fun showPerson_english() {
    javaClass.getResourceAsStream("/docs/Persons.jrxml").use { iStream ->
      val jr: JasperReport = JasperCompileManager.compileReport(iStream)

      val persons = listOf(
        Person("Andy", "USA"),
        Person("Bob", "Brazil"),
        Person("Cathy", "Canada")
      )

      val beanColDataSource: JRDataSource = JRBeanCollectionDataSource(persons)

      val jasperPrint: JasperPrint = JasperFillManager.fillReport(jr, mutableMapOf(), beanColDataSource)

      FileOutputStream(File("/tmp/persons_english.pdf")).use { fos ->
        JasperExportManager.exportReportToPdfStream(jasperPrint, fos)
      }
    }
  }

  @Test
  fun showPerson_withChineseChars() {
    javaClass.getResourceAsStream("/docs/PersonsChinese.jrxml").use { iStream ->
      val jr: JasperReport = JasperCompileManager.compileReport(iStream)

      val persons = listOf(
        Person("Andy", "USA"),
        Person("Bob", "Brazil"),
        Person("Cathy", "Canada"),
        Person("李小明", "Taiwan"),
        Person("許功蓋堃", "台灣")
      )

      val beanColDataSource: JRDataSource = JRBeanCollectionDataSource(persons)

      val jasperPrint: JasperPrint = JasperFillManager.fillReport(jr, mutableMapOf(), beanColDataSource)

      FileOutputStream(File("/tmp/persons_chinese.pdf")).use { fos ->
        JasperExportManager.exportReportToPdfStream(jasperPrint, fos)
      }
    }
  }
}
