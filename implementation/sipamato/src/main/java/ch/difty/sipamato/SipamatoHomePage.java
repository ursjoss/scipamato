package ch.difty.sipamato;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

public class SipamatoHomePage extends WebPage {

    private static final long serialVersionUID = 1L;

    protected void onInitialize() {
        super.onInitialize();
        add(new Label("message", "Hello SiPaMaTo!"));
    };
}
