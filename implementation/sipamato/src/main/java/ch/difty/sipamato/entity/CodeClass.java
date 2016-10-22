package ch.difty.sipamato.entity;

import javax.validation.constraints.NotNull;

public class CodeClass extends SipamatoEntity {

    private static final long serialVersionUID = 1L;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";

    @NotNull
    private Integer id;
    @NotNull
    private String name;
    @NotNull
    private String description;

    public CodeClass() {
    }

    public CodeClass(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public CodeClass(CodeClass from) {
        this(from.id, from.name, from.description);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
