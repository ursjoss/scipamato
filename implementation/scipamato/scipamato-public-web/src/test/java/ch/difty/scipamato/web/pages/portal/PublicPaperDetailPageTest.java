package ch.difty.scipamato.web.pages.portal;

import org.apache.wicket.model.Model;
import org.junit.Test;

import ch.difty.scipamato.entity.PublicPaper;
import ch.difty.scipamato.web.pages.BasePageTest;

public class PublicPaperDetailPageTest extends BasePageTest<PublicPaperDetailPage> {

    private PublicPaper paper;

    @Override
    protected void setUpHook() {
        super.setUpHook();
        paper = new PublicPaper(1l, 1l, 10000, "authors", "title", "location", 2017, "goals", "methods", "population", "result", "comment");
    }

    @Override
    protected PublicPaperDetailPage makePage() {
        return new PublicPaperDetailPage(Model.of(paper), null);
    }

    @Override
    protected Class<PublicPaperDetailPage> getPageClass() {
        return PublicPaperDetailPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        assertHeader();
        assertReferenceTopic();

        assertVisible("goals", "Goals:");
        assertVisible("population", "Population:");
        assertVisible("methods", "Methods:");
        assertVisible("result", "Results:");
        assertVisible("comment", "Comment:");
    }

    private void assertHeader() {
        getTester().assertLabel("captionLabel", "Summary of Paper (ID 1)");
        getTester().assertLabel("title", "title");
    }

    private void assertReferenceTopic() {
        getTester().assertLabel("referenceLabel", "Reference:");
        getTester().assertLabel("authors", "authors");
        getTester().assertLabel("title2", "title");
        getTester().assertLabel("location", "location");
    }

    private void assertVisible(final String topic, final String labelText) {
        assertTopic(topic, labelText, true);
    }

    private void assertInvisible(final String topic) {
        assertTopic(topic, null, false);
    }

    private void assertTopic(final String topic, final String labelText, final boolean visible) {
        if (visible) {
            getTester().assertLabel(topic + "Label", labelText);
            getTester().assertLabel(topic, topic);
        } else {
            getTester().assertInvisible(topic + "Label");
            getTester().assertInvisible(topic);
        }
    }

    @Test
    public void withGoalsMissing_hideGoalsTopic() {
        PublicPaper p = new PublicPaper(1l, 1l, 10000, "authors", "title", "location", 2017, null, "methods", "population", "result", "comment");
        getTester().startPage(new PublicPaperDetailPage(Model.of(p), null));

        assertHeader();
        assertReferenceTopic();

        assertInvisible("goals");
        assertVisible("population", "Population:");
        assertVisible("methods", "Methods:");
        assertVisible("result", "Results:");
        assertVisible("comment", "Comment:");
    }

    @Test
    public void withPopulationMissing_hidePopulationTopic() {
        PublicPaper p = new PublicPaper(1l, 1l, 10000, "authors", "title", "location", 2017, "goals", "methods", null, "result", "comment");
        getTester().startPage(new PublicPaperDetailPage(Model.of(p), null));

        assertHeader();
        assertReferenceTopic();

        assertVisible("goals", "Goals:");
        assertInvisible("population");
        assertVisible("methods", "Methods:");
        assertVisible("result", "Results:");
        assertVisible("comment", "Comment:");
    }

    @Test
    public void withMethodsMissing_hideMethodsTopic() {
        PublicPaper p = new PublicPaper(1l, 1l, 10000, "authors", "title", "location", 2017, "goals", null, "population", "result", "comment");
        getTester().startPage(new PublicPaperDetailPage(Model.of(p), null));

        assertHeader();
        assertReferenceTopic();

        assertVisible("goals", "Goals:");
        assertVisible("population", "Population:");
        assertInvisible("methods");
        assertVisible("result", "Results:");
        assertVisible("comment", "Comment:");
    }

    @Test
    public void withResultMissing_hideResultTopic() {
        PublicPaper p = new PublicPaper(1l, 1l, 10000, "authors", "title", "location", 2017, "goals", "methods", "population", null, "comment");
        getTester().startPage(new PublicPaperDetailPage(Model.of(p), null));

        assertHeader();
        assertReferenceTopic();

        assertVisible("goals", "Goals:");
        assertVisible("population", "Population:");
        assertVisible("methods", "Methods:");
        assertInvisible("result");
        assertVisible("comment", "Comment:");
    }

    @Test
    public void withCommentMissing_hideCommentTopic() {
        PublicPaper p = new PublicPaper(1l, 1l, 10000, "authors", "title", "location", 2017, "goals", "methods", "population", "result", null);
        getTester().startPage(new PublicPaperDetailPage(Model.of(p), null));

        assertHeader();
        assertReferenceTopic();

        assertVisible("goals", "Goals:");
        assertVisible("population", "Population:");
        assertVisible("methods", "Methods:");
        assertVisible("result", "Results:");
        assertInvisible("comment");
    }
}
