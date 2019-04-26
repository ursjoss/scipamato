package ch.difty.scipamato.core.web.newsletter.edit;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.LocalDateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;
import ch.difty.scipamato.core.web.common.BasePageTest;
import ch.difty.scipamato.core.web.paper.entry.PaperEntryPage;
import ch.difty.scipamato.core.web.paper.result.ResultPanel;

public class NewsletterEditPageTest extends BasePageTest<NewsletterEditPage> {

    private final Newsletter nl = Newsletter
        .builder()
        .issue("1804")
        .issueDate(LocalDate.parse("2018-04-01"))
        .publicationStatus(PublicationStatus.CANCELLED)
        .build();

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(newsletterServiceMock);
    }

    @Override
    protected NewsletterEditPage makePage() {
        return new NewsletterEditPage(Model.of(nl));
    }

    @Override
    protected Class<NewsletterEditPage> getPageClass() {
        return NewsletterEditPage.class;
    }

    @Override
    public void assertSpecificComponents() {
        String b = "form";
        getTester().assertComponent(b, Form.class);

        b += ":";
        getTester().assertLabel(b + "issueLabel", "Issue");
        getTester().assertComponent(b + "issue", TextField.class);

        getTester().assertLabel(b + "issueDateLabel", "Issue Date");
        getTester().assertComponent(b + "issueDate", LocalDateTextField.class);

        getTester().assertLabel(b + "publicationStatusLabel", "Publication Status");
        getTester().assertComponent(b + "publicationStatus", BootstrapSelect.class);
        getTester().assertEnabled(b + "publicationStatus");

        getTester().assertComponent(b + "submit", BootstrapButton.class);

        getTester().assertComponent("resultPanel", ResultPanel.class);
    }

    @Test
    public void submitting_callsService() {
        when(newsletterServiceMock.canCreateNewsletterInProgress()).thenReturn(true);
        when(newsletterServiceMock.saveOrUpdate(isA(Newsletter.class))).thenReturn(nl);

        getTester().startPage(NewsletterEditPage.class);

        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("issue", "1806");
        formTester.submit("submit");

        getTester().assertInfoMessages("Successfully saved Newsletter [id 0]: 1804 ({2}).");
        getTester().assertNoErrorMessage();

        verify(newsletterServiceMock).saveOrUpdate(isA(Newsletter.class));
    }

    @Test
    public void submitting_withServiceReturningNull_addsErrorMessage() {
        when(newsletterServiceMock.canCreateNewsletterInProgress()).thenReturn(true);
        when(newsletterServiceMock.saveOrUpdate(isA(Newsletter.class))).thenReturn(null);

        getTester().startPage(NewsletterEditPage.class);

        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("issue", "1806");
        formTester.submit("submit");

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("An unexpected error occurred when trying to save Newsletter [id 0]: ");

        verify(newsletterServiceMock).saveOrUpdate(isA(Newsletter.class));
    }

    @Test
    public void submitting_withServiceReturningOptimisticLockingException() {
        when(newsletterServiceMock.canCreateNewsletterInProgress()).thenReturn(true);
        when(newsletterServiceMock.saveOrUpdate(isA(Newsletter.class))).thenThrow(
            new OptimisticLockingException("newsletter", OptimisticLockingException.Type.UPDATE));

        getTester().startPage(NewsletterEditPage.class);

        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("issue", "1806");
        formTester.submit("submit");

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages(
            "The newsletter with id 0 has been modified concurrently by another user. Please reload it and apply your changes once more.");

        verify(newsletterServiceMock).saveOrUpdate(isA(Newsletter.class));
    }

    @Test
    public void submitting_withIllegalArgumentException() {
        when(newsletterServiceMock.canCreateNewsletterInProgress()).thenReturn(true);
        when(newsletterServiceMock.saveOrUpdate(isA(Newsletter.class))).thenThrow(
            new IllegalArgumentException("newsletter.onlyOneInStatusWipAllowed"));

        getTester().startPage(NewsletterEditPage.class);

        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("issue", "1806");
        formTester.submit("submit");

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages(
            "Another newsletter is already in status WIP. You cannot save the current newsletter in the same status.");

        verify(newsletterServiceMock).saveOrUpdate(isA(Newsletter.class));
    }

    @Test
    public void submitting_withOtherException() {
        when(newsletterServiceMock.canCreateNewsletterInProgress()).thenReturn(true);
        when(newsletterServiceMock.saveOrUpdate(isA(Newsletter.class))).thenThrow(new RuntimeException("boom"));

        getTester().startPage(NewsletterEditPage.class);

        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("issue", "1806");
        formTester.submit("submit");

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("An unexpected error occurred when trying to save Newsletter [id 0]: boom");

        verify(newsletterServiceMock).saveOrUpdate(isA(Newsletter.class));
    }

    @Test
    public void callingWithoutModel_restrictsToCreatingNewWIPNewsletter() {
        getTester().startPage(new NewsletterEditPage());
        getTester().assertRenderedPage(getPageClass());

        getTester().assertDisabled("form:publicationStatus");

        getTester().assertNoErrorMessage();
        getTester().assertNoInfoMessage();
    }

    @Test
    public void fieldsIssueAndIssueDate_areOnlyEnabledIfNewsletterIsInProgress() {
        for (final PublicationStatus ps : PublicationStatus.values()) {
            final Newsletter nl = Newsletter
                .builder()
                .issue("1804")
                .issueDate(LocalDate.parse("2018-04-01"))
                .publicationStatus(ps)
                .build();
            getTester().startPage(new NewsletterEditPage(Model.of(nl)));
            getTester().assertRenderedPage(NewsletterEditPage.class);

            String b = "form:";
            if (ps.isInProgress()) {
                getTester().assertEnabled(b + "issue");
                getTester().assertEnabled(b + "issueDate");
            } else {
                getTester().assertDisabled(b + "issue");
                getTester().assertDisabled(b + "issueDate");
            }
        }
    }

    @Test
    void clickingTitleInResultPanel_opensPaperEntryPage() {
        final List<PaperSlim> papers = new ArrayList<>();
        final PaperSlim ps = new PaperSlim();
        ps.setId(1L);
        ps.setNumber(2L);
        ps.setTitle("some title");
        ps.setPublicationYear(2019);
        ps.setFirstAuthor("Foo");
        papers.add(ps);
        final Paper p = new Paper();
        p.setId(ps.getId());
        when(paperSlimServiceMock.countByFilter(any())).thenReturn(papers.size());
        when(paperSlimServiceMock.findPageByFilter(any(), any())).thenReturn(papers);
        when(paperServiceMock.findByNumber(ps.getNumber(), "en_us")).thenReturn(Optional.of(p));

        getTester().startPage(makePage());
        getTester().assertRenderedPage(NewsletterEditPage.class);

        getTester().clickLink("resultPanel:table:body:rows:1:cells:5:cell:link");

        getTester().assertRenderedPage(PaperEntryPage.class);

        verify(paperSlimServiceMock, times(2)).countByFilter(any());
        verify(paperSlimServiceMock, times(1)).findPageByFilter(any(), any());
        verify(newsletterServiceMock, times(3)).canCreateNewsletterInProgress();
        verify(paperServiceMock).findByNumber(ps.getNumber(), "en_us");
    }
}