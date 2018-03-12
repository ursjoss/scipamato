package ch.difty.scipamato.publ.web.newstudies;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import ch.difty.scipamato.publ.web.common.BasePageTest;

public class NewStudyListPageTest extends BasePageTest<NewStudyListPage> {

    // TODO mock service and assertComponents finer grained

    @Override
    protected NewStudyListPage makePage() {
        return new NewStudyListPage(new PageParameters());
    }

    @Override
    protected Class<NewStudyListPage> getPageClass() {
        return NewStudyListPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        super.assertSpecificComponents();

        getTester().assertLabel("h1Title", "New Studies");

        getTester().assertComponent("introParagraph", Label.class);
        getTester().assertComponent("dbLink", ExternalLink.class);

        getTester().assertComponent("topics", ListView.class);
    }

}
