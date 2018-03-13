package ch.difty.scipamato.publ.web;

public enum PublicPageParameters {

    SEARCH_ORDER_ID("searchOrderId"),
    SHOW_EXCLUDED("showExcluded"),
    SHOW_NAVBAR("showNavbar"),
    NUMBER("number");

    private final String name;

    PublicPageParameters(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
