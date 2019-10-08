package ch.difty.scipamato.core.entity.code;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.core.entity.CodeClass;

@Data
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("SameParameterValue")
public class CodeFilter extends ScipamatoFilter {
    private static final long serialVersionUID = 1L;

    private CodeClass codeClass;
    private String    nameMask;
    private String    commentMask;
    private Boolean   internal;

    public enum CodeFilterFields implements FieldEnumType {
        CODE_CLASS("codeClass"),
        NAME_MASK("nameMask"),
        COMMENT_MASK("commentMask"),
        INTERNAL("internal");

        private final String name;

        CodeFilterFields(final String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getFieldName() {
            return name;
        }
    }

}
