package ch.difty.scipamato.core.entity.keyword;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;

@Data
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("SameParameterValue")
public class KeywordFilter implements ScipamatoFilter {
    @java.io.Serial
    private static final long serialVersionUID = 1L;

    private String nameMask;

    public enum KeywordFilterFields implements FieldEnumType {
        NAME_MASK("nameMask");

        private final String name;

        KeywordFilterFields(final String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getFieldName() {
            return name;
        }
    }

}
