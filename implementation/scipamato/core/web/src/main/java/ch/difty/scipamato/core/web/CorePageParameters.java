package ch.difty.scipamato.core.web;

import org.jetbrains.annotations.NotNull;

public enum CorePageParameters {

    SEARCH_ORDER_ID("searchOrderId"),
    SHOW_EXCLUDED("showExcluded"),
    USER_ID("userId"),
    MODE("mode"),
    TAB_INDEX("tabIndex");

    private final String name;

    CorePageParameters(@NotNull final String name) {
        this.name = name;
    }

    @NotNull
    public String getName() {
        return name;
    }
}
