package ch.difty.scipamato.persistance;

import java.util.Arrays;

import javax.cache.annotation.CacheInvocationParameter;
import javax.cache.annotation.GeneratedCacheKey;

import ch.difty.scipamato.lib.AssertAs;

public class DefaultGeneratedCacheKey implements GeneratedCacheKey {
    private static final long serialVersionUID = 1L;

    private final Object values[];
    private final int hashCode;

    public DefaultGeneratedCacheKey(final Object[] keyValues) {
        values = AssertAs.notNull(keyValues, "keyValues");
        hashCode = Arrays.deepHashCode(values);
    }

    DefaultGeneratedCacheKey(final CacheInvocationParameter[] cips) {
        AssertAs.notNull(cips, "cips");
        values = new Object[cips.length];
        for (int i = 0; i < values.length; i++)
            values[i] = cips[i].getValue();
        hashCode = Arrays.deepHashCode(values);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (!obj.getClass().equals(this.getClass()))
            return false;
        if (this.hashCode != obj.hashCode())
            return false;

        final DefaultGeneratedCacheKey other = (DefaultGeneratedCacheKey) obj;
        if (Arrays.deepEquals(this.values, other.values))
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

}