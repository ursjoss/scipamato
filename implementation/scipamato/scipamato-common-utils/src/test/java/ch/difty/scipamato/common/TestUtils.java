package ch.difty.scipamato.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

import org.apache.commons.io.IOUtils;

public final class TestUtils {

    private TestUtils() {
    }

    /**
     * Verify the supplier function (constructor/function) throws a
     * {@link NullArgumentException} with a non-nullable parameter passed in as null
     * value.
     *
     * @param supplier
     *            function or constructor with null parameter
     * @param fieldName
     *            the name of the field receiving the null parameter
     */
    public static void assertDegenerateSupplierParameter(final Supplier<?> supplier, final String fieldName) {
        try {
            supplier.get();
            fail("should have thrown exception");
        } catch (final Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class)
                .hasMessage(fieldName + " must not be null.");
        }
    }

    /**
     * Read the file with the provided name from the system and hand it over as a
     * string.
     *
     * @param fileName
     *            the relative path of the file within the resources folder (e.g.
     *            'xml/myfile.xml')
     * @return file content as string.
     * @throws IOException
     */
    public static String readFileAsString(final String fileName) throws IOException {
        return IOUtils.toString(ClassLoader.getSystemResourceAsStream(fileName), StandardCharsets.UTF_8);
    }

}
