package ch.difty.sipamato.entity;

import java.io.Serializable;

/**
 * Helper class to capture integer specific search terms
 */
public class IntegerSearchTerm implements Serializable {
    private static final long serialVersionUID = 1L;

    public final String key;
    public final String rawValue;

    IntegerSearchTerm(final String key, final String value) {
        this.key = key;
        this.rawValue = value;
    }

    @Override
    public String toString() {
        return rawValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((rawValue == null) ? 0 : rawValue.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IntegerSearchTerm other = (IntegerSearchTerm) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (rawValue == null) {
            if (other.rawValue != null)
                return false;
        } else if (!rawValue.equals(other.rawValue))
            return false;
        return true;
    }
}