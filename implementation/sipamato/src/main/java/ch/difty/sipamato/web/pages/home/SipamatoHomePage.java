package ch.difty.sipamato.web.pages.home;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import ch.difty.sipamato.auth.Roles;
import ch.difty.sipamato.web.pages.BasePage;

@MountPath("/")
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
@WicketHomePage
public class SipamatoHomePage extends BasePage<Void> {

    private static final long serialVersionUID = 1L;

    public SipamatoHomePage(PageParameters parameters) {
        super(parameters);
    }

    protected void onInitialize() {
        super.onInitialize();
        add(new Label("message", makeMessage()));
        add(new Label("currentTime", getDateTimeService().getCurrentDateTime()));
    }

    private String makeMessage() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Hello ").append(getAuthentication().getName()).append(" - welcome to ").append(getProperties().getBrand()).append("!");
        return sb.toString();
    };
}
