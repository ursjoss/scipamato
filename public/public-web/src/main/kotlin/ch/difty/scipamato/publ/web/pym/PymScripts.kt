package ch.difty.scipamato.publ.web.pym

/**
 * Enum defining the JavaScript snippets used in the context of providing responsive iframes with pym.js.
 */
enum class PymScripts(val id: String, val script: String) {
    INSTANTIATE("pymChild", "var pymChild = new pym.Child({ id: 'scipamato-public' });"),
    RESIZE("pymResize", "pymChild.sendHeight();");
}
