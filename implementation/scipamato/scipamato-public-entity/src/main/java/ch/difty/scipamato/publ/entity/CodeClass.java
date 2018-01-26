package ch.difty.scipamato.publ.entity;

import ch.difty.scipamato.common.entity.CodeClassLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CodeClass extends PublicEntity implements CodeClassLike {

    private static final long serialVersionUID = 1L;

    public static final String CODE_CLASS_ID = "codeClassId";
    public static final String LANG_CODE     = "langCode";
    public static final String NAME          = "name";
    public static final String DESCRIPTION   = "description";

    private final Integer codeClassId;
    private final String  langCode;
    private final String  name;
    private final String  description;
}
