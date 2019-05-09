package ch.difty.scipamato.publ.web.pym;

/**
 * Enum defining the JavaScript snippets used in the context of
 * providing responsive iframes with pym.js.
 */
public enum PymScripts {
    INSTANTIATE("pymChild", "var pymChild = new pym.Child({ id: 'scipamato-public' });"),
    RESIZE("pymResize", "pymChild.sendHeight();");

    public final String id;
    public final String script;

    PymScripts(final String id, final String script) {
        this.id = id;
        this.script = script;
    }

}
