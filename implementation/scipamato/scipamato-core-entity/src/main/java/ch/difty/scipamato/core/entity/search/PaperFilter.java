package ch.difty.scipamato.core.entity.search;

import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.core.entity.PaperSlimFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaperFilter extends ScipamatoFilter implements PaperSlimFilter {

    private static final long serialVersionUID = 1L;

    private Long    number;
    private String  authorMask;
    private String  methodsMask;
    private String  searchMask;
    private Integer publicationYearFrom;
    private Integer publicationYearUntil;

    public enum PaperFilterFields implements FieldEnumType {
        NUMBER("number"),
        AUTHOR_MASK("authorMask"),
        METHODS_MASK("methodsMask"),
        SEARCH_MASK("searchMask"),
        PUB_YEAR_FROM("publicationYearFrom"),
        PUB_YEAR_UNTIL("publicationYearUntil");

        private String name;

        PaperFilterFields(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

}
