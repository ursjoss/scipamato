package ch.difty.scipamato.common.web;

import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import org.apache.wicket.Page;
import org.apache.wicket.model.StringResourceModel;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.web.pages.AbstractMenuBuilder;

@Primary
@Component
public class TestMenuBuilder extends AbstractMenuBuilder {

    protected TestMenuBuilder(final ApplicationProperties applicationProperties,
        final ScipamatoWebSessionFacade webSessionFacade) {
        super(applicationProperties, webSessionFacade);
    }

    @Override
    public void addMenuLinksTo(final Navbar navbar, final Page page) {
        addPageLink(navbar, page, TestHomePage.class, "menu.home", GlyphIconType.home, Navbar.ComponentPosition.LEFT);
        addExternalLink(navbar, "https://github.com/ursjoss/scipamato/wiki",
            new StringResourceModel("menu.help", page, null).getString(), GlyphIconType.questionsign,
            Navbar.ComponentPosition.RIGHT);
    }

}
