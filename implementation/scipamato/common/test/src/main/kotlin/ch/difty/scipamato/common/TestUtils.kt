package ch.difty.scipamato.common

import org.apache.commons.io.IOUtils
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.function.Supplier

/**
 * Verify the [supplier] function (constructor/function) throws a [NullArgumentException] with a
 * non-nullable parameter passed in as null value. Uses [fieldName] in the exception message.
 */
// TODO Replace using kotlin non-nullable types
fun assertDegenerateSupplierParameter(supplier: Supplier<*>, fieldName: String) {
    try {
        supplier.get()
        fail<Any>("should have thrown exception")
    } catch (ex: Exception) {
        assertThat(ex).isInstanceOf(NullArgumentException::class.java).hasMessage("$fieldName must not be null.")
    }
}

/**
 * Read the file with the provided [fileName] from the system and hand it over as a string.
 */
@Throws(IOException::class)
fun readFileAsString(fileName: String): String {
    return IOUtils.toString(ClassLoader.getSystemResourceAsStream(fileName)!!, StandardCharsets.UTF_8)
}
