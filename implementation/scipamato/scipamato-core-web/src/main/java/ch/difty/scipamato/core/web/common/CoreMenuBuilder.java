package ch.difty.scipamato.core.web.common;

import org.apache.wicket.Page;
import org.apache.wicket.model.StringResourceModel;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.web.pages.AbstractMenuBuilder;
import ch.difty.scipamato.core.web.authentication.LogoutPage;
import ch.difty.scipamato.core.web.paper.list.PaperListPage;
import ch.difty.scipamato.core.web.paper.search.PaperSearchPage;
import ch.difty.scipamato.core.web.sync.RefDataSyncPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;

/**
 * Adds the SciPaMaTo-Core menus to the navbar for the base page.
 *
 * @author u.joss
 */
@Component
public class CoreMenuBuilder extends AbstractMenuBuilder {

    public CoreMenuBuilder(final ApplicationProperties applicationProperties) {
        super(applicationProperties);
    }

    @Override
    public void addMenuLinksTo(final Navbar navbar, final Page page) {
        AssertAs.notNull(navbar, "navbar");
        AssertAs.notNull(page, "page");

        addPageLink(navbar, page, PaperListPage.class, "menu.papers", GlyphIconType.list,
            Navbar.ComponentPosition.LEFT);
        addPageLink(navbar, page, PaperSearchPage.class, "menu.search", GlyphIconType.search,
            Navbar.ComponentPosition.LEFT);
        addPageLink(navbar, page, RefDataSyncPage.class, "menu.sync", GlyphIconType.export,
            Navbar.ComponentPosition.LEFT);

        addExternalLink(navbar, new StringResourceModel("menu.help.url", page, null).getString(),
            new StringResourceModel("menu.help", page, null).getString(), GlyphIconType.questionsign,
            Navbar.ComponentPosition.RIGHT);
        addExternalLink(navbar,
            new StringResourceModel("menu.changelog.url", page, null).setParameters(getVersionAnker())
                .getString(),
            getVersionLink(), GlyphIconType.briefcase, Navbar.ComponentPosition.RIGHT);
        addPageLink(navbar, page, LogoutPage.class, "menu.logout", GlyphIconType.edit, Navbar.ComponentPosition.RIGHT);
    }

}
