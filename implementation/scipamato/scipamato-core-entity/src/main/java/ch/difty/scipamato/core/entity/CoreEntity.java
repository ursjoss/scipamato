package ch.difty.scipamato.core.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import ch.difty.scipamato.common.entity.ScipamatoEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = { "createdBy", "lastModifiedBy" })
public abstract class CoreEntity extends ScipamatoEntity {

    private static final long serialVersionUID = 1L;

    public static final String DISPLAY_VALUE = "displayValue";

    /**
     * Regex extending the classical \w with non-ASCII characters. To be used within
     * a character class,
     * <p>
     *
     * e.g. {@literal [\\w\\u00C0-\\u024f]}
     * <p>
     *
     * Thanks to hqx5 @see <a href=
     * "http://stackoverflow.com/questions/4043307/why-this-regex-is-not-working-for-german-words">stackoverflow</a>
     */
    protected static final String RE_W   = "\\w\\u00C0-\\u024f";
    protected static final String RE_WW  = "[" + RE_W + "-']+";
    protected static final String RE_WW2 = "[." + RE_W + "-']+";
    // White Space
    protected static final String RE_S = "[ \\t\\f\\r\\n]";

    public static final String CREATED_DV        = "createdDisplayValue";
    public static final String MODIFIED_DV       = "modifiedDisplayValue";
    public static final String CREATOR_ID        = "createdBy";
    public static final String CREATOR_NAME      = "createdByName";
    public static final String CREATOR_FULL_NAME = "createdByFullName";
    public static final String MODIFIER_ID       = "lastModifiedBy";
    public static final String MODIFIER_NAME     = "lastModifiedByName";

    private Integer createdBy;
    private Integer lastModifiedBy;

    private transient String createdByName;
    private transient String createdByFullName;
    private transient String lastModifiedByName;

    public abstract String getDisplayValue();

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
            sb.append(ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .append(close);
        }
        return sb.toString();
    }

    public String getModifiedDisplayValue() {
        return makeDisplayValue(lastModifiedByName, getLastModified());
    }
}
