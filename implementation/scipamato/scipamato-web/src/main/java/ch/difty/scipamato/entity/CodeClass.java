package ch.difty.scipamato.entity;

import javax.validation.constraints.NotNull;

public class CodeClass extends IdScipamatoEntity<Integer> {

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

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CodeClass other = (CodeClass) obj;
        if (getId() == null) {
            if (other.getId() != null)
                return false;
        } else if (!getId().equals(other.getId()))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        return true;
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
