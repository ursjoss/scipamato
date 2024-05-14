package ch.difty.scipamato.core.entity.code;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.AbstractDefinitionTranslation;
import ch.difty.scipamato.common.entity.FieldEnumType;

/**
 * The individual translation in a particular language of a code.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CodeTranslation extends AbstractDefinitionTranslation {
    @java.io.Serial
    private static final long serialVersionUID = 1L;

    @Nullable
    private String comment;

    public CodeTranslation(@Nullable final Integer id, @NotNull final String langCode, @Nullable final String name,
        @Nullable final String comment, @Nullable final Integer version) {
        super(id, langCode, name, version);
        setComment(comment);
    }

    public enum CodeTranslationFields implements FieldEnumType {
        COMMENT("comment");

        private final String name;

        CodeTranslationFields(final String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getFieldName() {
            return name;
        }

    }
}
