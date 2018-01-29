package ch.difty.scipamato.core.entity.search;

import java.io.Serializable;
import java.util.LinkedHashMap;

public abstract class SearchTerms<C extends Serializable> extends LinkedHashMap<String, C> {

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
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        @SuppressWarnings("unchecked")
        final LinkedHashMap<String, C> other = (LinkedHashMap<String, C>) obj;
        if (values().size() != other.values()
            .size())
            return false;
        else
            for (final C st : values())
                if (!other.values()
                    .contains(st))
                    return false;
        return true;
    }
}
