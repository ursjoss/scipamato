package ch.difty.scipamato.publ.web.pym;

import org.jetbrains.annotations.NotNull;

/**
 * Enum defining the JavaScript snippets used in the context of
 * providing responsive iframes with pym.js.
 */
public enum PymScripts {
    INSTANTIATE("pymChild", "var pymChild = new pym.Child({ id: 'scipamato-public' });"),
    RESIZE("pymResize", "pymChild.sendHeight();");

    public final String id;
    public final String script;

    PymScripts(@NotNull final String id, @NotNull final String script) {
        this.id = id;
        this.script = script;
    }
}
