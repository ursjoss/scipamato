package ch.difty.scipamato.core.web.paper.jasper;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class JasperEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    protected String na(@Nullable final String s) {
        return s != null ? s : "";
    }

    /**
     * Provides the (non-null) label, but only if the associated value is neither
     * null nor blank.
     *
     * @param label
     *     the label to display (if it is non-null)
     * @param value
     *     the value to test against if it is null or blank to not show the
     *     label
     * @return label, never null
     */
    @NotNull
    String na(@Nullable final String label, @Nullable String value) {
        if (value == null || value.isEmpty())
            return "";
        else
            return label != null ? label : "";
    }

    /**
     * Provides the label, but only if the associated value is neither null nor
     * blank.
     *
     * @param label
     *     the label to display
     * @param value
     *     the value to test against if it is null or blank to not show the
     *     label
     * @return label, never null
     */
    @NotNull
    protected String na2(@NotNull final String label, @Nullable String value) {
        if (value == null || value.isEmpty())
            return "";
        else
            return label;
    }
}
