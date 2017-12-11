package ch.difty.scipamato.core.entity.filter;

import java.io.Serializable;

import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserFilter extends ScipamatoFilter implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String NAME_MASK = "nameMask";
    public static final String EMAIL_MASK = "emailMask";
    public static final String ENABLED = "enabled";

    private String nameMask;
    private String emailMask;
    private Boolean enabled;

}
