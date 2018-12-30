package ch.difty.scipamato.publ.web.newstudies;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapExternalLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.publ.config.ApplicationPublicProperties;
import ch.difty.scipamato.publ.config.ScipamatoPublicProperties;
import ch.difty.scipamato.publ.entity.NewStudy;
import ch.difty.scipamato.publ.entity.NewStudyPageLink;
import ch.difty.scipamato.publ.entity.NewStudyTopic;
import ch.difty.scipamato.publ.entity.Newsletter;
import ch.difty.scipamato.publ.persistence.api.NewStudyTopicService;
import ch.difty.scipamato.publ.web.CommercialFontResourceProvider;
import ch.difty.scipamato.publ.web.PublicPageParameters;
import ch.difty.scipamato.publ.web.common.BasePageTest;
import ch.difty.scipamato.publ.web.paper.browse.PublicPaperDetailPage;
import ch.difty.scipamato.publ.web.resources.IcoMoonIconType;

public class NewStudyListPageTest extends BasePageTest<NewStudyListPage> {

    @MockBean
    private NewStudyTopicService serviceMock;

    @MockBean(name = "simplonFontResourceProvider")
    private CommercialFontResourceProvider simplonFontResourceProvider;

    @MockBean(name = "icoMoonFontResourceProvider")
    private CommercialFontResourceProvider icoMoonFontResourceProvider;

    private final List<NewStudyTopic>    topics   = new ArrayList<>();
    private final List<NewStudyPageLink> links    = new ArrayList<>();
    private final List<Newsletter>       archived = new ArrayList<>();

    @Override
    protected void setUpHook() {
        super.setUpHook();

        int topicIndex = 0;
        int studyIndex = 0;
        List<NewStudy> newStudies1 = new ArrayList<>();
        newStudies1.add(new NewStudy(studyIndex++, 8924, 2017, "Foo et al.", "hl1", "descr1"));
        newStudies1.add(new NewStudy(studyIndex, 8993, 2017, "Bar et al.", "hl2", "descr2"));
        topics.add(new NewStudyTopic(topicIndex++, "Topic1", newStudies1));
        List<NewStudy> newStudies2 = new ArrayList<>();
        studyIndex = 0;
        newStudies2.add(new NewStudy(studyIndex, 8973, 2017, "Baz et al.", "hl3", "descr3"));
        topics.add(new NewStudyTopic(topicIndex, "Topic2", newStudies2));
        when(serviceMock.findMostRecentNewStudyTopics(Mockito.anyString())).thenReturn(topics);

        links.add(new NewStudyPageLink("en", 1, "linkTitle1", "linkUrl1"));
        links.add(new NewStudyPageLink("en", 2, "linkTitle2", "linkUrl2"));
        when(serviceMock.findNewStudyPageLinks(Mockito.anyString())).thenReturn(links);

        archived.add(new Newsletter(10, "2018/02", LocalDate.of(2018, 2, 10)));
        archived.add(new Newsletter(9, "2017/12", LocalDate.of(2017, 12, 12)));
        when(serviceMock.findArchivedNewsletters(Mockito.anyInt(), Mockito.anyString())).thenReturn(archived);
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

        assertStudySection();
        assertLinkSection();
        assertArchiveSection();
    }

    private void assertStudySection() {
        getTester().assertLabel("h1Title", "New Studies");

        getTester().assertComponent("introParagraph", Label.class);
        getTester().assertComponent("dbLink", ExternalLink.class);

        getTester().assertComponent("topics", ListView.class);

        String topic = "topics:0:";
        getTester().assertLabel(topic + "topicTitle", "Topic1");
        assertNewStudy(topic, 0, "hl1", "descr1", "(Foo et al.; 2017)");
        assertNewStudy(topic, 1, "hl2", "descr2", "(Bar et al.; 2017)");

        topic = "topics:1:";
        getTester().assertLabel(topic + "topicTitle", "Topic2");
        assertNewStudy(topic, 0, "hl3", "descr3", "(Baz et al.; 2017)");
    }

    private void assertLinkSection() {
        getTester().assertComponent("links", ListView.class);
        int index = 0;
        assertLink(index++, "linkTitle1", "linkUrl1");
        assertLink(index, "linkTitle2", "linkUrl2");
    }

