package ch.difty.scipamato.common.web.pages;

import java.util.ArrayList;
import java.util.List;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapExternalLink.Target;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.web.AbstractPage;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;
import ch.difty.scipamato.common.web.component.SerializableConsumer;

public abstract class AbstractMenuBuilder implements MenuBuilder {

    private final ApplicationProperties     applicationProperties;
    private final ScipamatoWebSessionFacade webSessionFacade;

    /**
     * @param applicationProperties
     *     must not be null. Injected through spring constructor injection. Must not be null.
     * @param webSessionFacade
     *     the webSessionFacade providing mockable access to the web session. Must not be null.
     */
    protected AbstractMenuBuilder(final ApplicationProperties applicationProperties,
        final ScipamatoWebSessionFacade webSessionFacade) {
        this.applicationProperties = AssertAs.INSTANCE.notNull(applicationProperties, "applicationProperties");
        this.webSessionFacade = AssertAs.INSTANCE.notNull(webSessionFacade, "webSessionFacade");
    }

    public ApplicationProperties getApplicationProperties() {
        return applicationProperties;
    }

    protected <P extends Page> void addPageLink(final Navbar navbar, final Page menuPage,
        final Class<P> referringPageClass, final String labelResource, final IconType iconType,
        final Navbar.ComponentPosition position) {
        final String label = new StringResourceModel(labelResource, menuPage, null).getString();
        final NavbarButton<Void> button = new NavbarButton<Void>(referringPageClass, Model.of(label)).setIconType(
            iconType);
        navbar.addComponents(NavbarComponents.transform(position, button));
    }

    protected void addExternalLink(final Navbar navbar, final String url, final String label, final IconType iconType,
        final Navbar.ComponentPosition position) {
        final NavbarExternalLink link = new NavbarExternalLink(Model.of(url));
        link.setLabel(Model.of(label));
        link.setTarget(Target.blank);
        if (iconType != null)
            link.setIconType(iconType);
        navbar.addComponents(NavbarComponents.transform(position, link));
    }

    protected void newMenu(final Navbar navbar, final Page page, final String labelResource,
        final GlyphIconType iconType, final SerializableConsumer<List<AbstractLink>> consumer) {
        final String label = new StringResourceModel("menu." + labelResource, page, null).getString();
        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT,
            new NavbarDropDownButton(Model.of(label), Model.of((IconType) iconType)) {
                private static final long serialVersionUID = 1L;

                @Override
                protected List<AbstractLink> newSubMenuButtons(String buttonMarkupId) {
                    final List<AbstractLink> links = new ArrayList<>();
                    consumer.accept(links);
                    return links;
                }
            }));
    }

    protected <P extends AbstractPage<?>> void addEntryToMenu(final String label, Component component,
        final Class<P> pageClass, final IconType iconType, final List<AbstractLink> links,
        final PageParameters pageParameters) {
        StringResourceModel labelModel = new StringResourceModel(label, component, null);
        MenuBookmarkablePageLink<Void> link = new MenuBookmarkablePageLink<>(pageClass, pageParameters, labelModel);
        if (iconType != null)
            link.setIconType(iconType);
        links.add(link);
    }

    protected <P extends AbstractPage<?>> void addEntryToMenu(final String label, Component component,
        final Class<P> pageClass, final IconType iconType, final List<AbstractLink> links) {
        addEntryToMenu(label, component, pageClass, iconType, links, new PageParameters());
    }

    protected String getVersionAnker() {
        final String buildVersion = getApplicationProperties().getBuildVersion();
        if (StringUtils.isEmpty(buildVersion))
            return "";
        else
            return "#" + (buildVersion.endsWith("SNAPSHOT") ? "unreleased" : "v" + buildVersion);
    }

    protected String getVersionLink() {
        return "version " + getApplicationProperties().getBuildVersion();
    }

    protected boolean hasOneOfRoles(String... roles) {
        return webSessionFacade.hasAtLeastOneRoleOutOf(roles);
    }
}
