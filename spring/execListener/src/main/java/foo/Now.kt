/**
 * Created by kevin.huang on 2020-06-02.
 */
package foo

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)
annotation class Now(val timeZone: String = "Asia/Taipei" ,
                    val offsetMin : Long = 0)