package ch.difty.scipamato.common.persistence.paging;

import org.mockito.ArgumentMatcher;

public class PaginationContextMatcher extends ArgumentMatcher<PaginationContext> {

    private final int offset;
    private final int pageSize;
    private final String sort;

    public PaginationContextMatcher(int offset, int pageSize, String sort) {
        this.offset = offset;
        this.pageSize = pageSize;
        this.sort = sort;
    }

    @Override
    public boolean matches(Object argument) {
        if (argument != null && argument instanceof PaginationContext) {
            PaginationContext p = (PaginationContext) argument;
            return p.getOffset() == offset && p.getPageSize() == pageSize && sort.equals(p.getSort().toString());
        }
        return false;
    }
}