package ch.difty.scipamato.core.entity;

import javax.validation.constraints.NotNull;

import ch.difty.scipamato.common.entity.CodeClassLike;
import ch.difty.scipamato.common.entity.FieldEnumType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class CodeClass extends IdScipamatoEntity<Integer> implements CodeClassLike {

    private static final long serialVersionUID = 1L;

    @NotNull
    private final String name;
    @NotNull
    private final String description;

    public enum CodeClassFields implements FieldEnumType {
        NAME("name"),
        DESCRIPTION("description");

        private final String name;

        CodeClassFields(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public CodeClass(final Integer id, final String name, final String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    public CodeClass(final CodeClass from) {
        this(from.getId(), from.getName(), from.getDescription());
    }

    @Override
    public String toString() {
        return "CodeClass[id=" + getId() + "]";
    }

    @Override
    public String getDisplayValue() {
        return name;
    }

}
