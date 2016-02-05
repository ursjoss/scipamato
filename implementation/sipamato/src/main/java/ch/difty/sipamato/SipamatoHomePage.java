package ch.difty.sipamato;

import java.util.Date;

import org.apache.wicket.markup.html.basic.Label;

public class SipamatoHomePage extends BasePage {

    private static final long serialVersionUID = 1L;

    protected void onInitialize() {
        super.onInitialize();
        add(new Label("message", "Hello SiPaMaTo!"));
        add(new Label("currentTime", new Date().toString()));
    };
}
