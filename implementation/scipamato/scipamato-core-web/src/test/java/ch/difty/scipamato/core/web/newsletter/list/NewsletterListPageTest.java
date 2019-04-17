package ch.difty.scipamato.core.web.newsletter.list;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.util.tester.FormTester;
import org.junit.After;
import org.junit.Test;

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.web.component.table.column.LinkIconPanel;
import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter;
import ch.difty.scipamato.core.web.common.BasePageTest;
import ch.difty.scipamato.core.web.newsletter.edit.NewsletterEditPage;

@SuppressWarnings("SameParameterValue")
public class NewsletterListPageTest extends BasePageTest<NewsletterListPage> {

    private Newsletter newsletterInProgress = Newsletter
        .builder()
        .issue("1801")
        .issueDate(LocalDate.parse("2018-01-01"))
        .publicationStatus(PublicationStatus.WIP)
        .build();
    private Newsletter newsletterPublished  = Newsletter
        .builder()
        .issue("1801")
        .issueDate(LocalDate.parse("2018-01-01"))
        .publicationStatus(PublicationStatus.PUBLISHED)
        .build();

    private final List<Newsletter> results = new ArrayList<>();

    @Override
    protected void setUpHook() {
        newsletterPublished.setId(1);
        newsletterInProgress.setId(2);
        results.add(newsletterInProgress);
        results.add(newsletterPublished);
        when(newsletterServiceMock.countByFilter(isA(NewsletterFilter.class))).thenReturn(results.size());
        when(newsletterServiceMock.findPageByFilter(isA(NewsletterFilter.class),
            isA(PaginationRequest.class))).thenReturn(results);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(newsletterServiceMock);
    }

    @Override
    protected NewsletterListPage makePage() {
        return new NewsletterListPage(null);
    }

    @Override
    protected Class<NewsletterListPage> getPageClass() {
        return NewsletterListPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        assertFilterForm("filterForm");

        final String[] headers = { "Issue", "Issue Date", "Publication Status" };
        final String[] values = { "1801", "2018-01-01", "In Progress" };
        assertResultTable("results", headers, values);

        verify(newsletterServiceMock).countByFilter(isA(NewsletterFilter.class));
        verify(newsletterServiceMock).findPageByFilter(isA(NewsletterFilter.class), isA(PaginationRequest.class));
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
    }

    private void assertFilterForm(final String b) {
        getTester().assertComponent(b, Form.class);
        assertLabeledTextField(b, "issue");
        assertLabeledBootstrapSelect(b, "publicationStatus");
        assertLabeledBootstrapSelect(b, "topics");
        getTester().assertComponent(b + ":newNewsletter", BootstrapAjaxButton.class);
    }

    private void assertResultTable(final String b, final String[] labels, final String[] values) {
        getTester().assertComponent(b, BootstrapDefaultDataTable.class);
        assertHeaderColumns(b, labels);
        assertTableValuesOfRow(b, 1, 1, values);
    }

    private void assertHeaderColumns(final String b, final String[] labels) {
        int idx = 0;
        for (final String label : labels)
            getTester().assertLabel(
                b + ":topToolbars:toolbars:2:headers:" + ++idx + ":header:orderByLink:header_body:label", label);
    }

    private void assertTableValuesOfRow(final String b, final int rowIdx, final Integer colIdxAsLink,
        final String[] values) {
        if (colIdxAsLink != null)
            getTester().assertComponent(b + ":body:rows:" + rowIdx + ":cells:" + colIdxAsLink + ":cell:link",
                Link.class);
        int colIdx = 1;
        for (final String value : values)
            getTester().assertLabel(b + ":body:rows:" + rowIdx + ":cells:" + colIdx + ":cell" + (
                colIdxAsLink != null && colIdx++ == colIdxAsLink ? ":link:label" : ""), value);
    }

    @Test
    public void clickingOnNewsletterIssue_forwardsToNewsletterEntryPage_withModelLoaded() {
        getTester().startPage(getPageClass());

        getTester().clickLink("results:body:rows:1:cells:1:cell:link");
        getTester().assertRenderedPage(NewsletterEditPage.class);

        // verify the newsletter was loaded in the target page
        FormTester formTester = getTester().newFormTester("form");
        assertThat(formTester.getTextComponentValue("issue")).isEqualTo("1801");

        verify(newsletterServiceMock).countByFilter(isA(NewsletterFilter.class));
        verify(newsletterServiceMock).findPageByFilter(isA(NewsletterFilter.class), isA(PaginationRequest.class));
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
    }

    @Test
    public void clickingNewNewsletter_forwardsToNewsletterEditPage() {
        when(newsletterServiceMock.canCreateNewsletterInProgress()).thenReturn(true);
        getTester().startPage(getPageClass());
        getTester().assertRenderedPage(getPageClass());

        getTester().assertEnabled("filterForm:newNewsletter");
        FormTester formTester = getTester().newFormTester("filterForm");
        formTester.submit("newNewsletter");

        getTester().assertRenderedPage(NewsletterEditPage.class);

        // verify we have a blank newsletter in the target page
        FormTester targetFormTester = getTester().newFormTester("form");
        assertThat(targetFormTester.getTextComponentValue("issue")).isBlank();

        verify(newsletterServiceMock).countByFilter(isA(NewsletterFilter.class));
        verify(newsletterServiceMock).findPageByFilter(isA(NewsletterFilter.class), isA(PaginationRequest.class));
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
    }

