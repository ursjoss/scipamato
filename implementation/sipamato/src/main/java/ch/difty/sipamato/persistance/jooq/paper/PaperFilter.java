package ch.difty.sipamato.persistance.jooq.paper;

import ch.difty.sipamato.entity.filter.PaperSlimFilter;
import ch.difty.sipamato.entity.filter.SipamatoFilter;

public class PaperFilter extends SipamatoFilter implements PaperSlimFilter {

    private static final long serialVersionUID = 1L;

    public static final String AUTHOR_MASK = "authorMask";
    public static final String METHODS_MASK = "methodsMask";
    public static final String SEARCH_MASK = "searchMask";
    public static final String PUB_YEAR_FROM = "publicationYearFrom";
    public static final String PUB_YEAR_UNTIL = "publicationYearUntil";

    private String authorMask;
    private String methodsMask;
    private String searchMask;
    private Integer publicationYearFrom;
    private Integer publicationYearUntil;

    public String getAuthorMask() {
        return authorMask;
    }

    public void setAuthorMask(String authorMask) {
        this.authorMask = authorMask;
    }

    public String getMethodsMask() {
        return methodsMask;
    }

    public void setMethodsMask(String methodsMask) {
        this.methodsMask = methodsMask;
    }

    public String getSearchMask() {
        return searchMask;
    }

    public void setSearchMask(String searchMask) {
        this.searchMask = searchMask;
    }

    public Integer getPublicationYearFrom() {
        return publicationYearFrom;
    }

    public void setPublicationYearFrom(Integer publicationYearFrom) {
        this.publicationYearFrom = publicationYearFrom;
    }

    public Integer getPublicationYearUntil() {
        return publicationYearUntil;
    }

    public void setPublicationYearUntil(Integer publicationYearUntil) {
        this.publicationYearUntil = publicationYearUntil;
    }

}
