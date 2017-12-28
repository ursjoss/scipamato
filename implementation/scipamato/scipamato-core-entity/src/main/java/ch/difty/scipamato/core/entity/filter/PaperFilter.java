package ch.difty.scipamato.core.entity.filter;

import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaperFilter extends ScipamatoFilter implements PaperSlimFilter {

    private static final long serialVersionUID = 1L;

    public static final String NUMBER         = "number";
    public static final String AUTHOR_MASK    = "authorMask";
    public static final String METHODS_MASK   = "methodsMask";
    public static final String SEARCH_MASK    = "searchMask";
    public static final String PUB_YEAR_FROM  = "publicationYearFrom";
    public static final String PUB_YEAR_UNTIL = "publicationYearUntil";

    private Long    number;
    private String  authorMask;
    private String  methodsMask;
    private String  searchMask;
    private Integer publicationYearFrom;
    private Integer publicationYearUntil;

}
