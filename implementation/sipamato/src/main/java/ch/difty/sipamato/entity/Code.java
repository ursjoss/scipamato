package ch.difty.sipamato.entity;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Code extends SipamatoEntity {

    private static final long serialVersionUID = 1L;

    public static final String CODE_REGEX = "[1-9][A-Z]";

    public static final String CODE = "code";
    public static final String CODE_CLASS = "codeClass";
    public static final String NAME = "name";
    public static final String COMMENT = "comment";
    public static final String INTERNAL = "internal";
    public static final String SORT = "sort";

    @NotNull
    @Pattern(regexp = CODE_REGEX, message = "{code.invalidCode}")
    private final String code;

    @NotNull
    private final String name;

    private final String comment;

    private final boolean internal;

    @NotNull
    private final CodeClass codeClass;

    private final int sort;

    public Code(final String code, final String name, final String comment, final boolean internal, final Integer codeClassId, final String codeClassName, final String codeClassDescription,
            final int sort) {
        this(code, name, comment, internal, codeClassId, codeClassName, codeClassDescription, sort, null, null, null, null, null);
    }

    public Code(final String code, final String name, final String comment, final boolean internal, final Integer codeClassId, final String codeClassName, final String codeClassDescription,
            final int sort, final LocalDateTime created, final Integer createdBy, final LocalDateTime lastModified, final Integer lastModifiedBy, final Integer version) {
        this.code = code;
        this.name = name;
        this.comment = comment;
        this.internal = internal;
        if (codeClassId != null)
            this.codeClass = new CodeClass(codeClassId, codeClassName, codeClassDescription);
        else
            this.codeClass = null;
        this.sort = sort;
        setCreated(created);
        setCreatedBy(createdBy);
        setLastModified(lastModified);
        setLastModifiedBy(lastModifiedBy);
        setVersion(version != null ? version : 0);
    }

    public Code(final Code from) {
        this(from.code, from.name, from.comment, from.internal, new CodeClass(from.codeClass), from.sort, from.getCreated(), from.getCreatedBy(), from.getLastModified(), from.getLastModifiedBy(),
                from.getVersion());
    }

    private Code(final String code, final String name, final String comment, final boolean internal, final CodeClass codeClass, final int sort, final LocalDateTime created, final Integer createdBy,
            final LocalDateTime lastModified, final Integer lastModifiedBy, final Integer version) {
        this.code = code;
        this.name = name;
        this.comment = comment;
        this.internal = internal;
        this.codeClass = codeClass;
        this.sort = sort;
        setCreated(created);
        setCreatedBy(createdBy);
        setLastModified(lastModified);
        setLastModifiedBy(lastModifiedBy);
        setVersion(version);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public boolean isInternal() {
        return internal;
    }

    public CodeClass getCodeClass() {
        return codeClass;
    }

    public int getSort() {
        return sort;
    }

    @Override
    public String getDisplayValue() {
        final StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" (").append(code).append(")");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((codeClass == null) ? 0 : codeClass.hashCode());
        result = prime * result + ((comment == null) ? 0 : comment.hashCode());
        result = prime * result + (internal ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + sort;
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
        Code other = (Code) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (codeClass == null) {
            if (other.codeClass != null)
                return false;
        } else if (!codeClass.equals(other.codeClass))
            return false;
        if (comment == null) {
            if (other.comment != null)
                return false;
        } else if (!comment.equals(other.comment))
            return false;
        if (internal != other.internal)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (sort != other.sort)
            return false;
        return true;
    }

}
