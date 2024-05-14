package ch.difty.scipamato.core.entity.codeclass;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;

@Data
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("SameParameterValue")
public class CodeClassFilter implements ScipamatoFilter {
    @java.io.Serial
    private static final long serialVersionUID = 1L;

    private String nameMask;
    private String descriptionMask;

    public enum CodeClassFilterFields implements FieldEnumType {
        NAME_MASK("nameMask"),
        DESCRIPTION_MASK("descriptionMask");

        private final String name;

        CodeClassFilterFields(final String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getFieldName() {
            return name;
        }
    }

}
