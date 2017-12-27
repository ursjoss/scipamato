package ch.difty.scipamato.common;

public final class AssertAs {

    private AssertAs() {
    }

    /**
     * Checks if the value is null. If so, throws a {@link NullArgumentException}.
     * If not returns the value.
     *
     * @param value
     *            to validate
     * @param name
     *            the name of the field
     * @return the non-null value of type {@code T}
     * @throws {@link
     *             NullArgumentException} if the value is null
     */
    public static <T> T notNull(final T value, final String name) {
        if (value == null) {
            throw new NullArgumentException(name);
        }
        return value;
    }

    /**
     * Checks if the field is null. If so, throws a {@link NullArgumentException}.
     *
     * @param value
     *            to validate
     * @return the non-null value of type {@code T}
     * @throws {@link
     *             NullArgumentException} if the value is null
     */
    public static <T> T notNull(final T value) {
        if (value == null) {
            throw new NullArgumentException(null);
        }
        return value;
    }

}
