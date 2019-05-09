package ch.difty.scipamato.publ.web.resources;

import org.apache.wicket.request.resource.CssResourceReference;

/**
 * This resource reference relies on commercial fonts that are not present in
 * the open-source distribution of SciPaMaTo. Do not call get() on this class
 * unless those fonts are actually present in the resource folder (sub-folder
 * fonts/Simplon).
 *
 * @author Urs Joss
 */
public class SimplonCssResourceReference extends CssResourceReference {

    private static final long serialVersionUID = 1L;

    private static final SimplonCssResourceReference INSTANCE = new SimplonCssResourceReference();

    private SimplonCssResourceReference() {
        super(SimplonCssResourceReference.class, "css/Simplon.css");
    }

    public static SimplonCssResourceReference get() {
        return INSTANCE;
    }

}
