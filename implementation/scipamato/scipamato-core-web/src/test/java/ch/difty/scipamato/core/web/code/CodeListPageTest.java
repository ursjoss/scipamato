package ch.difty.scipamato.core.web.code;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.util.tester.FormTester;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.code.CodeDefinition;
import ch.difty.scipamato.core.entity.code.CodeFilter;
import ch.difty.scipamato.core.entity.code.CodeTranslation;
import ch.difty.scipamato.core.persistence.CodeService;
import ch.difty.scipamato.core.web.common.BasePageTest;

@SuppressWarnings("SameParameterValue")
public class CodeListPageTest extends BasePageTest<CodeListPage> {

    private static final int COLUMN_ID_WITH_LINK = 2;

    private final CodeClass cc1 = new CodeClass(1, "cc1", null);
    private final CodeClass cc2 = new CodeClass(2, "cc2", null);

    private final List<CodeDefinition> results = new ArrayList<>();

    @MockBean
    protected CodeService codeServiceMock;

    @Override
    protected void setUpHook() {
        final CodeTranslation ct1_de = new CodeTranslation(1, "de", "Name1", "a comment", 1);
        final CodeTranslation ct1_en = new CodeTranslation(2, "en", "name1", null, 1);
        final CodeTranslation ct1_fr = new CodeTranslation(3, "fr", "nom1", null, 1);
        final CodeDefinition cd1 = new CodeDefinition("1A", "de", cc1, 1, false, 1, ct1_de, ct1_en, ct1_fr);

        final CodeTranslation ct2_en = new CodeTranslation(5, "en", "name2", null, 1);
        final CodeTranslation ct2_fr = new CodeTranslation(6, "fr", "nom2", null, 1);
        final CodeTranslation ct2_de = new CodeTranslation(4, "de", "Name2", null, 1);
        final CodeDefinition cd2 = new CodeDefinition("2A", "de", cc2, 2, true, 1, ct2_de, ct2_en, ct2_fr);

        results.addAll(List.of(cd1, cd2));

        when(codeServiceMock.countByFilter(isA(CodeFilter.class))).thenReturn(results.size());
        when(codeServiceMock.findPageOfEntityDefinitions(isA(CodeFilter.class),
            isA(PaginationRequest.class))).thenReturn(results.iterator());
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(codeServiceMock);
    }

    @Override
    protected CodeListPage makePage() {
        return new CodeListPage(null);
    }

    @Override
    protected Class<CodeListPage> getPageClass() {
        return CodeListPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        assertFilterForm("filterPanel:filterForm");

        final String[] headers = { "Code", "Translations", "Sort", "Scope" };
        final String[] row1 = { "1A", "DE: 'Name1'; EN: 'name1'; FR: 'nom1'".replace("'", "&#039;"), "1", "Public" };
        final String[] row2 = { "2A", "DE: 'Name2'; EN: 'name2'; FR: 'nom2'".replace("'", "&#039;"), "2", "Internal" };
        assertResultTable("resultPanel:results", headers, row1, row2);

        verify(codeServiceMock).getCodeClass1("en_us");
        verify(codeServiceMock).countByFilter(isA(CodeFilter.class));
        verify(codeServiceMock).findPageOfEntityDefinitions(isA(CodeFilter.class), isA(PaginationRequest.class));
    }

    private void assertFilterForm(final String b) {
        getTester().assertComponent(b, Form.class);
        getTester().assertLabel(b + ":codeClassLabel", "Code Class");
        getTester().assertComponent(b + ":codeClass", BootstrapSelect.class);
        assertLabeledTextField(b, "name");
        assertLabeledTextField(b, "comment");
        getTester().assertComponent(b + ":newCode", BootstrapAjaxButton.class);
    }

    private void assertResultTable(final String b, final String[] labels, final String[]... rows) {
        getTester().assertComponent(b, BootstrapDefaultDataTable.class);
        assertHeaderColumns(b, labels);
        assertTableValuesOfRows(b, 1, COLUMN_ID_WITH_LINK, rows);
    }

    private void assertHeaderColumns(final String b, final String[] labels) {
        int idx = 0;
        for (final String label : labels)
            getTester().assertLabel(
                b + ":topToolbars:toolbars:2:headers:" + ++idx + ":header:orderByLink:header_body:label", label);
    }

    private void assertTableValuesOfRows(final String b, final int rowStartIdx, final Integer colIdxAsLink,
        final String[]... rows) {
        if (colIdxAsLink != null)
            getTester().assertComponent(b + ":body:rows:" + rowStartIdx + ":cells:" + colIdxAsLink + ":cell:link",
                Link.class);
        int rowIdx = rowStartIdx;
        for (final String[] row : rows) {
            int colIdx = 1;
            for (final String value : row)
                getTester().assertLabel(b + ":body:rows:" + rowIdx + ":cells:" + colIdx + ":cell" + (
                    colIdxAsLink != null && colIdx++ == colIdxAsLink ? ":link:label" : ""), value);
            rowIdx++;
        }
    }

    @Test
    public void clickingOnCodeTitle_forwardsToCodeEditPage_withModelLoaded() {
        getTester().startPage(getPageClass());

        getTester().clickLink("resultPanel:results:body:rows:1:cells:" + COLUMN_ID_WITH_LINK + ":cell:link");
        getTester().assertRenderedPage(CodeEditPage.class);

        // verify the codes were loaded into the target page
        getTester().assertModelValue("form:translationsPanel:translations:1:name", "Name1");
        getTester().assertModelValue("form:translationsPanel:translations:2:name", "name1");
        getTester().assertModelValue("form:translationsPanel:translations:3:name", "nom1");

        verify(codeServiceMock).getCodeClass1("en_us");
        verify(codeServiceMock).countByFilter(isA(CodeFilter.class));
        verify(codeServiceMock).findPageOfEntityDefinitions(isA(CodeFilter.class), isA(PaginationRequest.class));
    }

    @Test
    public void clickingNewCode_forwardsToCodeEditPage() {
        CodeTranslation ct_en = new CodeTranslation(1, "en", "ct_en", null, 1);
        CodeDefinition kd = new CodeDefinition("1A", "en", cc1, 1, false, 1, ct_en);
        when(codeServiceMock.newUnpersistedCodeDefinition()).thenReturn(kd);

        getTester().startPage(getPageClass());
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("filterPanel:filterForm");
        formTester.submit("newCode");

        getTester().assertRenderedPage(CodeEditPage.class);

        verify(codeServiceMock).getCodeClass1("en_us");
        verify(codeServiceMock).countByFilter(isA(CodeFilter.class));
        verify(codeServiceMock).findPageOfEntityDefinitions(isA(CodeFilter.class), isA(PaginationRequest.class));
        verify(codeServiceMock).newUnpersistedCodeDefinition();
    }

    @Test
    public void changingCodeClass_refreshesResultPanel() {
        getTester().startPage(getPageClass());

        getTester().executeAjaxEvent("filterPanel:filterForm:codeClass", "change");
        getTester().assertComponentOnAjaxResponse("resultPanel");

        verify(codeServiceMock).getCodeClass1("en_us");
        verify(codeServiceMock, times(2)).countByFilter(isA(CodeFilter.class));
        verify(codeServiceMock, times(2)).findPageOfEntityDefinitions(isA(CodeFilter.class),
            isA(PaginationRequest.class));
    }

}