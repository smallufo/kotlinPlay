/**
 * Created by smallufo on 2020-04-30.
 */
package foobar

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream
import mu.KotlinLogging
import net.sf.jasperreports.engine.*
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.support.ResourcePatternResolver
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
    val map = resolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "**/*.jrxml").map { r ->
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
  fun fill() {
    javaClass.getResourceAsStream("/Empty.jrxml").use { iStream ->
      val jr: JasperReport = JasperCompileManager.compileReport(iStream)
      logger.info("jr = {}", jr)

      val jasperPrint: JasperPrint = JasperFillManager
        .fillReport(jr , mutableMapOf(), JRBeanArrayDataSource(arrayOf()))
      val bos = ByteOutputStream()
      JasperExportManager.exportReportToPdfStream(jasperPrint , bos)
      logger.info("bytes size = {}" , bos.bytes.size)
    }
  }
}
