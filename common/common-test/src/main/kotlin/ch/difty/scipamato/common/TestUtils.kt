package ch.difty.scipamato.common

import java.nio.charset.StandardCharsets

/**
 * Read the file with the provided [fileName] from the resources directory and hand it over as a string.
 */
fun readFileAsString(fileName: String): String =
    ClassLoader.getSystemResourceAsStream(fileName)
        ?.bufferedReader(StandardCharsets.UTF_8)
        ?.use { it.readText() } ?: ""
