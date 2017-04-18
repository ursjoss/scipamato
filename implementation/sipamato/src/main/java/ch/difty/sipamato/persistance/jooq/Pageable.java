package ch.difty.sipamato.persistance.jooq;

public interface Pageable {

    Sort getSort();

    int getPageSize();

    int getOffset();

    int getPageNumber();

    Pageable next();

    boolean hasPrevious();

    Pageable first();

    Pageable previousOrFirst();

}
