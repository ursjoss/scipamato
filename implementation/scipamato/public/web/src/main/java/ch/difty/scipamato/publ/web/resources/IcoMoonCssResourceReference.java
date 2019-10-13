package ch.difty.scipamato.publ.web.resources;

import org.apache.wicket.request.resource.CssResourceReference;
import org.jetbrains.annotations.NotNull;

/**
 * This resource reference relies on commercial fonts that are not present in
 * the open-source distribution of SciPaMaTo. Do not call get() on this class
 * unless those fonts are actually present in the resource folder (sub-folder
 * fonts/IcoMoon).
 *
 * @author Urs Joss
 */
public class IcoMoonCssResourceReference extends CssResourceReference {

    private static final long serialVersionUID = 1L;

    private static final IcoMoonCssResourceReference INSTANCE = new IcoMoonCssResourceReference();

    private IcoMoonCssResourceReference() {
        super(IcoMoonCssResourceReference.class, "css/IcoMoon.css");
    }

    @NotNull
    public static IcoMoonCssResourceReference get() {
        return INSTANCE;
    }
}
