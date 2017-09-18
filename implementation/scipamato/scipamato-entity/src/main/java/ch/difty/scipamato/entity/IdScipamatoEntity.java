package ch.difty.scipamato.entity;

/**
 * SciPaMaTo entity having a numeric id.
 *
 * @author u.joss
 *
 * @param <ID> type of the numeric id
 */
public abstract class IdScipamatoEntity<ID extends Number> extends ScipamatoEntity {

    private static final long serialVersionUID = 1L;

    public static final String ID = "id";

    private ID id;

    public IdScipamatoEntity() {
    }

    public IdScipamatoEntity(final ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

}
