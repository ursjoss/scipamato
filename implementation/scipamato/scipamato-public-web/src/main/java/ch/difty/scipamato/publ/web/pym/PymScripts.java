package ch.difty.scipamato.publ.web.pym;

/**
 * Enum defining the JavaScript snippets used in the context of
 * providing responsive iframes with pym.js.
 */
public enum PymScripts {
    INSTANTIATE("pymChild", "var pymChild = new pym.Child()"),
    RESIZE("pymResize", "pymChild.sendHeight(); console.log('sendHeight called');");

    private static final PymScripts[] SCRIPTS = values();

    public final String id;
    public final String script;

    PymScripts(final String id, final String script) {
        this.id = id;
        this.script = script;
    }
}