    private void assertLink(final int index, final String title, final String url) {
        String path = "links:" + index + ":link";
        getTester().assertComponent(path, BootstrapExternalLink.class);
        getTester().assertModelValue(path, url);
        getTester().assertLabel(path + ":label", title);
    }

    private void assertArchiveSection() {
        getTester().assertLabel("h2ArchiveTitle", "Archive");
        getTester().assertComponent("archive", ListView.class);

        getTester().assertLabel("archive:0:monthName:label", "Feb 2018");
        getTester().assertLabel("archive:1:monthName:label", "Dec 2017");
    }

    private void assertNewStudy(String base, int studyIndex, String headline, String description, String reference) {
        final String path = base + "topicStudies:" + studyIndex + ":";
        getTester().assertLabel(path + "headline", headline);
        getTester().assertLabel(path + "description", description);
        getTester().assertLabel(path + "reference:referenceLabel", reference);
        getTester().assertComponent(path + "reference", Link.class);
    }

    @Test
    public void canAccessPublicPaperDetailPageForSpecificPaper_andReturnToNewStudyListPageFromThere() {
        getTester().startPage(makePage());
        getTester().assertRenderedPage(getPageClass());

        getTester().clickLink("topics:1:topicStudies:0:reference");
        getTester().assertRenderedPage(PublicPaperDetailPage.class);

        getTester()
            .newFormTester("form")
            .submit("back");
        getTester().assertRenderedPage(NewStudyListPage.class);
    }

    @Test
    public void renderingCommercialFonts() {
        final NewStudyListPage page = makePage();

        IHeaderResponse hr = mock(IHeaderResponse.class);
        page.renderAdditionalCommercialFonts(hr);

        // null, as commercial font is configured to not be used
        verify(hr, times(2)).render(CssHeaderItem.forReference(null));
    }

    @Test
    public void withIssueMissing() {
        PageParameters pp = new PageParameters();
        pageWithIssue(pp);
    }

    @Test
    public void withIssueNull() {
        PageParameters pp = new PageParameters();
        pp.set(PublicPageParameters.ISSUE.getName(), null);
        pageWithIssue(pp);
    }

    @Test
    public void withIssueBlank() {
        PageParameters pp = new PageParameters();
        pp.set(PublicPageParameters.ISSUE.getName(), "");
        pageWithIssue(pp);
    }

    private void pageWithIssue(final PageParameters pp) {
        final NewStudyListPage page = new NewStudyListPage(pp);
        getTester().startPage(page);
        getTester().assertRenderedPage(getPageClass());
        verify(serviceMock, times(2)).findMostRecentNewStudyTopics("en_us");
        verify(serviceMock, never()).findNewStudyTopicsForNewsletterIssue(anyString(), anyString());
    }

    @Test
    public void withIssuePresent() {
        PageParameters pp = new PageParameters();
        pp.set(PublicPageParameters.ISSUE.getName(), "1806");
        final NewStudyListPage page = new NewStudyListPage(pp);
        getTester().startPage(page);
        getTester().assertRenderedPage(getPageClass());
        verify(serviceMock, times(2)).findNewStudyTopicsForNewsletterIssue("1806", "en_us");
        verify(serviceMock, never()).findMostRecentNewStudyTopics(anyString());
    }

    @Test
    public void icon_withFreeFont() {
        final NewStudyListPage page = makePage();
        final IconType icon = page.chooseIcon(GlyphIconType.arrowright, IcoMoonIconType.arrow_right);
        assertThat(icon).isEqualTo(GlyphIconType.arrowright);
    }

    @Test
    public void icon_withCommercialFont() {
        final ScipamatoPublicProperties applicationProperties = mock(ScipamatoPublicProperties.class);
        when(applicationProperties.isCommercialFontPresent()).thenReturn(true);
        final NewStudyListPage page = new NewStudyListPage(new PageParameters()) {
            @Override
            protected ApplicationPublicProperties getProperties() {
                return applicationProperties;
            }
        };
        final IconType icon = page.chooseIcon(GlyphIconType.arrowright, IcoMoonIconType.arrow_right);
        assertThat(icon).isEqualTo(IcoMoonIconType.arrow_right);
    }
}
