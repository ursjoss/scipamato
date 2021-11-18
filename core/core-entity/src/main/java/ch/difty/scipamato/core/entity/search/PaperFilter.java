package ch.difty.scipamato.core.entity.search;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.core.entity.PaperSlimFilter;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaperFilter implements ScipamatoFilter, PaperSlimFilter {

    private static final long serialVersionUID = 1L;

    private Long    number;
    private String  authorMask;
    private String  methodsMask;
    private String  populationMask;
    private String  searchMask;
    private Integer publicationYearFrom;
    private Integer publicationYearUntil;
    private Integer newsletterId;

    public enum PaperFilterFields implements FieldEnumType {
        NUMBER("number"),
        AUTHOR_MASK("authorMask"),
        METHODS_MASK("methodsMask"),
        POPULATION_MASK("populationMask"),
        SEARCH_MASK("searchMask"),
        PUB_YEAR_FROM("publicationYearFrom"),
        PUB_YEAR_UNTIL("publicationYearUntil"),
        NEWSLETTER_ID("newsletterId");

        private final String name;

        PaperFilterFields(final String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getFieldName() {
            return name;
        }
    }
}
