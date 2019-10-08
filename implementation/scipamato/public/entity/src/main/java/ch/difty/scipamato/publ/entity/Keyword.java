package ch.difty.scipamato.publ.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.entity.FieldEnumType;

@Value
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Keyword extends PublicEntity {

    private static final long serialVersionUID = 1L;

    private final int    id;
    private final int    keywordId;
    private final String langCode;
    private final String name;
    private final String searchOverride;

    public String getDisplayValue() {
        return name;
    }

    public enum KeywordFields implements FieldEnumType {
        ID("id"),
        KEYWORD_ID("keywordId"),
        LANG_CODE("langCode"),
        NAME("name"),
        SEARCH_OVERRIDE("searchOverride"),
        DISPLAY_VALUE("displayValue");

        private final String name;

        KeywordFields(final String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getFieldName() {
            return name;
        }
    }

}
