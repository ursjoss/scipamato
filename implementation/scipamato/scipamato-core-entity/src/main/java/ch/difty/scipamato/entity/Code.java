package ch.difty.scipamato.entity;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import ch.difty.scipamato.AssertAs;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Code extends CoreEntity {

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
        this.code = AssertAs.notNull(code, "code");
        this.name = name;
        this.comment = comment;
        this.internal = internal;
        this.codeClass = new CodeClass(AssertAs.notNull(codeClassId, "codeClassId"), codeClassName, codeClassDescription);
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
        this.code = AssertAs.notNull(code, CODE);
        this.name = name;
        this.comment = comment;
        this.internal = internal;
        this.codeClass = AssertAs.notNull(codeClass, CODE_CLASS);
        this.sort = sort;
        setCreated(created);
        setCreatedBy(createdBy);
        setLastModified(lastModified);
        setLastModifiedBy(lastModifiedBy);
        setVersion(version);
    }

    @Override
    public String getDisplayValue() {
        final StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" (").append(code).append(")");
        return sb.toString();
    }

}
