package ch.difty.sipamato.entity;

import java.io.Serializable;

/**
 * Helper class to capture integer specific search terms
 */
public class IntegerSearchTerm implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String key;
    private final String rawValue;
    private final MatchType type;
    private final int value;
    private final int value2;

    IntegerSearchTerm(final String key, final String value) {
        this.key = key;
        this.rawValue = value;
        final String rv = value.trim();
        if (rv.length() > 2 && rv.startsWith(">=")) {
            this.type = MatchType.GREATER_OR_EQUAL;
            this.value = Integer.parseInt(rv.substring(2, rv.length()).trim());
            this.value2 = this.value;
        } else if (rv.length() > 1 && rv.startsWith(">")) {
            this.type = MatchType.GREATER_THAN;
            this.value = Integer.parseInt(rv.substring(1, rv.length()).trim());
            this.value2 = this.value;
        } else if (rv.length() > 2 && rv.startsWith("<=")) {
            this.type = MatchType.LESS_OR_EQUAL;
            this.value = Integer.parseInt(rv.substring(2, rv.length()).trim());
            this.value2 = this.value;
        } else if (rv.length() > 1 && rv.startsWith("<")) {
            this.type = MatchType.LESS_THAN;
            this.value = Integer.parseInt(rv.substring(1, rv.length()).trim());
            this.value2 = this.value;
        } else if (rv.length() > 1 && rv.startsWith("=")) {
            this.type = MatchType.EXACT;
            this.value = Integer.parseInt(rv.substring(1, rv.length()).trim());
            this.value2 = this.value;
        } else if (rv.length() > 1 && rv.contains("-")) {
            this.type = MatchType.RANGE;
            this.value = Integer.parseInt(rv.substring(0, rv.indexOf("-")).trim());
            this.value2 = Integer.parseInt(rv.substring(rv.indexOf("-") + 1, rv.length()).trim());
        } else {
            this.type = MatchType.EXACT;
            this.value = Integer.parseInt(rv.trim());
            this.value2 = this.value;
        }
    }

    public String getKey() {
        return key;
    }

    public String getRawValue() {
        return rawValue;
    }

    public int getValue() {
        return value;
    }

    public int getValue2() {
        return value2;
    }

    public MatchType getType() {
        return type;
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
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final IntegerSearchTerm other = (IntegerSearchTerm) obj;
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

    public enum MatchType {
        EXACT,
        GREATER_THAN,
        GREATER_OR_EQUAL,
        LESS_THAN,
        LESS_OR_EQUAL,
        RANGE;
    }

}