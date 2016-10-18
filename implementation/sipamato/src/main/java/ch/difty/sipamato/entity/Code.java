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
    private String code;

    @NotNull
    private CodeClass codeClass;

    @NotNull
    private String name;

    public Code() {
    }

    public Code(String code, CodeClass codeClass, String name) {
        this.code = code;
        this.codeClass = codeClass;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CodeClass getCodeClass() {
        return codeClass;
    }

    public void setCodeClass(CodeClass codeClass) {
        this.codeClass = codeClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
