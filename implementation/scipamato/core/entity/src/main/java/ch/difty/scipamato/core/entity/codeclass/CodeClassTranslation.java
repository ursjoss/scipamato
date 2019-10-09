package ch.difty.scipamato.core.entity.codeclass;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

import ch.difty.scipamato.common.entity.AbstractDefinitionTranslation;
import ch.difty.scipamato.common.entity.FieldEnumType;

/**
 * The individual translation in a particular language of a code class.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CodeClassTranslation extends AbstractDefinitionTranslation {
    private static final long serialVersionUID = 1L;

    private String description;

    public CodeClassTranslation(final Integer id, final String langCode, final String name, final String description,
        final Integer version) {
        super(id, langCode, name, version);
        setDescription(description);
    }

    public enum CodeClassTranslationFields implements FieldEnumType {
        DESCRIPTION("description");

        private final String name;

        CodeClassTranslationFields(final String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getFieldName() {
            return name;
        }

    }

}
