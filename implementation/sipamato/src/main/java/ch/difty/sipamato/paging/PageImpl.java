package ch.difty.sipamato.paging;

import java.util.Iterator;
import java.util.List;

@Deprecated
public class PageImpl<T> implements Page<T> {

    public PageImpl(List<T> asList) {
        // TODO Auto-generated constructor stub
    }

    public PageImpl(List<T> content, Pageable pageable, long total) {
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
