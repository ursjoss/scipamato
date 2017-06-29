package ch.difty.scipamato.web.jasper;

import java.io.Serializable;

public abstract class JasperEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String na(final String s) {
        return s != null ? s : "";
    }

    /**
     * Has the goal to provide a non-null label, but only if the associated value is neither null nor blank.
     * @param label the label that shall be displayed if we have a valid value
     * @param value the value to test against if present
     * @return non-null label
     */
    protected String na(final String label, String value) {
        if (value == null || value.isEmpty())
            return "";
        else
            return label != null ? label : "";
    }

}
