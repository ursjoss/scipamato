package ch.difty.scipamato.core.web;

import java.util.List;

import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;
import ch.difty.scipamato.common.web.pages.AbstractMenuBuilder;
import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.web.authentication.LogoutPage;
import ch.difty.scipamato.core.web.newsletter.list.NewsletterListPage;
import ch.difty.scipamato.core.web.newsletter.topic.NewsletterTopicListPage;
import ch.difty.scipamato.core.web.paper.list.PaperListPage;
import ch.difty.scipamato.core.web.paper.search.PaperSearchPage;
import ch.difty.scipamato.core.web.sync.RefDataSyncPage;
import ch.difty.scipamato.core.web.user.UserEditPage;

/**
 * Adds the SciPaMaTo-Core menus to the navbar for the base page.
 *
 * @author u.joss
 */
@Component
public class CoreMenuBuilder extends AbstractMenuBuilder {

    private static final long serialVersionUID = 1L;

    public CoreMenuBuilder(final ApplicationProperties applicationProperties,
        final ScipamatoWebSessionFacade webSessionFacade) {
        super(applicationProperties, webSessionFacade);
    }

    @Override
    public void addMenuLinksTo(final Navbar navbar, final Page page) {
        AssertAs.notNull(navbar, "navbar");
        AssertAs.notNull(page, "page");

        newMenu(navbar, page, "papers", GlyphIconType.paperclip, l -> addPaperMenuEntries(l, page));
        newMenu(navbar, page, "newsletters", GlyphIconType.book, l -> addNewsletterMenuEntries(l, page));
        newMenu(navbar, page, "sync", GlyphIconType.export, l -> addSyncMenuEntries(l, page));
        newMenu(navbar, page, "preferences", GlyphIconType.user, l -> addPreferencesMenuEntries(l, page));

        addExternalLink(navbar, new StringResourceModel("menu.help.url", page, null).getString(),
            new StringResourceModel("menu.help", page, null).getString(), GlyphIconType.questionsign,
            Navbar.ComponentPosition.RIGHT);
        addExternalLink(navbar, new StringResourceModel("menu.changelog.url", page, null)
            .setParameters(getVersionAnker())
            .getString(), getVersionLink(), GlyphIconType.briefcase, Navbar.ComponentPosition.RIGHT);
        addPageLink(navbar, page, LogoutPage.class, "menu.logout", GlyphIconType.edit, Navbar.ComponentPosition.RIGHT);
    }

    private void addPaperMenuEntries(final List<AbstractLink> links, Page page) {
        final String labelParent = "menu.papers.";
        addEntryToMenu(labelParent + "paper", page, PaperListPage.class, GlyphIconType.list, links);
        addEntryToMenu(labelParent + "search", page, PaperSearchPage.class, GlyphIconType.search, links);
    }

    private void addNewsletterMenuEntries(final List<AbstractLink> links, Page page) {
        final String labelParent = "menu.newsletters.";
        if (hasOneOfRoles(Roles.USER, Roles.ADMIN)) {
            addEntryToMenu(labelParent + "newsletter", page, NewsletterListPage.class, GlyphIconType.book, links);
            addEntryToMenu(labelParent + "newslettertopic", page, NewsletterTopicListPage.class, GlyphIconType.bookmark,
                links);
        }
    }

    private void addSyncMenuEntries(final List<AbstractLink> links, Page page) {
        final String labelParent = "menu.sync.";
        if (hasOneOfRoles(Roles.USER, Roles.ADMIN)) {
            addEntryToMenu(labelParent + "sync", page, RefDataSyncPage.class, GlyphIconType.export, links);
        }
    }

    private void addPreferencesMenuEntries(final List<AbstractLink> links, Page page) {
        final String labelParent = "menu.preferences.";
        if (hasOneOfRoles(Roles.USER, Roles.ADMIN)) {
            final PageParameters pp = new PageParameters();
            pp.add("mode", UserEditPage.Mode.EDIT);
            addEntryToMenu(labelParent + "profile", page, UserEditPage.class, GlyphIconType.user, links, pp);

            final PageParameters pp2 = new PageParameters();
            pp2.add("mode", UserEditPage.Mode.CHANGE_PASSWORD);
            addEntryToMenu(labelParent + "password", page, UserEditPage.class, GlyphIconType.user, links, pp2);

        }
    }

}
