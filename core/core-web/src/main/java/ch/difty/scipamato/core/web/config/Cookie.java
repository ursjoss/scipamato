package ch.difty.scipamato.core.web.config;

import org.jetbrains.annotations.NotNull;

/**
 * Enum containing all Cookies used within SciPaMaTo. Names must be unique.
 *
 * @author u.joss
 */
@SuppressWarnings("SameParameterValue")
public enum Cookie {
    PAPER_LIST_PAGE_MODAL_WINDOW("xmlPasteModal-1");

    private static final String TAG = "SciPaMaTo-";

    @NotNull
    private final String name;

    Cookie(@NotNull final String name) {
        this.name = TAG + name;
    }

    @NotNull
    public String getName() {
        return name;
    }
}
