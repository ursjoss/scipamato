package ch.difty.scipamato.core.web.codeclass;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.core.entity.code_class.CodeClassDefinition;
import ch.difty.scipamato.core.entity.code_class.CodeClassFilter;
import ch.difty.scipamato.core.entity.code_class.CodeClassTranslation;
import ch.difty.scipamato.core.persistence.CodeClassService;
import ch.difty.scipamato.core.web.common.BasePageTest;

@SuppressWarnings("SameParameterValue")
class CodeClassListPageTest extends BasePageTest<CodeClassListPage> {

    private static final int COLUMN_ID_WITH_LINK = 2;

    private final List<CodeClassDefinition> results = new ArrayList<>();

    @MockBean
    protected CodeClassService codeClassServiceMock;

    @Override
    protected void setUpHook() {
        final CodeClassTranslation cct1_de = new CodeClassTranslation(1, "de", "Name1", "a description", 1);
        final CodeClassTranslation cct1_en = new CodeClassTranslation(2, "en", "name1", null, 1);
        final CodeClassTranslation cct1_fr = new CodeClassTranslation(3, "fr", "nom1", null, 1);
        final CodeClassDefinition ccd1 = new CodeClassDefinition(1, "de", 1, cct1_de, cct1_en, cct1_fr);

        final CodeClassTranslation cct2_en = new CodeClassTranslation(5, "en", "name2", null, 1);
        final CodeClassTranslation cct2_fr = new CodeClassTranslation(6, "fr", "nom2", null, 1);
        final CodeClassTranslation cct2_de = new CodeClassTranslation(4, "de", "Name2", null, 1);
        final CodeClassDefinition ccd2 = new CodeClassDefinition(2, "de", 1, cct2_de, cct2_en, cct2_fr);

        results.addAll(List.of(ccd1, ccd2));

        when(codeClassServiceMock.countByFilter(isA(CodeClassFilter.class))).thenReturn(results.size());
        when(codeClassServiceMock.findPageOfEntityDefinitions(isA(CodeClassFilter.class),
            isA(PaginationRequest.class))).thenReturn(results.iterator());
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(codeClassServiceMock);
    }

    @Override
    protected CodeClassListPage makePage() {
        return new CodeClassListPage(null);
    }

    @Override
    protected Class<CodeClassListPage> getPageClass() {
        return CodeClassListPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        assertFilterForm("filterPanel:filterForm");

        final String[] headers = { "Id", "Translations" };
        final String[] values = { "1", "DE: 'Name1'; EN: 'name1'; FR: 'nom1'".replace("'", "&#039;") };
        assertResultTable("resultPanel:results", headers, values);

        verify(codeClassServiceMock).countByFilter(isA(CodeClassFilter.class));
        verify(codeClassServiceMock).findPageOfEntityDefinitions(isA(CodeClassFilter.class),
            isA(PaginationRequest.class));
    }

    private void assertFilterForm(final String b) {
        getTester().assertComponent(b, Form.class);
        assertLabeledTextField(b, "name");
        assertLabeledTextField(b, "description");
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
    void clickingOnCodeTitle_forwardsToCodeEditPage_withModelLoaded() {
        getTester().startPage(getPageClass());

        getTester().clickLink("resultPanel:results:body:rows:1:cells:" + COLUMN_ID_WITH_LINK + ":cell:link");
        getTester().assertRenderedPage(CodeClassEditPage.class);

        // verify the codes were loaded into the target page
        getTester().assertModelValue("form:translationsPanel:translations:1:name", "Name1");
        getTester().assertModelValue("form:translationsPanel:translations:2:name", "name1");
        getTester().assertModelValue("form:translationsPanel:translations:3:name", "nom1");

        verify(codeClassServiceMock).countByFilter(isA(CodeClassFilter.class));
        verify(codeClassServiceMock).findPageOfEntityDefinitions(isA(CodeClassFilter.class),
            isA(PaginationRequest.class));
    }

}