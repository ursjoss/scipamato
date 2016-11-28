package ch.difty.sipamato.entity;

import java.io.Serializable;

/**
 * Helper class to capture boolean specific search terms
 */
public class BooleanSearchTerm implements Serializable {
    private static final long serialVersionUID = 1L;

    public final String key;
    public final boolean rawValue;

    BooleanSearchTerm(final String key, final boolean rawValue) {
        this.key = key;
        this.rawValue = rawValue;
    }

    @Override
    public String toString() {
        return key;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + (rawValue ? 1231 : 1237);
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
        BooleanSearchTerm other = (BooleanSearchTerm) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (rawValue != other.rawValue)
            return false;
        return true;
    }
}