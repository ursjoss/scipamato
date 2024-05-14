package ch.difty.scipamato.core.entity.code;

import java.util.Arrays;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.AbstractDefinitionEntity;
import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.core.entity.CodeClass;

/**
 * Entity used for managing the codes in all defined languages.
 * This aggregate encapsulates the individual translations for all languages,
 * each captured in a {@link CodeTranslation}.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CodeDefinition extends AbstractDefinitionEntity<CodeTranslation, String> {
    @java.io.Serial
    private static final long serialVersionUID = 1L;

    @Nullable
    private String    code;
    @Nullable
    private CodeClass codeClass;
    private int       sort;
    private boolean   internal;

    public CodeDefinition(@Nullable final String code, @NotNull final String mainLanguageCode,
        @Nullable final CodeClass codeClass, final int sort, final boolean internal, @Nullable final Integer version,
        @NotNull final CodeTranslation... translations) {
        super(mainLanguageCode, Arrays
            .stream(translations)
            .filter(tr -> mainLanguageCode.equals(tr.getLangCode()))
            .map(CodeTranslation::getName)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse("n.a."), version, translations);

        this.code = code;
        this.codeClass = codeClass;
        this.sort = sort;
        this.internal = internal;
    }

    @NotNull
    @Override
    public String getNullSafeId() {
        return code != null ? code : "n.a.";
    }

    public enum CodeDefinitionFields implements FieldEnumType {
        CODE("code"),
        MAIN_LANG_CODE("mainLanguageCode"),
        CODE_CLASS("codeClass"),
        SORT("sort"),
        INTERNAL("internal"),
        NAME("name");

        private final String name;

        CodeDefinitionFields(final String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getFieldName() {
            return name;
        }

    }
}
