package ch.difty.sipamato.paging;

@Deprecated
public interface Pageable {

    Pageable first();

    Pageable previousOrFirst();

    Pageable next();

    boolean hasPrevious();

    Sort getSort();

    int getPageSize();

    int getOffset();

    int getPageNumber();

}
