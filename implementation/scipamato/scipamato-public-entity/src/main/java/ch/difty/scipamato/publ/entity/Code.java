package ch.difty.scipamato.publ.entity;

import ch.difty.scipamato.common.entity.CodeLike;
import ch.difty.scipamato.common.entity.FieldEnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Code extends PublicEntity implements CodeLike {

    private static final long serialVersionUID = 1L;

    private final Integer codeClassId;
    private final String  code;
    private final String  langCode;
    private final String  name;
    private final String  comment;
    private final int     sort;

    public String getDisplayValue() {
        return name;
    }

    public enum CodeFields implements FieldEnumType {
        CODE_CLASS_ID("codeClassId"),
        CODE("code"),
        LANG_CODE("langCode"),
        NAME("name"),
        COMMENT("comment"),
        SORT("sort"),
        DISPLAY_VALUE("displayValue");

        private final String name;

        CodeFields(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

}
