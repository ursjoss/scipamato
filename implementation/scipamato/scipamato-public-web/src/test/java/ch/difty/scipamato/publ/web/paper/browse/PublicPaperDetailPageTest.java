package ch.difty.scipamato.publ.web.paper.browse;

import static org.mockito.Mockito.*;

import java.util.Optional;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapExternalLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.publ.entity.PublicPaper;
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.publ.persistence.api.PublicPaperService;
import ch.difty.scipamato.publ.web.PublicPageParameters;
import ch.difty.scipamato.publ.web.common.BasePageTest;

public class PublicPaperDetailPageTest extends BasePageTest<PublicPaperDetailPage> {

    private static final long NUMBER = 17L;

    @MockBean
    private PublicPaperService serviceMock;

    @Override
    protected void setUpHook() {
        super.setUpHook();

        PublicPaper paper = new PublicPaper(1L, NUMBER, 10000, "authors", "auths", "title", "location", "journal", 2017,
            "goals", "methods", "population", "result", "comment");

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
        pp.set(PublicPageParameters.NUMBER.getName(), NUMBER);
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
        PublicPaper p = new PublicPaper(1L, NUMBER, 10000, "authors", "auths", "title", "location", "journal", 2017,
            null, "methods", "population", "result", "comment");
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
        PublicPaper p = new PublicPaper(1L, NUMBER, 10000, "authors", "auths", "title", "location", "journal", 2017,
            "goals", "methods", null, "result", "comment");
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
        PublicPaper p = new PublicPaper(1L, NUMBER, 10000, "authors", "auths", "title", "location", "journal", 2017,
            "goals", null, "population", "result", "comment");
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
        PublicPaper p = new PublicPaper(1L, NUMBER, 10000, "authors", "auths", "title", "location", "journal", 2017,
            "goals", "methods", "population", null, "comment");
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
        PublicPaper p = new PublicPaper(1L, NUMBER, 10000, "authors", "auths", "title", "location", "journal", 2017,
            "goals", "methods", "population", "result", null);
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
        PublicPaper p = new PublicPaper(1L, NUMBER, null, "authors", "auths", "title", "location", "journal", 2017,
            "goals", "methods", "population", "result", "comment");
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

    @Test
    public void clickingPrevious_withPreviousItemAvailable_skipsBack() {
        final long previousId = 1;
        when(getItemNavigator().hasPrevious()).thenReturn(true);
        when(getItemNavigator().getItemWithFocus()).thenReturn(previousId);

        PublicPaper p = new PublicPaper(2L, NUMBER, 2, "authors", "auths", "title", "location", "journal", 2017,
            "goals", "methods", "population", "result", "comment");
        getTester().startPage(new PublicPaperDetailPage(Model.of(p), null));

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("previous");

        getTester().assertRenderedPage(PublicPaperDetailPage.class);

        verify(getItemNavigator(), times(2)).hasPrevious();
        verify(getItemNavigator()).previous();
        verify(getItemNavigator()).getItemWithFocus();

        verify(serviceMock).findByNumber(previousId);
    }

    @Test
    public void clickingNext_withNextItemAvailable_skipsForward() {
        final long nextId = 2;
        when(getItemNavigator().hasNext()).thenReturn(true);
        when(getItemNavigator().getItemWithFocus()).thenReturn(nextId);

        PublicPaper p = new PublicPaper(1L, NUMBER, 2, "authors", "auths", "title", "location", "journal", 2017,
            "goals", "methods", "population", "result", "comment");
        getTester().startPage(new PublicPaperDetailPage(Model.of(p), null));

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("next");

        getTester().assertRenderedPage(PublicPaperDetailPage.class);

        verify(getItemNavigator(), times(2)).hasNext();
        verify(getItemNavigator()).next();
        verify(getItemNavigator()).getItemWithFocus();

        verify(serviceMock).findByNumber(nextId);
    }

    @Test
    public void clickingNext_withNextItemAvailable_butWithNoIdReturnedFromItemManager_triesToSkipForwardButRemainsOnPage() {
        when(getItemNavigator().hasNext()).thenReturn(true);
        when(getItemNavigator().getItemWithFocus()).thenReturn(null);

        PublicPaper p = new PublicPaper(1L, NUMBER, 2, "authors", "auths", "title", "location", "journal", 2017,
            "goals", "methods", "population", "result", "comment");
        getTester().startPage(new PublicPaperDetailPage(Model.of(p), null));

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("next");

        getTester().assertRenderedPage(PublicPaperDetailPage.class);

        verify(getItemNavigator(), times(2)).hasNext();
        verify(getItemNavigator()).next();
        verify(getItemNavigator()).getItemWithFocus();

        verify(serviceMock, never()).findByNumber(anyLong());
    }

    @Test
    public void clickingBack_withoutCallingRef_jumpsToPublicPage() {
        PublicPaper p = new PublicPaper(2L, NUMBER, 2, "authors", "auths", "title", "location", "journal", 2017,
            "goals", "methods", "population", "result", "comment");
        getTester().startPage(new PublicPaperDetailPage(Model.of(p), null));

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("back");

        getTester().assertRenderedPage(PublicPage.class);

        verify(serviceMock).findPageOfNumbersByFilter(isA(PublicPaperFilter.class), isA(PaginationRequest.class));
    }

    @Test
    public void constructingPage_withPageParmeterProvidingNumber_loadsPaperWithNumber() {
        PageParameters pp = new PageParameters();
        pp.set(PublicPageParameters.NUMBER.getName(), NUMBER);
        new PublicPaperDetailPage(pp);
        verify(serviceMock).findByNumber(NUMBER);
    }

    @Test
    public void constructingPage_withoutPageParmeterProvidingNumber_loadsNothing() {
        new PublicPaperDetailPage(new PageParameters());
        verify(serviceMock, never()).findByNumber(anyLong());
    }

}
