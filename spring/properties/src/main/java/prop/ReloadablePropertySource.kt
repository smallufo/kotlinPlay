/**
 * Created by smallufo on 2020-05-13.
 */
package prop

import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.support.ResourcePropertySource


/**
 * TODO : ReloadablePropertySource
 */
class ReloadablePropertySource(
  name: String, fileSystemResource: FileSystemResource) : ResourcePropertySource(name, fileSystemResource) {

  var lastModified: Long

  init {
    val file = fileSystemResource.file
    lastModified = file.lastModified()
  }

}
