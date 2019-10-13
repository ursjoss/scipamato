package ch.difty.scipamato.core.entity.codeclass;

import java.util.Arrays;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.AbstractDefinitionEntity;
import ch.difty.scipamato.common.entity.FieldEnumType;

/**
 * Entity used for managing the code classes in all defined languages.
 * This aggregate encapsulates the individual translations for all languages,
 * each captured in a {@link CodeClassTranslation}.
 * <p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CodeClassDefinition extends AbstractDefinitionEntity<CodeClassTranslation, Integer> {
    private static final long serialVersionUID = 1L;

    private Integer id;

    public CodeClassDefinition(@Nullable final Integer id, @NotNull final String mainLanguageCode,
        @Nullable final Integer version, final CodeClassTranslation... translations) {
        super(mainLanguageCode, Arrays
            .stream(translations)
            .filter(tr -> mainLanguageCode.equals(tr.getLangCode()))
            .map(CodeClassTranslation::getName)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse("n.a."), version, translations);

        this.id = id;
    }

    @NotNull
    @Override
    public Integer getNullSafeId() {
        return id;
    }

    public enum CodeClassDefinitionFields implements FieldEnumType {
        ID("id"),
        MAIN_LANG_CODE("mainLanguageCode"),
        NAME("name");

        private final String name;

        CodeClassDefinitionFields(final String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getFieldName() {
            return name;
        }

    }
}
