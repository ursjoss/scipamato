package ch.difty.scipamato.core.entity.search;

import java.io.Serializable;
import java.util.LinkedHashMap;

import org.jetbrains.annotations.Nullable;

public abstract class SearchTerms<C extends Serializable> extends LinkedHashMap<String, C> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        for (final C st : values()) {
            result = prime * result + st.hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(@Nullable final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        @SuppressWarnings("unchecked") final LinkedHashMap<String, C> other = (LinkedHashMap<String, C>) obj;
        if (values().size() != other
            .values()
            .size())
            return false;
        else
            for (final C st : values())
                if (!other.containsValue(st))
                    return false;
        return true;
    }
}
