package ch.difty.scipamato.publ.web.resources;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * JavaScriptResourceReference to pym.js allowing to use SciPaMaTo within a
 * responsive iframe.
 *
 * @author Urs Joss
 * @see <a href=
 *     "http://blog.apps.npr.org/pym.js/">http://blog.apps.npr.org/pym.js/</a>
 */
public class PymJavaScriptResourceReference extends JavaScriptResourceReference {

    private static final long serialVersionUID = 1L;

    private static final PymJavaScriptResourceReference INSTANCE = new PymJavaScriptResourceReference();

    private PymJavaScriptResourceReference() {
        super(PymJavaScriptResourceReference.class, "js/pym.v1.js");
    }

    public static PymJavaScriptResourceReference get() {
        return INSTANCE;
    }
}
