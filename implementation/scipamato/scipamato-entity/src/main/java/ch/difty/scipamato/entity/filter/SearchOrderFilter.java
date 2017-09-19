package ch.difty.scipamato.entity.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchOrderFilter extends ScipamatoFilter {

    private static final long serialVersionUID = 1L;

    private String nameMask;
    private Integer owner;
    private Boolean global;
    private Integer ownerIncludingGlobal;

}
