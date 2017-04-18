package ch.difty.sipamato.persistance.jooq;

import java.util.Iterator;
import java.util.List;

public class PageImpl<T> implements Page<T> {

    public PageImpl(List<T> content, Pageable pageable, long total) {
    }

    public PageImpl(List<T> asList) {
        // TODO Auto-generated constructor stub
    }

    @Override
    public Iterator<T> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<T> getContent() {
        // TODO Auto-generated method stub
        return null;
    }
}
