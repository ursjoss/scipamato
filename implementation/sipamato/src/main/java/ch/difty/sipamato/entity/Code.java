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
    private String name;

    @NotNull
    private CodeClass codeClass;

    public Code(String code, String name, Integer codeClassId, String codeClassName, String codeClassDescription) {
        this.code = code;
        this.name = name;
        if (codeClassId != null)
            this.codeClass = new CodeClass(codeClassId, codeClassName, codeClassDescription);
    }

    public Code(final Code from) {
        this(from.code, from.name, new CodeClass(from.codeClass));
    }

    private Code(String code, String name, CodeClass codeClass) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CodeClass getCodeClass() {
        return codeClass;
    }

    public void setCodeClass(CodeClass codeClass) {
        this.codeClass = codeClass;
    }

}
