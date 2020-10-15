package ch.difty.scipamato.publ.web

enum class PublicPageParameters(val parameterName: String) {
    SEARCH_ORDER_ID("searchOrderId"),
    SHOW_EXCLUDED("showExcluded"),
    SHOW_NAVBAR("showNavbar"),
    NUMBER("number"),
    ISSUE("issue"),
    PARENT_URL("parentUrl"),
    ;
}