    @Test
    public void givenNoNewNewsletterShallBeCreated_newNewsletterButtonIsDisabled() {
        when(newsletterServiceMock.canCreateNewsletterInProgress()).thenReturn(false);
        getTester().startPage(getPageClass());
        getTester().assertRenderedPage(getPageClass());

        getTester().assertDisabled("filterForm:newNewsletter");

        verify(newsletterServiceMock).countByFilter(isA(NewsletterFilter.class));
        verify(newsletterServiceMock).findPageByFilter(isA(NewsletterFilter.class), isA(PaginationRequest.class));
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
    }

    @Test
    public void clickingSortTopicsIcon_forwardsToSortTopicsPage() {
        getTester().startPage(getPageClass());
        getTester().assertRenderedPage(getPageClass());

        getTester().clickLink("results:body:rows:1:cells:4:cell:link");

        getTester().assertRenderedPage(NewsletterTopicSortPage.class);

        verify(newsletterServiceMock).countByFilter(isA(NewsletterFilter.class));
        verify(newsletterServiceMock).findPageByFilter(isA(NewsletterFilter.class), isA(PaginationRequest.class));
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
    }

    @Test
    public void clickingRemoveIcon_forNewsletterInProgress_delegatesRemovalToServiceAndRefreshesResultPanel() {
        getTester().startPage(getPageClass());
        getTester().assertRenderedPage(getPageClass());

        getTester().clickLink("results:body:rows:1:cells:5:cell:link");

        getTester().assertComponentOnAjaxResponse("filterForm:newNewsletter");
        getTester().assertComponentOnAjaxResponse("results");
        getTester().assertComponentOnAjaxResponse("feedback");

        getTester().assertInfoMessages("Newsletter 1801 was deleted successfully.");

        verify(newsletterServiceMock, times(2)).countByFilter(isA(NewsletterFilter.class));
        verify(newsletterServiceMock, times(2)).findPageByFilter(isA(NewsletterFilter.class),
            isA(PaginationRequest.class));
        verify(newsletterServiceMock).remove(newsletterInProgress);
        verify(newsletterServiceMock, times(2)).canCreateNewsletterInProgress();
    }

    @Test
    public void clickingRemoveIcon_forNewsletterPublished_doesNotDelegate() {
        getTester().startPage(getPageClass());
        getTester().assertRenderedPage(getPageClass());

        getTester().clickLink("results:body:rows:2:cells:5:cell:link");

        verify(newsletterServiceMock).countByFilter(isA(NewsletterFilter.class));
        verify(newsletterServiceMock).findPageByFilter(isA(NewsletterFilter.class), isA(PaginationRequest.class));
        verify(newsletterServiceMock, never()).remove(newsletterInProgress);
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
    }

    @Test
    public void assertVisibilityOfRemoveLink_dependingOnPublicationState() {
        getTester().startPage(getPageClass());
        getTester().assertRenderedPage(getPageClass());

        validateLinkIconColumn(1, "In Progress", "fas fa-trash-alt fa-fw");
        validateLinkIconColumn(2, "Published", "");

        verify(newsletterServiceMock).countByFilter(isA(NewsletterFilter.class));
        verify(newsletterServiceMock).findPageByFilter(isA(NewsletterFilter.class), isA(PaginationRequest.class));
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
    }

    private void validateLinkIconColumn(final int row, final String status, final String value) {
        String bodyRow = "results:body:rows:" + row + ":cells:";
        getTester().assertLabel(bodyRow + "3:cell", status);
        getTester().assertComponent(bodyRow + "4:cell", LinkIconPanel.class);
        getTester().assertModelValue(bodyRow + "4:cell", "fas fa-random fa-fw");
        getTester().assertComponent(bodyRow + "5:cell", LinkIconPanel.class);
        getTester().assertModelValue(bodyRow + "5:cell", value);
    }

    @Test
    public void changingPublicationStatus_updatesResultTable() {
        getTester().startPage(getPageClass());

        getTester().executeAjaxEvent("filterForm:publicationStatus", "change");
        getTester().assertComponentOnAjaxResponse("results");

        verify(newsletterServiceMock, times(2)).countByFilter(isA(NewsletterFilter.class));
        verify(newsletterServiceMock, times(2)).findPageByFilter(isA(NewsletterFilter.class),
            isA(PaginationRequest.class));
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
    }

    @Test
    public void changingNewsletterTopic_updatesResultTable() {
        getTester().startPage(getPageClass());

        getTester().executeAjaxEvent("filterForm:topics", "change");
        getTester().assertComponentOnAjaxResponse("results");

        verify(newsletterServiceMock, times(2)).countByFilter(isA(NewsletterFilter.class));
        verify(newsletterServiceMock, times(2)).findPageByFilter(isA(NewsletterFilter.class),
            isA(PaginationRequest.class));
        verify(newsletterServiceMock).canCreateNewsletterInProgress();
    }

}