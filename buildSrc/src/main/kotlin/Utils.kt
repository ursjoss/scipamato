import java.io.File

fun File.asProperties() = java.util.Properties().apply {
    inputStream().use { fis ->
        load(fis)
    }
}
