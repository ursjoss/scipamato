package ch.difty.sipamato.web.resources;

import org.apache.wicket.request.resource.CssResourceReference;

public class MainCssResourceReference extends CssResourceReference {

    private static final long serialVersionUID = 1L;

    private static final MainCssResourceReference INSTANCE = new MainCssResourceReference();

    private MainCssResourceReference() {
        super(MainCssResourceReference.class, "css/main.css");
    }

    public static MainCssResourceReference get() {
        return INSTANCE;
    }

}
