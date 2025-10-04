package ch.difty.scipamato.core.web;

import java.util.List;

import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome7IconType;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;
import ch.difty.scipamato.common.web.pages.AbstractMenuBuilder;
import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.web.authentication.LogoutPage;
import ch.difty.scipamato.core.web.code.CodeListPage;
import ch.difty.scipamato.core.web.codeclass.CodeClassListPage;
import ch.difty.scipamato.core.web.keyword.KeywordListPage;
import ch.difty.scipamato.core.web.newsletter.list.NewsletterListPage;
import ch.difty.scipamato.core.web.newsletter.topic.NewsletterTopicListPage;
import ch.difty.scipamato.core.web.paper.list.PaperListPage;
import ch.difty.scipamato.core.web.paper.search.PaperSearchPage;
import ch.difty.scipamato.core.web.sync.RefDataSyncPage;
import ch.difty.scipamato.core.web.user.UserEditPage;
import ch.difty.scipamato.core.web.user.UserListPage;

/**
 * Adds the SciPaMaTo-Core menus to the navbar for the base page.
 *
 * @author u.joss
 */
@Component
public class CoreMenuBuilder extends AbstractMenuBuilder {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    public CoreMenuBuilder(@NotNull final ApplicationProperties applicationProperties, @NotNull final ScipamatoWebSessionFacade webSessionFacade) {
        super(applicationProperties, webSessionFacade);
    }

    @Override
    public void addMenuLinksTo(@NotNull final Navbar navbar, @NotNull final Page page) {
        newMenu(navbar, page, "papers", FontAwesome7IconType.paperclip_s, l -> addPaperMenuEntries(l, page));
        if (hasOneOfRoles(Roles.USER, Roles.ADMIN)) {
            newMenu(navbar, page, "newsletters", FontAwesome7IconType.book_s, l -> addNewsletterMenuEntries(l, page));
            newMenu(navbar, page, "refData", FontAwesome7IconType.folder_open_s, l -> addRefDataMenuEntries(l, page));
            newMenu(navbar, page, "preferences", FontAwesome7IconType.user_s, l -> addPreferencesMenuEntries(l, page));
        }

        addExternalLink(navbar, new StringResourceModel("menu.help.url", page, null).getString(),
            new StringResourceModel("menu.help", page, null).getString(), FontAwesome7IconType.circle_question_s, Navbar.ComponentPosition.RIGHT);
        addExternalLink(navbar, new StringResourceModel("menu.changelog.url", page, null)
            .setParameters(getVersionAnker())
            .getString(), getVersionLink(), FontAwesome7IconType.briefcase_s, Navbar.ComponentPosition.RIGHT);
        addPageLink(navbar, page, LogoutPage.class, "menu.logout", FontAwesome7IconType.right_from_bracket_s, Navbar.ComponentPosition.RIGHT);
    }

    private void addPaperMenuEntries(@NotNull final List<AbstractLink> links, @NotNull final Page page) {
        final String labelParent = "menu.papers.";
        addEntryToMenu(labelParent + "paper", page, PaperListPage.class, FontAwesome7IconType.list_s, links);
        addEntryToMenu(labelParent + "search", page, PaperSearchPage.class, FontAwesome7IconType.magnifying_glass_s, links);
    }

    private void addNewsletterMenuEntries(@NotNull final List<AbstractLink> links, @NotNull final Page page) {
        final String labelParent = "menu.newsletters.";
        if (hasOneOfRoles(Roles.USER, Roles.ADMIN)) {
            addEntryToMenu(labelParent + "newsletter", page, NewsletterListPage.class, FontAwesome7IconType.newspaper_s, links);
            addEntryToMenu(labelParent + "newslettertopic", page, NewsletterTopicListPage.class, FontAwesome7IconType.bookmark_s, links);
        }
    }

    private void addRefDataMenuEntries(@NotNull final List<AbstractLink> links, @NotNull final Page page) {
        final String labelParent = "menu.refData.";
        if (hasOneOfRoles(Roles.USER, Roles.ADMIN)) {
            addEntryToMenu(labelParent + "keyword", page, KeywordListPage.class, FontAwesome7IconType.briefcase_s, links);
            addEntryToMenu(labelParent + "code", page, CodeListPage.class, FontAwesome7IconType.barcode_s, links);
            addEntryToMenu(labelParent + "codeClass", page, CodeClassListPage.class, FontAwesome7IconType.qrcode_s, links);
            addEntryToMenu(labelParent + "sync", page, RefDataSyncPage.class, FontAwesome7IconType.rotate_s, links);
        }
    }

    private void addPreferencesMenuEntries(@NotNull final List<AbstractLink> links, @NotNull final Page page) {
        final String labelParent = "menu.preferences.";
        if (hasOneOfRoles(Roles.ADMIN)) {
            addEntryToMenu(labelParent + "users", page, UserListPage.class, FontAwesome7IconType.users_s, links);
        }
        if (hasOneOfRoles(Roles.USER, Roles.ADMIN)) {
            final PageParameters pp = new PageParameters();
            pp.add("mode", UserEditPage.Mode.EDIT);
            addEntryToMenu(labelParent + "profile", page, UserEditPage.class, FontAwesome7IconType.user_pen_s, links, pp);

            final PageParameters pp2 = new PageParameters();
            pp2.add("mode", UserEditPage.Mode.CHANGE_PASSWORD);
            addEntryToMenu(labelParent + "password", page, UserEditPage.class, FontAwesome7IconType.key_s, links, pp2);
        }
    }
}
