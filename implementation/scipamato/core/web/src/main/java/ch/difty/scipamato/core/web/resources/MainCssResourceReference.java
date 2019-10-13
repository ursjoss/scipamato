package ch.difty.scipamato.core.web.resources;

import org.apache.wicket.request.resource.CssResourceReference;
import org.jetbrains.annotations.NotNull;

public class MainCssResourceReference extends CssResourceReference {

    private static final long serialVersionUID = 1L;

    private static final MainCssResourceReference INSTANCE = new MainCssResourceReference();

    private MainCssResourceReference() {
        super(MainCssResourceReference.class, "css/main.css");
    }

    @NotNull
    public static MainCssResourceReference get() {
        return INSTANCE;
    }
}
