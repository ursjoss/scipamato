package ch.difty.scipamato.core.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.common.entity.ScipamatoEntity;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = { "createdBy", "createdByName", "createdByFullName", "lastModifiedBy",
    "lastModifiedByName" })
public abstract class CoreEntity extends ScipamatoEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Regex extending the classical \w with non-ASCII characters. To be used within
     * a character class,
     * <p>
     * e.g. {@literal [\\w\\u00C0-\\u024f]}
     * <p>
     * Thanks to hqx5 for his answer to the following <a href=
     * "http://stackoverflow.com/questions/4043307/why-this-regex-is-not-working-for-german-words">stackoverflow
     * question </a>
     */
    protected static final String RE_W = "\\w\\u00C0-\\u024f";

    /**
     * Regex extending {@link #RE_W} with dash ({@literal -}) and hyphen
     * ({@literal '}) and matching one or more of them.
     *
     * @see #RE_W
     */
    protected static final String RE_WW = "[" + RE_W + "-']+";

    /**
     * Regex extending {@link #RE_W} with dash ({@literal -}), hyphen ({@literal '})
     * and dot ({@literal .}) and matching one or more of them.
     *
     * @see #RE_W
     */
    protected static final String RE_WW2 = "[." + RE_W + "-']+";

    /**
     * Regex comprising a single of the typical 'separating' characters:
     * <ul>
     * <li>Space</li>
     * <li>Tab ({@literal \t})</li>
     * <li>Page Break ({@literal \f})</li>
     * <li>Newline ({@literal \n} (Linux), {@literal \r} (Mac) or {@literal \r\n}
     * (Windows))</li>
     * </ul>
     * Note that the Windows newlines {@literal \r\n} requires two (i.e. more than
     * one) {@literal RE_S} to match.
     */
    protected static final String RE_S = "[ \\t\\f\\r\\n]";

    private Integer createdBy;
    private Integer lastModifiedBy;

    private transient String createdByName;
    private transient String createdByFullName;
    private transient String lastModifiedByName;

    public abstract String getDisplayValue();

    public enum CoreEntityFields implements FieldEnumType {
        DISPLAY_VALUE("displayValue"),
        CREATED_DV("createdDisplayValue"),
        MODIFIED_DV("modifiedDisplayValue"),
        CREATOR_ID("createdBy"),
        CREATOR_NAME("createdByName"),
        CREATOR_FULL_NAME("createdByFullName"),
        MODIFIER_ID("lastModifiedBy"),
        MODIFIER_NAME("lastModifiedByName");

        private final String name;

        CoreEntityFields(final String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getFieldName() {
            return name;
        }
    }

    @NotNull
    public String getCreatedDisplayValue() {
        return makeDisplayValue(createdByName, getCreated());
    }

    private String makeDisplayValue(String name, LocalDateTime ldt) {
        final StringBuilder sb = new StringBuilder();
        String close = "";
        if (name != null) {
            sb.append(name);
        }
        if (ldt != null) {
            if (sb.length() > 0) {
                sb.append(" (");
                close = ")";
            }
            sb
                .append(ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .append(close);
        }
        return sb.toString();
    }

    @NotNull
    public String getModifiedDisplayValue() {
        return makeDisplayValue(lastModifiedByName, getLastModified());
    }
}
