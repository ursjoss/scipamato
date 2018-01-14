package ch.difty.scipamato.public_.entity;

/**
 * StudyDesignCode contains aggregated Codes of Code Class 5:
 * <ol>
 * <li>Experimental Studies: Codes 5A + 5B + 5C</li>
 * <li>Epidemiological Studies: Codes 5E + 5F + 5G + 5H + 5I</li>
 * <li>Overview Methodology: Codes 5U + 5M</li>
 * </ol>
 *
 * @author u.joss
 */
public enum StudyDesignCode {
    EXPERIMENTAL((short) 1),
    EPIODEMIOLOGICAL((short) 2),
    OVERVIEW_METHODOLOGY((short) 3);

    // cache values
    private static final StudyDesignCode[] CODES = values();

    private final short id;

    StudyDesignCode(final short id) {
        this.id = id;
    }

    public static StudyDesignCode of(final short id) {
        for (final StudyDesignCode r : CODES)
            if (id == r.getId())
                return r;
        throw new IllegalArgumentException("No matching type for id " + id);
    }

    public short getId() {
        return id;
    }

}
