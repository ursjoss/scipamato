package ch.difty.sipamato.web;

import java.util.Date;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

@AuthorizeInstantiation("USER")
@WicketHomePage
public class SipamatoHomePage extends BasePage {

    private static final long serialVersionUID = 1L;

    public SipamatoHomePage(PageParameters parameters) {
        super(parameters);
    }

    protected void onInitialize() {
        super.onInitialize();
        add(new Label("message", "Hello SiPaMaTo!"));
        add(new Label("currentTime", new Date().toString()));
    };
}
