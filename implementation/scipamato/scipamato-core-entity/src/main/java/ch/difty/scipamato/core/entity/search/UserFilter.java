package ch.difty.scipamato.core.entity.search;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserFilter extends ScipamatoFilter implements Serializable {

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

        @Override
        public String getName() {
            return name;
        }
    }

}
