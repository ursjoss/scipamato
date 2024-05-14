package ch.difty.scipamato.core.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.CodeClassLike;
import ch.difty.scipamato.common.entity.FieldEnumType;

@Getter
@EqualsAndHashCode(callSuper = true)
public class CodeClass extends IdScipamatoEntity<Integer> implements CodeClassLike {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    @jakarta.validation.constraints.NotNull
    private final String name;
    @jakarta.validation.constraints.NotNull
    private final String description;

    public enum CodeClassFields implements FieldEnumType {
        NAME("name"),
        DESCRIPTION("description");

        private final String name;

        CodeClassFields(final String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getFieldName() {
            return name;
        }
    }

    public CodeClass(@Nullable final Integer id, @NotNull final String name, @NotNull final String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    public CodeClass(@NotNull final CodeClass from) {
        this(from.getId(), from.getName(), from.getDescription());
    }

    @NotNull
    @Override
    public String toString() {
        return "CodeClass[id=" + getId() + "]";
    }

    @NotNull
    @Override
    public String getDisplayValue() {
        return getId() + " - " + name;
    }
}
