package ch.difty.scipamato.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SciPaMaTo entity having a numeric id.
 *
 * @author u.joss
 *
 * @param <ID> type of the numeric id
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class IdScipamatoEntity<ID extends Number> extends ScipamatoEntity {

    private static final long serialVersionUID = 1L;

    public static final String ID = "id";

    private ID id;

}
