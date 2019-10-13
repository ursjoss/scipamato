package ch.difty.scipamato.publ.web.resources;

import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import org.jetbrains.annotations.NotNull;

public class IcoMoonIconType extends IconType {
    private static final long serialVersionUID = 1L;

    public static final IcoMoonIconType arrow_right = new IcoMoonIconType("arrow-right");
    public static final IcoMoonIconType link        = new IcoMoonIconType("link");

    private IcoMoonIconType(@NotNull String cssClassName) {
        super(cssClassName);
    }

    @NotNull
    public String cssClassName() {
        return "icon-" + this.getCssClassName();
    }
}
