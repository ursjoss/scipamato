package ch.difty.scipamato.web.test;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import ch.difty.scipamato.web.AbstractPage;
import ch.difty.scipamato.web.TestRecord;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;

public class TestAbstractPage extends AbstractPage<TestRecord> {
    private static final long serialVersionUID = 1L;

    public TestAbstractPage(final IModel<TestRecord> model) {
        super(model);
    }

    @Override
    protected Navbar newNavbar(String markupId) {
        Navbar nb = new Navbar(markupId);
        nb.setPosition(Navbar.Position.TOP);
        nb.setBrandName(new ResourceModel("brandname", "foobar"));
        nb.setInverted(true);

        addPageLink(nb, TestHomePage.class, "menu.home", GlyphIconType.home);
        addExternalLink(nb, "https://github.com/ursjoss/scipamato/wiki", "menu.help", GlyphIconType.questionsign);
        return nb;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        queue(new Form<Void>("form"));

        queueFieldAndLabel(new TextField<String>("foo"));

        queueResponsePageButton("respPageButton", () -> new TestHomePage(new PageParameters()));
    }

}
