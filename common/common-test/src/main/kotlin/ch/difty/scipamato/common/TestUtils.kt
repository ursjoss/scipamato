package ch.difty.scipamato.common

import java.io.IOException
import java.nio.charset.StandardCharsets

/**
 * Read the file with the provided [fileName] from the system and hand it over as a string.
 */
@Throws(IOException::class)
fun readFileAsString(fileName: String): String =
    ClassLoader.getSystemResourceAsStream(fileName)
        ?.bufferedReader(StandardCharsets.UTF_8)
        ?.use { it.readText() } ?: ""
