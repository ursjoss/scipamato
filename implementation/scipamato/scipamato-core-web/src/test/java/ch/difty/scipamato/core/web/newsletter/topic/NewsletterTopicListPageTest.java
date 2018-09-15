package ch.difty.scipamato.core.web.newsletter.topic;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.util.tester.FormTester;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.web.component.table.column.LinkIconPanel;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicTranslation;
import ch.difty.scipamato.core.persistence.NewsletterTopicService;
import ch.difty.scipamato.core.web.common.BasePageTest;

@SuppressWarnings("SameParameterValue")
public class NewsletterTopicListPageTest extends BasePageTest<NewsletterTopicListPage> {

    private final List<NewsletterTopicDefinition> results = new ArrayList<>();

    @MockBean
    protected NewsletterTopicService newsletterTopicServiceMock;

    @Override
    protected void setUpHook() {
        final NewsletterTopicTranslation ntt1_de = new NewsletterTopicTranslation(1, "de", "thema1", 1);
        final NewsletterTopicTranslation ntt1_en = new NewsletterTopicTranslation(2, "en", "topic1", 1);
        final NewsletterTopicTranslation ntt1_fr = new NewsletterTopicTranslation(3, "fr", "theme1", 1);
        final NewsletterTopicDefinition ntd1 = new NewsletterTopicDefinition(1, "de", 1, ntt1_de, ntt1_en, ntt1_fr);

        final NewsletterTopicTranslation ntt2_de = new NewsletterTopicTranslation(4, "de", "thema2", 1);
        final NewsletterTopicTranslation ntt2_en = new NewsletterTopicTranslation(5, "en", "topic2", 1);
        final NewsletterTopicTranslation ntt2_fr = new NewsletterTopicTranslation(6, "fr", "theme2", 1);
        final NewsletterTopicDefinition ntd2 = new NewsletterTopicDefinition(2, "de", 1, ntt2_de, ntt2_en, ntt2_fr);

        results.addAll(List.of(ntd1, ntd2));

        when(newsletterTopicServiceMock.countByFilter(isA(NewsletterTopicFilter.class))).thenReturn(results.size());
        when(newsletterTopicServiceMock.findPageOfNewsletterTopicDefinitions(isA(NewsletterTopicFilter.class),
            isA(PaginationRequest.class))).thenReturn(results);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(newsletterTopicServiceMock);
    }

    @Override
    protected NewsletterTopicListPage makePage() {
        return new NewsletterTopicListPage(null);
    }

    @Override
    protected Class<NewsletterTopicListPage> getPageClass() {
        return NewsletterTopicListPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        assertFilterForm("filterForm");
        final String[] headers = { "Translations" };
        final String[] values = { "DE: 'thema1'; EN: 'topic1'; FR: 'theme1'".replace("'", "&#039;") };
        assertResultTable("results", headers, values);

        verify(newsletterTopicServiceMock).countByFilter(isA(NewsletterTopicFilter.class));
        verify(newsletterTopicServiceMock).findPageOfNewsletterTopicDefinitions(isA(NewsletterTopicFilter.class),
            isA(PaginationRequest.class));
    }

    private void assertFilterForm(final String b) {
        getTester().assertComponent(b, Form.class);
        assertLabeledTextField(b, "title");
        getTester().assertComponent(b + ":newNewsletterTopic", BootstrapAjaxButton.class);
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
    public void clickingOnNewsletterTopicTitle_forwardsToNewsletterTopicEntryPage_withModelLoaded() {
        getTester().startPage(getPageClass());

        getTester().clickLink("results:body:rows:1:cells:1:cell:link");
        getTester().assertRenderedPage(NewsletterTopicEditPage.class);

        // verify the newletter was loaded in the target page
        getTester().assertModelValue("form:translations:1:title", "thema1");
        getTester().assertModelValue("form:translations:2:title", "topic1");
        getTester().assertModelValue("form:translations:3:title", "theme1");

        verify(newsletterTopicServiceMock).countByFilter(isA(NewsletterTopicFilter.class));
        verify(newsletterTopicServiceMock).findPageOfNewsletterTopicDefinitions(isA(NewsletterTopicFilter.class),
            isA(PaginationRequest.class));
    }

    @Test
    public void clickingNewNewslettterTopic_forwardsToNewsletterTopicEditPage() {
        NewsletterTopicTranslation ntt_en = new NewsletterTopicTranslation(1, "en", "ntt_en", 1);
        NewsletterTopicDefinition ntd = new NewsletterTopicDefinition(1, "en", 1, ntt_en);
        when(newsletterTopicServiceMock.newUnpersistedNewsletterTopicDefinition()).thenReturn(ntd);

        getTester().startPage(getPageClass());
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("filterForm");
        formTester.submit("newNewsletterTopic");

        getTester().assertRenderedPage(NewsletterTopicEditPage.class);

        FormTester targetFormTester = getTester().newFormTester("form");

        verify(newsletterTopicServiceMock).countByFilter(isA(NewsletterTopicFilter.class));
        verify(newsletterTopicServiceMock).findPageOfNewsletterTopicDefinitions(isA(NewsletterTopicFilter.class),
            isA(PaginationRequest.class));
        verify(newsletterTopicServiceMock).newUnpersistedNewsletterTopicDefinition();
    }

    private void validateLinkIconColumn(final int row, final String status, final String value) {
        String bodyRow = "results:body:rows:" + row + ":cells:";
        getTester().assertLabel(bodyRow + "3:cell", status);
        getTester().assertComponent(bodyRow + "4:cell", LinkIconPanel.class);
        getTester().assertModelValue(bodyRow + "4:cell", value);
    }

}