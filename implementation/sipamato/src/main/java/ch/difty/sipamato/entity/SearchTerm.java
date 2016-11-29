package ch.difty.sipamato.entity;

import java.io.Serializable;

import ch.difty.sipamato.lib.AssertAs;

/**
 * Implementations of {@link SearchTerm} accept a <code>key</code> and a <code>rawValue</code>.
 * They key defines a field name and the rawValue a comparison type holding a value and some
 * meta information on how to compare the key with the provided value.
 * 
 * Identity is based on key and rawValue only.
 *
 * @author u.joss
 */
public abstract class SearchTerm implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String key;
    private final String rawValue;

    SearchTerm(String key, String rawValue) {
        this.key = AssertAs.notNull(key, "key");
        this.rawValue = AssertAs.notNull(rawValue, "rawValue");
    }

    public String getKey() {
        return key;
    }

    public String getRawValue() {
        return rawValue;
    }

    @Override
    public String toString() {
        return rawValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + key.hashCode();
        result = prime * result + rawValue.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final IntegerSearchTerm other = (IntegerSearchTerm) obj;
        if (!key.equals(other.getKey()))
            return false;
        else if (!rawValue.equals(other.getRawValue()))
            return false;
        return true;
    }

}
