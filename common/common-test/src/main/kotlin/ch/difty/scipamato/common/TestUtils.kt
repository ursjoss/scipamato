package ch.difty.scipamato.common

import org.apache.commons.io.IOUtils
import java.io.IOException
import java.nio.charset.StandardCharsets

/**
 * Read the file with the provided [fileName] from the system and hand it over as a string.
 */
@Throws(IOException::class)
fun readFileAsString(fileName: String): String {
    return IOUtils.toString(ClassLoader.getSystemResourceAsStream(fileName)!!, StandardCharsets.UTF_8)
}
