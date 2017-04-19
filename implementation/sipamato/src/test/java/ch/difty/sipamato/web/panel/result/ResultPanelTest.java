package ch.difty.sipamato.web.panel.result;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.sipamato.entity.CodeClassId;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.PaperSlimFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.paging.PaginationRequest;
import ch.difty.sipamato.service.CodeClassService;
import ch.difty.sipamato.service.CodeService;
import ch.difty.sipamato.service.PaperService;
import ch.difty.sipamato.service.PaperSlimService;
import ch.difty.sipamato.web.pages.paper.entry.PaperEntryPage;
import ch.difty.sipamato.web.pages.paper.provider.SearchOrderBasedSortablePaperSlimProvider;
import ch.difty.sipamato.web.pages.paper.provider.SortablePaperSlimProvider;
import ch.difty.sipamato.web.panel.PanelTest;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

public class ResultPanelTest extends PanelTest<ResultPanel> {

    private static final long ID = 1l;
    private static final int ROWS_PER_PAGE = 12;

    @MockBean
    private PaperSlimService paperSlimServiceMock;
    @MockBean
    private PaperService paperServiceMock;
    @MockBean
    private CodeClassService codeClassServiceMock;
    @MockBean
    private CodeService codeServiceMock;

    @Mock
    private SearchOrder searchOrderMock;

    private final PaperSlim paperSlim = new PaperSlim(ID, "firstAuthor", 2016, "title");

    @Mock
    private Paper paperMock;

    @Override
    protected void setUpHook() {
        when(paperSlimServiceMock.countBySearchOrder(searchOrderMock)).thenReturn(1);
        when(paperSlimServiceMock.findPageBySearchOrder(eq(searchOrderMock), isA(PaginationRequest.class))).thenReturn(Arrays.asList(paperSlim));

        when(paperServiceMock.findPageBySearchOrder(eq(searchOrderMock), isA(PaginationRequest.class))).thenReturn(Arrays.asList(paperMock));
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(paperSlimServiceMock, paperServiceMock, codeClassServiceMock, codeServiceMock, searchOrderMock);
    }

    @Override
    protected ResultPanel makePanel() {
        return new ResultPanel(PANEL_ID, (SortablePaperSlimProvider<? extends PaperSlimFilter>) new SearchOrderBasedSortablePaperSlimProvider(searchOrderMock, ROWS_PER_PAGE));
    }

    @Override
    protected void assertSpecificComponents() {
        String b = PANEL_ID;
        getTester().assertComponent(b, Panel.class);
        String bb = b + ":table";
        getTester().assertComponent(bb, BootstrapDefaultDataTable.class);
        assertTableRow(bb + ":body:rows:1:cells");

        bb = b + ":summaryLink";
        getTester().assertComponent(bb, ResourceLink.class);
        bb = b + ":reviewLink";
        getTester().assertComponent(bb, ResourceLink.class);
        bb = b + ":literatureReviewLink";
        getTester().assertComponent(bb, ResourceLink.class);
        bb = b + ":summaryTableLink";
        getTester().assertComponent(bb, ResourceLink.class);

        verify(paperSlimServiceMock, times(2)).countBySearchOrder(searchOrderMock);
        verify(paperSlimServiceMock).findPageBySearchOrder(eq(searchOrderMock), isA(PaginationRequest.class));
    }

    private void assertTableRow(String bb) {
        getTester().assertLabel(bb + ":1:cell", String.valueOf(ID));
        getTester().assertLabel(bb + ":2:cell", "firstAuthor");
        getTester().assertLabel(bb + ":3:cell", "2016");
        getTester().assertLabel(bb + ":4:cell:link:label", "title");
        getTester().assertComponent(bb + ":5:cell:link", AjaxLink.class);
        getTester().assertLabel(bb + ":5:cell:link:image", "");
    }

    @Test
    public void clickingLink_opensPaperEntryPage() {
        Paper paper = new Paper();
        paper.setId(ID);
        when(paperServiceMock.findById(ID)).thenReturn(Optional.of(paper));

        getTester().startComponentInPage(makePanel());
        getTester().clickLink(PANEL_ID + ":table:body:rows:1:cells:4:cell:link");
        getTester().assertRenderedPage(PaperEntryPage.class);

        verify(paperSlimServiceMock).countBySearchOrder(searchOrderMock);
        verify(paperSlimServiceMock).findPageBySearchOrder(eq(searchOrderMock), isA(PaginationRequest.class));
        verify(paperServiceMock).findById(ID);
        verify(codeClassServiceMock).find(anyString());
        verify(codeServiceMock, times(8)).findCodesOfClass(isA(CodeClassId.class), anyString());
    }

    @Test
    public void clickingDeleteIconLink_() {
        getTester().startComponentInPage(makePanel());
        getTester().clickLink(PANEL_ID + ":table:body:rows:1:cells:5:cell:link");
        getTester().assertInfoMessages("Excluded firstAuthor (2016): title.");
        getTester().assertComponentOnAjaxResponse(PANEL_ID + ":table");
        // TODO how to verify the response was sent with the id to be excluded

        verify(paperSlimServiceMock, times(2)).countBySearchOrder(searchOrderMock);
        verify(paperSlimServiceMock, times(2)).findPageBySearchOrder(eq(searchOrderMock), isA(PaginationRequest.class));
    }

    /**
     * Note, we're partially also testing the PaperSummaryDataSource and even the Provider here in order to make
     * sure the functionality is triggered. Not sure how to verify the action otherwise.
     * 
     * Also, this is not really asserting anything, just verifying the methods have been called. Bit of a workaround
     */
    private void verifyPdfExport() {
        verify(paperSlimServiceMock, times(2)).countBySearchOrder(searchOrderMock);
        verify(paperSlimServiceMock, times(1)).findPageBySearchOrder(eq(searchOrderMock), isA(PaginationRequest.class));
        verify(paperServiceMock).findPageBySearchOrder(eq(searchOrderMock), isA(PaginationRequest.class));
    }

    @Test
    public void clickingSummaryLink_succeeds() {
        getTester().startComponentInPage(makePanel());
        getTester().clickLink(PANEL_ID + ":summaryLink");
        verifyPdfExport();
    }

    @Test
    public void clickingReviewLink_succeeds() {
        getTester().startComponentInPage(makePanel());
        getTester().clickLink(PANEL_ID + ":reviewLink");
        verifyPdfExport();
    }

    @Test
    public void clickingLiteratureReviewLink_succeeds() {
        getTester().startComponentInPage(makePanel());
        getTester().clickLink(PANEL_ID + ":literatureReviewLink");
        verifyPdfExport();
    }

    @Test
    public void clickingSummaryTableLink_succeeds() {
        getTester().startComponentInPage(makePanel());
        getTester().clickLink(PANEL_ID + ":summaryTableLink");
        verifyPdfExport();
    }

    @Test
    public void clickingSummaryTableWithoutResultLink_succeeds() {
        getTester().startComponentInPage(makePanel());
        getTester().clickLink(PANEL_ID + ":summaryTableWithoutResultsLink");
        verifyPdfExport();
    }

}
