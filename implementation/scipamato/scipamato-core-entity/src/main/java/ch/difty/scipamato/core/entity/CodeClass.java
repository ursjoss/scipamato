package ch.difty.scipamato.core.entity;

import javax.validation.constraints.NotNull;

import ch.difty.scipamato.common.entity.CodeClassLike;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class CodeClass extends IdScipamatoEntity<Integer> implements CodeClassLike {

    private static final long serialVersionUID = 1L;

    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";

    @NotNull
    private final String name;
    @NotNull
    private final String description;

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
        StringBuilder builder = new StringBuilder();
        builder.append("CodeClass[id=");
        builder.append(getId());
        builder.append("]");
        return builder.toString();
    }

    @Override
    public String getDisplayValue() {
        return name;
    }

}
