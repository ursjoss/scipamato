package ch.difty.scipamato.core.entity.keyword;

import lombok.Data;
import lombok.EqualsAndHashCode;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.entity.FieldEnumType;

/**
 * The individual translation in a particular language of a keyword.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KeywordTranslation extends Keyword {
    private static final long serialVersionUID = 1L;

    private final String langCode;

    public KeywordTranslation(final Integer id, final String langCode, final String name, final String searchOverride,
        final Integer version) {
        super(id, name, searchOverride);
        this.langCode = AssertAs.notNull(langCode, "langCode");
        setVersion(version != null ? version : 0);
    }

    public enum KeywordTranslationFields implements FieldEnumType {
        ID("id"),
        LANG_CODE("langCode"),
        NAME("name"),
        SEARCH_OVERRIDE("searchOverride");

        private final String name;

        KeywordTranslationFields(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

    }

    @Override
    public String getDisplayValue() {
        return getLangCode() + ": " + getName();
    }

}
