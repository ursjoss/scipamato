package ch.difty.sipamato.lib;

public final class Asserts {

    private Asserts() {
    }

    /**
     * Checks if the field is null. If so, throws a {@link NullArgumentException}.
     *
     * @param field the object to check
     * @param fieldName the name of the field
     */
    public static void notNull(Object field, String fieldName) {
        if (field == null) {
            throw new NullArgumentException(fieldName);
        }
    }

    /**
     * Checks if the field is null. If so, throws a {@link NullArgumentException}.
     *
     * @param field the object to check
     */
    public static void notNull(Object field) {
        if (field == null) {
            throw new NullArgumentException(null);
        }
    }

}
