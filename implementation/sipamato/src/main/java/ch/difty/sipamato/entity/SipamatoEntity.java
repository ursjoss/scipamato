package ch.difty.sipamato.entity;

import java.io.Serializable;

public abstract class SipamatoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Regex extending the classical \w with non-ASCII characters. To be used within a character class,<p/>
     *
     * e.g. <literal>[\\w\\u00C0-\\u024f]</literal><p/>
     *
     * Thanks to hqx5 @see http://stackoverflow.com/questions/4043307/why-this-regex-is-not-working-for-german-words
     */
    protected static final String RE_W = "\\w\\u00C0-\\u024f";

}
