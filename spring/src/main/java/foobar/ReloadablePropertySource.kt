/**
 * Created by smallufo on 2020-05-13.
 */
package foobar

import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.support.ResourcePropertySource
import java.io.File


/**
 * TODO : ReloadablePropertySource
 */
class ReloadablePropertySource(
  name: String, fileSystemResource: FileSystemResource) : ResourcePropertySource(name, fileSystemResource) {



  init {

    val file = fileSystemResource.file
    val lastModified = file.lastModified()
    map[file] = lastModified
  }

  companion object {
    val map = mutableMapOf<File, Long>()
  }
}
