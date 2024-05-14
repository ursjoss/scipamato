package ch.difty.scipamato.core.entity.codeclass;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.AbstractDefinitionTranslation;
import ch.difty.scipamato.common.entity.FieldEnumType;

/**
 * The individual translation in a particular language of a code class.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CodeClassTranslation extends AbstractDefinitionTranslation {
    @java.io.Serial
    private static final long serialVersionUID = 1L;

    private String description;

    public CodeClassTranslation(@Nullable final Integer id, @NotNull final String langCode, @Nullable final String name,
        final String description, @Nullable final Integer version) {
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
