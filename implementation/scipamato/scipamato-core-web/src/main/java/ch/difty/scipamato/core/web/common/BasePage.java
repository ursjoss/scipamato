package ch.difty.scipamato.core.web.common;

import java.util.Arrays;
import java.util.stream.Collectors;

import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import ch.difty.scipamato.common.navigator.ItemNavigator;
import ch.difty.scipamato.common.web.AbstractPage;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;
import ch.difty.scipamato.common.web.pages.MenuBuilder;
import ch.difty.scipamato.core.config.ApplicationCoreProperties;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.web.resources.MainCssResourceReference;

public abstract class BasePage<T> extends AbstractPage<T> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private ApplicationCoreProperties applicationProperties;
    @SpringBean
    private MenuBuilder               menuBuilder;
    @SpringBean
    private ScipamatoWebSessionFacade sessionFacade;

    protected BasePage(final PageParameters parameters) {
        super(parameters);
    }

    protected BasePage(final IModel<T> model) {
        super(model);
    }

    protected ApplicationCoreProperties getProperties() {
        return applicationProperties;
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(MainCssResourceReference.get()));
    }

    @Override
    protected void addLinksTo(final Navbar nb) {
        super.addLinksTo(nb);
        menuBuilder.addMenuLinksTo(nb, this);
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder
            .getContext()
            .getAuthentication();
    }

    protected User getActiveUser() {
        return (User) getAuthentication().getPrincipal();
    }

    protected ItemNavigator<Long> getPaperIdManager() {
        return sessionFacade.getPaperIdManager();
    }

    protected String getLanguageCode() {
        return sessionFacade.getLanguageCode();
    }

    protected boolean hasOneOfRoles(String... roles) {
        return sessionFacade.hasAtLeastOneRoleOutOf(Arrays
            .stream(roles)
            .sorted()
            .collect(Collectors.toList())
            .toArray(new String[0]));
    }

}
