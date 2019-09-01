package ch.difty.scipamato.core.entity.code;

import lombok.Data;
import lombok.EqualsAndHashCode;

import ch.difty.scipamato.common.entity.AbstractDefinitionTranslation;
import ch.difty.scipamato.common.entity.FieldEnumType;

/**
 * The individual translation in a particular language of a code.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CodeTranslation extends AbstractDefinitionTranslation {
    private static final long serialVersionUID = 1L;

    private String comment;

    public CodeTranslation(final Integer id, final String langCode, final String name, final String comment,
        final Integer version) {
        super(id, langCode, name, version);
        setComment(comment);
    }

    public enum CodeTranslationFields implements FieldEnumType {
        COMMENT("comment");

        private final String name;

        CodeTranslationFields(final String name) {
            this.name = name;
        }

        @Override
        public String getFieldName() {
            return name;
        }

    }

}
