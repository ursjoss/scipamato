package ch.difty.scipamato.web.jasper;

import java.io.Serializable;

import ch.difty.scipamato.NullArgumentException;

public abstract class JasperEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String na(final String s) {
        return s != null ? s : "";
    }

    /**
     * Provides the (non-null) label, but only if the associated value is neither null nor blank.
     * @param label the label to display (if it is non-null)
     * @param value the value to test against if it is null or blank to not show the label
     * @return label, never null
     */
    protected String na(final String label, String value) {
        if (value == null || value.isEmpty())
            return "";
        else
            return label != null ? label : "";
    }

    /**
     * Provides the label, but only if the associated value is neither null nor blank.
     * @param label the label to display
     * @param value the value to test against if it is null or blank to not show the label
     * @throws NullArgumentException in case of a null label
     * @return label, never null
     */
    protected String na2(final String label, String value) {
        if (label == null)
            throw new NullArgumentException("label");
        if (value == null || value.isEmpty())
            return "";
        else
            return label;
    }

}
