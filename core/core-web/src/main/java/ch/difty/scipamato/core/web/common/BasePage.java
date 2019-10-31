package ch.difty.scipamato.core.web.common;

import java.util.Arrays;

import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

    protected BasePage(@Nullable final PageParameters parameters) {
        super(parameters);
    }

    protected BasePage(@Nullable final IModel<T> model) {
        super(model);
    }

    @NotNull
    protected ApplicationCoreProperties getProperties() {
        return applicationProperties;
    }

    @Override
    public void renderHead(@NotNull final IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(MainCssResourceReference.get()));
    }

    @Override
    protected void addLinksTo(@NotNull final Navbar nb) {
        super.addLinksTo(nb);
        menuBuilder.addMenuLinksTo(nb, this);
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder
            .getContext()
            .getAuthentication();
    }

    @NotNull
    protected User getActiveUser() {
        return (User) getAuthentication().getPrincipal();
    }

    @NotNull
    protected ItemNavigator<Long> getPaperIdManager() {
        return sessionFacade.getPaperIdManager();
    }

    @NotNull
    protected String getLanguageCode() {
        return sessionFacade.getLanguageCode();
    }

    protected boolean hasOneOfRoles(@NotNull String... roles) {
        return sessionFacade.hasAtLeastOneRoleOutOf(Arrays
            .stream(roles)
            .sorted()
            .toArray(String[]::new));
    }
}
