package ch.difty.sipamato;

import java.util.Date;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

@AuthorizeInstantiation("USER")
public class SipamatoHomePage extends BasePage {

    public SipamatoHomePage(PageParameters parameters) {
        super(parameters);
    }

    private static final long serialVersionUID = 1L;

    protected void onInitialize() {
        super.onInitialize();
        add(new Label("message", "Hello SiPaMaTo!"));
        add(new Label("currentTime", new Date().toString()));
    };
}
