package ch.difty.scipamato.core.entity;

import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.CodeLike;
import ch.difty.scipamato.common.entity.FieldEnumType;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Code extends CoreEntity implements CodeLike {

    private static final long serialVersionUID = 1L;

    public static final String CODE_REGEX = "[1-9][A-Z]";

    @jakarta.validation.constraints.NotNull
    @jakarta.validation.constraints.Pattern(regexp = CODE_REGEX, message = "{code.invalidCode}")
    private final String    code;
    @jakarta.validation.constraints.NotNull
    private final String    name;
    private final String    comment;
    private final boolean   internal;
    @jakarta.validation.constraints.NotNull
    private final CodeClass codeClass;
    private final int       sort;

    public enum CodeFields implements FieldEnumType {
        CODE("code"),
        CODE_CLASS("codeClass"),
        NAME("name"),
        COMMENT("comment"),
        INTERNAL("internal"),
        SORT("sort");

        private final String name;

        CodeFields(final String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getFieldName() {
            return name;
        }
    }

    public Code(@NotNull final String code, @NotNull final String name,
        @Nullable final String comment, final boolean internal, @NotNull final Integer codeClassId,
        final @NotNull String codeClassName, @NotNull final String codeClassDescription, final int sort) {
        this(code, name, comment, internal, codeClassId, codeClassName, codeClassDescription, sort, null, null, null,
            null, null);
    }

    public Code(@NotNull final String code, @NotNull final String name,
        @Nullable final String comment, final boolean internal,
        @NotNull final Integer codeClassId, @NotNull final String codeClassName,
        @NotNull final String codeClassDescription, final int sort, @Nullable final LocalDateTime created,
        @Nullable final Integer createdBy, @Nullable final LocalDateTime lastModified,
        @Nullable final Integer lastModifiedBy, @Nullable final Integer version) {
        this.code = code;
        this.name = name;
        this.comment = comment;
        this.internal = internal;
        this.codeClass = new CodeClass(codeClassId, codeClassName, codeClassDescription);
        this.sort = sort;
        setCreated(created);
        setCreatedBy(createdBy);
        setLastModified(lastModified);
        setLastModifiedBy(lastModifiedBy);
        setVersion(version != null ? version : 0);
    }

    @SuppressWarnings("CopyConstructorMissesField")
    public Code(@NotNull final Code from) {
        this(from.code, from.name, from.comment, from.internal, new CodeClass(from.codeClass), from.sort,
            from.getCreated(), from.getCreatedBy(), from.getLastModified(), from.getLastModifiedBy(),
            from.getVersion());
    }

    private Code(@NotNull final String code, final String name, final String comment,
        final boolean internal, @NotNull final CodeClass codeClass, final int sort,
        final LocalDateTime created, final Integer createdBy, final LocalDateTime lastModified,
        final Integer lastModifiedBy, final Integer version) {
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

    @NotNull
    @Override
    public String getDisplayValue() {
        return name + " (" + code + ")";
    }
}
