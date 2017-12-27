package ch.difty.scipamato.common.web.test;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import ch.difty.scipamato.common.config.core.ApplicationProperties;
import ch.difty.scipamato.common.web.AbstractPage;
import ch.difty.scipamato.common.web.TestRecord;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;

public class TestAbstractPage extends AbstractPage<TestRecord> {
    private static final long serialVersionUID = 1L;

    public TestAbstractPage(final IModel<TestRecord> model) {
        super(model);
    }

    @Override
    protected void addLinksTo(Navbar nb) {
        addPageLink(nb, TestHomePage.class, "menu.home", GlyphIconType.home, Navbar.ComponentPosition.LEFT);
        addExternalLink(nb, "https://github.com/ursjoss/scipamato/wiki",
            new StringResourceModel("menu.help", this, null).getString(), GlyphIconType.questionsign,
            Navbar.ComponentPosition.RIGHT);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        queue(new Form<Void>("form"));

        queueFieldAndLabel(new TextField<String>("foo"));

        queueResponsePageButton("respPageButton", () -> new TestHomePage(new PageParameters()));
    }

    @Override
    protected ApplicationProperties getProperties() {
        return new TestApplicationProperties();
    }

}
