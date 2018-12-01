package ch.difty.scipamato.common.entity.newsletter;

/**
 * Newsletter specific Publication Status
 *
 * @author Urs Joss
 */
public enum PublicationStatus {
    WIP(0, "in progress"),
    PUBLISHED(1, "published"),
    CANCELLED(-1, "cancelled");

    // cache the array
    private static final PublicationStatus[] NEWSLETTER_STATI = values();

    private final int    id;
    private final String name;

    PublicationStatus(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static PublicationStatus byId(final int id) {
        for (final PublicationStatus t : NEWSLETTER_STATI)
            if (t.getId() == id)
                return t;
        throw new IllegalArgumentException("id " + id + " is not supported");
    }

    /**
     * Newsletters of only certain status are eligible for deletes or modifications.
     *
     * @return if the newsletter is in status WIP (work in progress)
     */
    public boolean isInProgress() {
        return WIP == this;
    }

}
