package ch.difty.scipamato.core.web.paper.list;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarExternalLink;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.core.config.ApplicationCoreProperties;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy;
import ch.difty.scipamato.core.web.common.BasePageTest;
import ch.difty.scipamato.core.web.paper.entry.PaperEntryPage;
import ch.difty.scipamato.core.web.paper.result.ResultPanel;

@SuppressWarnings("SameParameterValue")
abstract class PaperListPageTest extends BasePageTest<PaperListPage> {

    static final String LC = "en_us";

    @MockBean
    protected ApplicationCoreProperties applicationPropertiesMock;

    @Override
    protected PaperListPage makePage() {
        return new PaperListPage(null);
    }

    @Override
    protected Class<PaperListPage> getPageClass() {
        return PaperListPage.class;
    }

    @Override
    protected void setUpHook() {
        when(applicationPropertiesMock.getBrand()).thenReturn("SciPaMaTo-Core");
        when(applicationPropertiesMock.getAuthorParserStrategy()).thenReturn(AuthorParserStrategy.PUBMED);
        when(newsletterTopicServiceMock.findAll("en")).thenReturn(Collections.emptyList());
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(paperSlimServiceMock, paperServiceMock, codeServiceMock, codeClassServiceMock,
            paperServiceMock, pubmedImporterMock);
    }

    void assertSearchForm(String b) {
        getTester().assertComponent(b, Form.class);

        assertLabeledTextField(b, "number");
        assertLabeledTextField(b, "authorsSearch");
        assertLabeledTextField(b, "methodsSearch");
        assertLabeledTextField(b, "fieldSearch");
        assertLabeledTextField(b, "pubYearFrom");
        assertLabeledTextField(b, "pubYearUntil");

        assertSpecificSearchFormComponents(b);
    }

    protected abstract void assertSpecificSearchFormComponents(String b);

    void assertResultPanel(String b) {
        getTester().assertComponent(b, ResultPanel.class);
        getTester().assertComponent(b + ":table", BootstrapDefaultDataTable.class);
    }

    void assertPageLinkButton(int index, String position, String expectedLabelText) {
        String path = "navbar:container:collapse:nav" + position + "ListEnclosure:nav" + position + "List:" + index
                      + ":component";
        getTester().assertComponent(path, NavbarButton.class);
        getTester().assertLabel(path + ":label", expectedLabelText);
    }

    void assertTopLevelMenu(int index, String position, String expectedLabelText) {
        String path = "navbar:container:collapse:nav" + position + "ListEnclosure:nav" + position + "List:" + index
                      + ":component:btn";
        getTester().assertComponent(path, WebMarkupContainer.class);
        getTester().assertLabel(path + ":label", expectedLabelText);
    }

    void assertNestedMenu(int topLevelIndex, int menuIndex, String position, String expectedLabelText) {
        String path =
            "navbar:container:collapse:nav" + position + "ListEnclosure:nav" + position + "List:" + topLevelIndex
            + ":component:dropdown-menu:buttons:" + menuIndex + ":button";
        getTester().assertComponent(path, MenuBookmarkablePageLink.class);
        getTester().assertLabel(path + ":label", expectedLabelText);
    }

    void assertExternalLink(final String path, final String link) {
        getTester().assertComponent(path, NavbarExternalLink.class);
        getTester().assertModelValue(path, link);
    }

    @Test
    void clickingOnResultTitle_forwardsToPaperEntryPage() {
        final List<PaperSlim> list = new ArrayList<>();
        long number = 10L;
        list.add(new PaperSlim(1L, number, "author", 2018, "title"));
        when(paperSlimServiceMock.countByFilter(isA(PaperFilter.class))).thenReturn(list.size());
        when(paperSlimServiceMock.findPageByFilter(isA(PaperFilter.class), isA(PaginationRequest.class))).thenReturn(
            list);

        getTester().startPage(getPageClass());

        getTester().clickLink("resultPanel:table:body:rows:1:cells:5:cell:link");
        getTester().assertRenderedPage(PaperEntryPage.class);

        verify(paperSlimServiceMock, times(2)).countByFilter(isA(PaperFilter.class));
        verify(paperSlimServiceMock).findPageByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
        verify(paperServiceMock, times(4)).findPageOfIdsByFilter(isA(PaperFilter.class), isA(PaginationRequest.class));
        verify(paperServiceMock).findByNumber(number, LC);
        verify(pubmedImporterMock, never()).persistPubmedArticlesFromXml(anyString());
    }

}