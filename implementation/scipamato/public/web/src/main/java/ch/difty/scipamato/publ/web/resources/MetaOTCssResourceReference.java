package ch.difty.scipamato.publ.web.resources;

import org.apache.wicket.request.resource.CssResourceReference;

/**
 * This resource reference relies on commercial fonts that are not present in
 * the open-source distribution of SciPaMaTo. Do not call get() on this class
 * unless those fonts are actually present in the resource folder (sub-folder
 * fonts/MetaOT).
 *
 * @author Urs Joss
 */
public class MetaOTCssResourceReference extends CssResourceReference {

    private static final long serialVersionUID = 1L;

    private static final MetaOTCssResourceReference INSTANCE = new MetaOTCssResourceReference();

    private MetaOTCssResourceReference() {
        super(MetaOTCssResourceReference.class, "css/MetaOT.css");
    }

    public static MetaOTCssResourceReference get() {
        return INSTANCE;
    }

}
