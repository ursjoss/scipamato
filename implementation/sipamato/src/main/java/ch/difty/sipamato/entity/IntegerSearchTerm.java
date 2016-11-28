package ch.difty.sipamato.entity;

import java.io.Serializable;

/**
 * Helper class to capture integer specific search terms
 */
public class IntegerSearchTerm implements Serializable {
    private static final long serialVersionUID = 1L;

    public final String key;
    public final String rawValue;
    public final IntegerSearchTermType type;
    public final int value;
    public final int value2;

    IntegerSearchTerm(final String key, final String value) {
        this.key = key;
        this.rawValue = value;
        final String rv = value.trim();
        if (rv.length() > 2 && rv.startsWith(">=")) {
            this.type = IntegerSearchTermType.GREATER_OR_EQUAL;
            this.value = Integer.parseInt(rv.substring(2, rv.length()).trim());
            this.value2 = this.value;
        } else if (rv.length() > 1 && rv.startsWith(">")) {
            this.type = IntegerSearchTermType.GREATER_THAN;
            this.value = Integer.parseInt(rv.substring(1, rv.length()).trim());
            this.value2 = this.value;
        } else if (rv.length() > 2 && rv.startsWith("<=")) {
            this.type = IntegerSearchTermType.LESS_OR_EQUAL;
            this.value = Integer.parseInt(rv.substring(2, rv.length()).trim());
            this.value2 = this.value;
        } else if (rv.length() > 1 && rv.startsWith("<")) {
            this.type = IntegerSearchTermType.LESS_THAN;
            this.value = Integer.parseInt(rv.substring(1, rv.length()).trim());
            this.value2 = this.value;
        } else if (rv.length() > 1 && rv.startsWith("=")) {
            this.type = IntegerSearchTermType.EXACT;
            this.value = Integer.parseInt(rv.substring(1, rv.length()).trim());
            this.value2 = this.value;
        } else if (rv.length() > 1 && rv.contains("-")) {
            this.type = IntegerSearchTermType.RANGE;
            this.value = Integer.parseInt(rv.substring(0, rv.indexOf("-")).trim());
            this.value2 = Integer.parseInt(rv.substring(rv.indexOf("-") + 1, rv.length()).trim());
        } else {
            this.type = IntegerSearchTermType.EXACT;
            this.value = Integer.parseInt(rv.trim());
            this.value2 = this.value;
        }
    }

    @Override
    public String toString() {
        return rawValue;
    }

    public IntegerSearchTermType getType() {
        return type;
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

    public enum IntegerSearchTermType {
        EXACT,
        GREATER_THAN,
        GREATER_OR_EQUAL,
        LESS_THAN,
        LESS_OR_EQUAL,
        RANGE;
    }

}