package ch.difty.sipamato.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public abstract class SipamatoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DISPLAY_VALUE = "displayValue";

    /**
     * Regex extending the classical \w with non-ASCII characters. To be used within a character class,<p/>
     *
     * e.g. <literal>[\\w\\u00C0-\\u024f]</literal><p/>
     *
     * Thanks to hqx5 @see http://stackoverflow.com/questions/4043307/why-this-regex-is-not-working-for-german-words
     */
    protected static final String RE_W = "\\w\\u00C0-\\u024f";

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public abstract String getDisplayValue();
}
