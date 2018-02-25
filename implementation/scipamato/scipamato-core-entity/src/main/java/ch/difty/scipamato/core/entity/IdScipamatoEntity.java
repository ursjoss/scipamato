package ch.difty.scipamato.core.entity;

import ch.difty.scipamato.common.entity.FieldEnumType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SciPaMaTo entity having a numeric id.
 *
 * @author u.joss
 *
 * @param <ID>
 *            type of the numeric id
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class IdScipamatoEntity<ID extends Number> extends CoreEntity {

    private static final long serialVersionUID = 1L;

    private ID id;

    public enum IdScipamatoEntityFields implements FieldEnumType {
        ID("id");

        private String name;

        IdScipamatoEntityFields(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

}
