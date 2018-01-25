package ch.difty.scipamato.public_.web.paper.browse;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.public_.entity.PublicPaper;
import ch.difty.scipamato.public_.persistence.api.PublicPaperService;
import ch.difty.scipamato.public_.web.common.BasePageTest;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapExternalLink;

public class PublicPaperDetailPageTest extends BasePageTest<PublicPaperDetailPage> {

    private static final long NUMBER = 17l;

    private PublicPaper paper;

    @MockBean
    private PublicPaperService serviceMock;

    @Override
    protected void setUpHook() {
        super.setUpHook();

        paper = new PublicPaper(1l, NUMBER, 10000, "authors", "title", "location", 2017, "goals", "methods",
                "population", "result", "comment");

        when(serviceMock.findByNumber(NUMBER)).thenReturn(Optional.of(paper));
    }

    @Override
    protected void doVerify() {
        verify(serviceMock).findByNumber(NUMBER);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(serviceMock);
    }

    @Override
    protected PublicPaperDetailPage makePage() {
        PageParameters pp = new PageParameters();
        pp.set(PublicPaperDetailPage.PAGE_PARAM_NUMBER, NUMBER);
        return new PublicPaperDetailPage(pp);
    }

    @Override
    protected Class<PublicPaperDetailPage> getPageClass() {
        return PublicPaperDetailPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        String b = "form";
        getTester().assertComponent(b, Form.class);
        assertHeader(b, true);
        assertReferenceTopic(b);

        assertVisible(b, "goals", "Goals:");
        assertVisible(b, "population", "Population:");
        assertVisible(b, "methods", "Methods:");
        assertVisible(b, "result", "Results:");
        assertVisible(b, "comment", "Comment:");
    }

    private void assertHeader(String form, boolean pubmedVisible) {
        getTester().assertLabel(form + ":captionLabel", "Summary of Paper (No 17)");
        getTester().assertLabel(form + ":title", "title");

        getTester().assertComponent(form + ":back", BootstrapButton.class);
        getTester().assertComponent(form + ":previous", BootstrapButton.class);
        getTester().assertComponent(form + ":next", BootstrapButton.class);
        if (pubmedVisible)
            getTester().assertComponent(form + ":pubmed", BootstrapExternalLink.class);
        else
            getTester().assertInvisible(form + ":pubmed");
    }

    private void assertReferenceTopic(String form) {
        getTester().assertLabel(form + ":referenceLabel", "Reference:");
        getTester().assertLabel(form + ":authors", "authors");
        getTester().assertLabel(form + ":title2", "title");
        getTester().assertLabel(form + ":location", "location");
    }

    private void assertVisible(final String parent, final String topic, final String labelText) {
        assertTopic(parent, topic, labelText, true);
    }

    private void assertInvisible(final String parent, final String topic) {
        assertTopic(parent, topic, null, false);
    }

    private void assertTopic(final String parent, final String topic, final String labelText, final boolean visible) {
        String fullTopic = parent + ":" + topic;
        if (visible) {
            getTester().assertLabel(fullTopic + "Label", labelText);
            getTester().assertLabel(fullTopic, topic);
        } else {
            getTester().assertInvisible(fullTopic + "Label");
            getTester().assertInvisible(fullTopic);
        }
    }

    @Test
    public void withGoalsMissing_hideGoalsTopic() {
        PublicPaper p = new PublicPaper(1l, NUMBER, 10000, "authors", "title", "location", 2017, null, "methods",
                "population", "result", "comment");
        getTester().startPage(new PublicPaperDetailPage(Model.of(p), null));

        String b = "form";
        assertHeader(b, true);
        assertReferenceTopic(b);

        assertInvisible(b, "goals");
        assertVisible(b, "population", "Population:");
        assertVisible(b, "methods", "Methods:");
        assertVisible(b, "result", "Results:");
        assertVisible(b, "comment", "Comment:");
    }

    @Test
    public void withPopulationMissing_hidePopulationTopic() {
        PublicPaper p = new PublicPaper(1l, NUMBER, 10000, "authors", "title", "location", 2017, "goals", "methods",
                null, "result", "comment");
        getTester().startPage(new PublicPaperDetailPage(Model.of(p), null));

        String b = "form";
        assertHeader(b, true);
        assertReferenceTopic(b);

        assertVisible(b, "goals", "Goals:");
        assertInvisible(b, "population");
        assertVisible(b, "methods", "Methods:");
        assertVisible(b, "result", "Results:");
        assertVisible(b, "comment", "Comment:");
    }

    @Test
    public void withMethodsMissing_hideMethodsTopic() {
        PublicPaper p = new PublicPaper(1l, NUMBER, 10000, "authors", "title", "location", 2017, "goals", null,
                "population", "result", "comment");
        getTester().startPage(new PublicPaperDetailPage(Model.of(p), null));

        String b = "form";
        assertHeader(b, true);
        assertReferenceTopic(b);

        assertVisible(b, "goals", "Goals:");
        assertVisible(b, "population", "Population:");
        assertInvisible(b, "methods");
        assertVisible(b, "result", "Results:");
        assertVisible(b, "comment", "Comment:");
    }

    @Test
    public void withResultMissing_hideResultTopic() {
        PublicPaper p = new PublicPaper(1l, NUMBER, 10000, "authors", "title", "location", 2017, "goals", "methods",
                "population", null, "comment");
        getTester().startPage(new PublicPaperDetailPage(Model.of(p), null));

        String b = "form";
        assertHeader(b, true);
        assertReferenceTopic(b);

        assertVisible(b, "goals", "Goals:");
        assertVisible(b, "population", "Population:");
        assertVisible(b, "methods", "Methods:");
        assertInvisible(b, "result");
        assertVisible(b, "comment", "Comment:");
    }

    @Test
    public void withCommentMissing_hideCommentTopic() {
        PublicPaper p = new PublicPaper(1l, NUMBER, 10000, "authors", "title", "location", 2017, "goals", "methods",
                "population", "result", null);
        getTester().startPage(new PublicPaperDetailPage(Model.of(p), null));

        String b = "form";
        assertHeader(b, true);
        assertReferenceTopic(b);

        assertVisible(b, "goals", "Goals:");
        assertVisible(b, "population", "Population:");
        assertVisible(b, "methods", "Methods:");
        assertVisible(b, "result", "Results:");
        assertInvisible(b, "comment");
    }

    @Test
    public void withNullPmId_pubMedLinkIsInvisible() {
        PublicPaper p = new PublicPaper(1l, NUMBER, null, "authors", "title", "location", 2017, "goals", "methods",
                "population", "result", "comment");
        getTester().startPage(new PublicPaperDetailPage(Model.of(p), null));

        String b = "form";
        assertHeader(b, false);
        assertReferenceTopic(b);

        assertVisible(b, "goals", "Goals:");
        assertVisible(b, "population", "Population:");
        assertVisible(b, "methods", "Methods:");
        assertVisible(b, "result", "Results:");
        assertVisible(b, "comment", "Comment:");
    }
}
