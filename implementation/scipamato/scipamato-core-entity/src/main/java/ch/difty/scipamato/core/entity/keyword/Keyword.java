package ch.difty.scipamato.core.entity.keyword;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;

/**
 * The Keyword class represents the keyword in one
 * particular language only.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Keyword extends IdScipamatoEntity<Integer> {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;
    private String searchOverride;

    public Keyword(final Integer id, final String name, final String searchOverride) {
        setId(id);
        this.name = name;
        this.searchOverride = searchOverride;
    }

    public enum KeywordFields implements FieldEnumType {
        ID("id"),
        NAME("name"),
        SEARCH_OVERRIDE("searchOverride");

        private final String name;

        KeywordFields(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    @Override
    public String getDisplayValue() {
        return getName();
    }

}
