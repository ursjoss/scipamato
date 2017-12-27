package ch.difty.scipamato.core.entity.filter;

import java.io.Serializable;
import java.util.LinkedHashMap;

public abstract class SearchTerms<C extends Serializable> extends LinkedHashMap<String, C> {

    private static final long serialVersionUID = 1L;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        for (C st : values()) {
            result = prime * result + st.hashCode();
        }
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
        @SuppressWarnings("unchecked")
        LinkedHashMap<String, C> other = (LinkedHashMap<String, C>) obj;
        if (values().size() != other.values()
            .size())
            return false;
        else
            for (C st : values())
                if (!other.values()
                    .contains(st))
                    return false;
        return true;
    }
}
