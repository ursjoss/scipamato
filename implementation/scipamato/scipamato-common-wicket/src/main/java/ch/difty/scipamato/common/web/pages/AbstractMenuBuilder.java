package ch.difty.scipamato.common.web.pages;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapExternalLink.Target;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarExternalLink;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Page;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.config.ApplicationProperties;

public abstract class AbstractMenuBuilder implements MenuBuilder {

    private final ApplicationProperties applicationProperties;

    /**
     * @param applicationProperties
     *     must not be null. Injected through spring constructor injection.
     */
    protected AbstractMenuBuilder(final ApplicationProperties applicationProperties) {
        this.applicationProperties = AssertAs.notNull(applicationProperties, "applicationProperties");
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

}
