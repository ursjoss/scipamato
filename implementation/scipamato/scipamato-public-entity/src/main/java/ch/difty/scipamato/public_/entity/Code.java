package ch.difty.scipamato.public_.entity;

import ch.difty.scipamato.common.entity.CodeLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Code extends PublicEntity implements CodeLike {

    private static final long serialVersionUID = 1L;

    public static final String CODE_CLASS_ID = "codeClassId";
    public static final String CODE = "code";
    public static final String LANG_CODE = "langCode";
    public static final String NAME = "name";
    public static final String COMMENT = "comment";
    public static final String SORT = "sort";
    public static final String DISPLAY_VALUE = "displayValue";

    private final Integer codeClassId;
    private final String code;
    private final String langCode;
    private final String name;
    private final String comment;
    private final int sort;

    public String getDisplayValue() {
        return name;
    }
}
