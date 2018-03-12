package ch.difty.scipamato.publ.web.newstudies;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.publ.entity.NewStudy;
import ch.difty.scipamato.publ.entity.NewStudyTopic;
import ch.difty.scipamato.publ.persistence.api.NewStudyTopicService;
import ch.difty.scipamato.publ.web.common.BasePageTest;

public class NewStudyListPageTest extends BasePageTest<NewStudyListPage> {

    @MockBean
    private NewStudyTopicService serviceMock;

    private final List<NewStudyTopic> topics = new ArrayList<>();

    @Override
    protected void setUpHook() {
        super.setUpHook();

        int topicIndex = 0;
        int studyIndex = 0;
        List<NewStudy> newStudies1 = new ArrayList<>();
        newStudies1.add(new NewStudy(studyIndex++, 8924, "(Foo et al.; 2017)", "hl1", "descr1"));
        newStudies1.add(new NewStudy(studyIndex++, 8993, "(Bar et al.; 2017)", "hl2", "descr2"));
        topics.add(new NewStudyTopic(topicIndex++, "Topic1", newStudies1));
        List<NewStudy> newStudies2 = new ArrayList<>();
        studyIndex = 0;
        newStudies2.add(new NewStudy(studyIndex++, 8973, "(Baz et al.; 2017)", "hl3", "descr3"));
        topics.add(new NewStudyTopic(topicIndex++, "Topic2", newStudies2));

        when(serviceMock.findMostRecentNewStudyTopics()).thenReturn(topics);
    }

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
        getTester().debugComponentTrees();

        getTester().assertLabel("topics:0:topicTitle", "Topic1");
        getTester().assertLabel("topics:0:topicStudies:0:headline", "hl1");
        getTester().assertLabel("topics:0:topicStudies:0:description", "descr1");
        getTester().assertComponent("topics:0:topicStudies:0:reference", BookmarkablePageLink.class);
        getTester().assertLabel("topics:0:topicStudies:0:reference:referenceLabel", "(Foo et al.; 2017)");
    }

}
