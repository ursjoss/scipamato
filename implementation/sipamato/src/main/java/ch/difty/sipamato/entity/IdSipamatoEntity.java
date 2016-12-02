package ch.difty.sipamato.entity;

/**
 * Sipamato entity having a numeric id.
 *
 * @author u.joss
 *
 * @param <ID> type of the numeric id
 */
public abstract class IdSipamatoEntity<ID extends Number> extends SipamatoEntity {

    private static final long serialVersionUID = 1L;

    public static final String ID = "id";

    private ID id;

    public IdSipamatoEntity() {
    }

    public IdSipamatoEntity(final ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

}
