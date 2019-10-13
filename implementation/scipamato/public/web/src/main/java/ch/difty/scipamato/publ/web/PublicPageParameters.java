package ch.difty.scipamato.publ.web;

import org.jetbrains.annotations.NotNull;

public enum PublicPageParameters {

    SEARCH_ORDER_ID("searchOrderId"),
    SHOW_EXCLUDED("showExcluded"),
    SHOW_NAVBAR("showNavbar"),
    NUMBER("number"),
    ISSUE("issue"),
    PARENT_URL("parentUrl");

    private final String name;

    PublicPageParameters(@NotNull final String name) {
        this.name = name;
    }

    @NotNull
    public String getName() {
        return name;
    }
}
