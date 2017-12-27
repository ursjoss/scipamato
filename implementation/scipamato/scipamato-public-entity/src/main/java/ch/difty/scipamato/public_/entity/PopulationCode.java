package ch.difty.scipamato.public_.entity;

/**
 * PopulationCode contains aggregated Codes of Code Class 3:
 * <ol>
 * <li>Children: Codes 3A + 3B</li>
 * <li>Adults: Codes 3C</li>
 * </ol>
 *
 * @author u.joss
 */
public enum PopulationCode {
    CHILDREN((short) 1),
    ADULTS((short) 2);

    private final short id;

    PopulationCode(final short id) {
        this.id = id;
    }

    public static PopulationCode of(final short id) {
        for (final PopulationCode r : values())
            if (id == r.getId())
                return r;
        throw new IllegalArgumentException("No matching type for id " + id);
    }

    public short getId() {
        return id;
    }

}
