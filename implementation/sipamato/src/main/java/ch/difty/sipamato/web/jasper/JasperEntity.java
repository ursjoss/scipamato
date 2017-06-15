package ch.difty.sipamato.web.jasper;

import java.io.Serializable;

public abstract class JasperEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String na(final String s) {
        return s != null ? s : "";
    }

    /**
     * Returns the label with the real value if present or a default value if null.
     * However, if the associated value is blank or null, the label is blank as well.
     * @param label
     * @param value
     * @return non-null label
     */
    protected String na(final String label, String value) {
        if (!value.isEmpty())
            return label != null ? label : "";
        else
            return "";
    }

}
