package ch.difty.sipamato.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Code extends SipamatoEntity {

    private static final long serialVersionUID = 1L;

    public static final String CODE_REGEX = "[1-9][A-Z]";

    public static final String CODE = "code";
    public static final String CODE_CLASS = "codeClass";
    public static final String NAME = "name";

    @NotNull
    @Pattern(regexp = CODE_REGEX, message = "{code.invalidCode}")
    private final String code;

    @NotNull
    private final String name;

    @NotNull
    private final CodeClass codeClass;

    public Code(final String code, final String name, final Integer codeClassId, final String codeClassName, final String codeClassDescription) {
        this.code = code;
        this.name = name;
        if (codeClassId != null)
            this.codeClass = new CodeClass(codeClassId, codeClassName, codeClassDescription);
        else
            this.codeClass = null;
    }

    public Code(final Code from) {
        this(from.code, from.name, new CodeClass(from.codeClass));
    }

    private Code(final String code, final String name, final CodeClass codeClass) {
        this.code = code;
        this.codeClass = codeClass;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public CodeClass getCodeClass() {
        return codeClass;
    }

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
        Code other = (Code) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (codeClass == null) {
            if (other.codeClass != null)
                return false;
        } else if (!codeClass.equals(other.codeClass))
            return false;
        return true;
    }

}
