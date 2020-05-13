/**
 * Created by smallufo on 2020-05-13.
 */
package foobar

import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.support.ResourcePropertySource


/**
 * TODO : ReloadablePropertySource
 */
class ReloadablePropertySource(
  name: String, fileSystemResource: FileSystemResource) : ResourcePropertySource(name, fileSystemResource) {
}
