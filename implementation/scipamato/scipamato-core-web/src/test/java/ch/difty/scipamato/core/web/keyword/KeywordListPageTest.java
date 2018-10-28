package ch.difty.scipamato.core.web.keyword;

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
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordFilter;
import ch.difty.scipamato.core.entity.keyword.KeywordTranslation;
import ch.difty.scipamato.core.persistence.KeywordService;
import ch.difty.scipamato.core.web.common.BasePageTest;

@SuppressWarnings("SameParameterValue")
public class KeywordListPageTest extends BasePageTest<KeywordListPage> {

    private static final int COLUMN_ID_WITH_LINK = 1;

    private final List<KeywordDefinition> results = new ArrayList<>();

    @MockBean
    protected KeywordService keywordServiceMock;

    @Override
    protected void setUpHook() {
        final KeywordTranslation kt1_de = new KeywordTranslation(1, "de", "Name1", 1);
        final KeywordTranslation kt1_en = new KeywordTranslation(2, "en", "name1", 1);
        final KeywordTranslation kt1_fr = new KeywordTranslation(3, "fr", "nom1", 1);
        final KeywordDefinition kd1 = new KeywordDefinition(1, "de", "nameOverride", 1, kt1_de, kt1_en, kt1_fr);

        final KeywordTranslation kt2_en = new KeywordTranslation(5, "en", "name2", 1);
        final KeywordTranslation kt2_fr = new KeywordTranslation(6, "fr", "nom2", 1);
        final KeywordTranslation kt2_de = new KeywordTranslation(4, "de", "Name2", 1);
        final KeywordDefinition kd2 = new KeywordDefinition(2, "de", 1, kt2_de, kt2_en, kt2_fr);

        results.addAll(List.of(kd1, kd2));

        when(keywordServiceMock.countByFilter(isA(KeywordFilter.class))).thenReturn(results.size());
        when(keywordServiceMock.findPageOfKeywordDefinitions(isA(KeywordFilter.class),
            isA(PaginationRequest.class))).thenReturn(results);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(keywordServiceMock);
    }

    @Override
    protected KeywordListPage makePage() {
        return new KeywordListPage(null);
    }

    @Override
    protected Class<KeywordListPage> getPageClass() {
        return KeywordListPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        assertFilterForm("filterForm");
        final String[] headers = { "Translations", "Search Override" };
        final String[] values = { "DE: 'Name1'; EN: 'name1'; FR: 'nom1'".replace("'", "&#039;"), "nameOverride" };
        assertResultTable("results", headers, values);

        verify(keywordServiceMock).countByFilter(isA(KeywordFilter.class));
        verify(keywordServiceMock).findPageOfKeywordDefinitions(isA(KeywordFilter.class), isA(PaginationRequest.class));
    }

    private void assertFilterForm(final String b) {
        getTester().assertComponent(b, Form.class);
        assertLabeledTextField(b, "name");
        getTester().assertComponent(b + ":newKeyword", BootstrapAjaxButton.class);
    }

    private void assertResultTable(final String b, final String[] labels, final String[] values) {
        getTester().assertComponent(b, BootstrapDefaultDataTable.class);
        assertHeaderColumns(b, labels);
        assertTableValuesOfRow(b, 1, COLUMN_ID_WITH_LINK, values);
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
    public void clickingOnKeywordTitle_forwardsToKeywordEditPage_withModelLoaded() {
        getTester().startPage(getPageClass());

        getTester().clickLink("results:body:rows:1:cells:" + COLUMN_ID_WITH_LINK + ":cell:link");
        getTester().assertRenderedPage(KeywordEditPage.class);

        // verify the keywords were loaded into the target page
        getTester().assertModelValue("form:translations:1:name", "Name1");
        getTester().assertModelValue("form:translations:2:name", "name1");
        getTester().assertModelValue("form:translations:3:name", "nom1");

        verify(keywordServiceMock).countByFilter(isA(KeywordFilter.class));
        verify(keywordServiceMock).findPageOfKeywordDefinitions(isA(KeywordFilter.class), isA(PaginationRequest.class));
    }

    @Test
    public void clickingNewKeyword_forwardsToKeywordEditPage() {
        KeywordTranslation kt_en = new KeywordTranslation(1, "en", "kt_en", 1);
        KeywordDefinition kd = new KeywordDefinition(1, "en", 1, kt_en);
        when(keywordServiceMock.newUnpersistedKeywordDefinition()).thenReturn(kd);

        getTester().startPage(getPageClass());
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("filterForm");
        formTester.submit("newKeyword");

        getTester().assertRenderedPage(KeywordEditPage.class);

        FormTester targetFormTester = getTester().newFormTester("form");

        verify(keywordServiceMock).countByFilter(isA(KeywordFilter.class));
        verify(keywordServiceMock).findPageOfKeywordDefinitions(isA(KeywordFilter.class), isA(PaginationRequest.class));
        verify(keywordServiceMock).newUnpersistedKeywordDefinition();
    }

    private void validateLinkIconColumn(final int row, final String status, final String value) {
        String bodyRow = "results:body:rows:" + row + ":cells:";
        getTester().assertLabel(bodyRow + "3:cell", status);
        getTester().assertComponent(bodyRow + "4:cell", LinkIconPanel.class);
        getTester().assertModelValue(bodyRow + "4:cell", value);
    }

}