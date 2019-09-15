package ch.difty.scipamato.publ.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import ch.difty.scipamato.common.entity.CodeClassLike;
import ch.difty.scipamato.common.entity.FieldEnumType;

@Value
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CodeClass extends PublicEntity implements CodeClassLike {

    private static final long serialVersionUID = 1L;

    private final Integer codeClassId;
    private final String  langCode;
    private final String  name;
    private final String  description;

    public enum CodeClassFields implements FieldEnumType {
        CODE_CLASS_ID("codeClassId"),
        LANG_CODE("langCode"),
        NAME("name"),
        DESCRIPTION("description");

        private final String name;

        CodeClassFields(final String name) {
            this.name = name;
        }

        @Override
        public String getFieldName() {
            return name;
        }
    }

}
