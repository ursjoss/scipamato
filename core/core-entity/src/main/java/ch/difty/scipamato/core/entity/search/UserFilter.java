package ch.difty.scipamato.core.entity.search;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserFilter implements ScipamatoFilter {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    private String  nameMask;
    private String  emailMask;
    private Boolean enabled;

    public enum UserFilterFields implements FieldEnumType {
        NAME_MASK("nameMask"),
        EMAIL_MASK("emailMask"),
        ENABLED("enabled");

        private final String name;

        UserFilterFields(final String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getFieldName() {
            return name;
        }
    }
}
