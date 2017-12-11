package ch.difty.scipamato.common;

import static org.assertj.core.api.Assertions.*;

import java.util.function.Supplier;

public final class TestUtils {

    private TestUtils() {
    }

    /**
     * Verify the supplier function (constructor/function) throws a {@link NullArgumentException} with a
     * non-nullable parameter passed in as null value.
     * @param supplier function or constructor with null parameter
     * @param fieldName the name of the field receiving the null parameter
     */
    public static void assertDegenerateSupplierParameter(final Supplier<?> supplier, final String fieldName) {
        try {
            supplier.get();
            fail("should have thrown exception");
        } catch (final Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage(fieldName + " must not be null.");
        }
    }

}
