package ch.difty.scipamato.core.entity.filter;

import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SearchOrderFilter extends ScipamatoFilter {

    private static final long serialVersionUID = 1L;

    private String  nameMask;
    private Integer owner;
    private Boolean global;
    private Integer ownerIncludingGlobal;

}
