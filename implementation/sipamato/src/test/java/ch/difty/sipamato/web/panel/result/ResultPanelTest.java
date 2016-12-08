package ch.difty.sipamato.web.panel.result;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.apache.wicket.markup.html.panel.Panel;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.entity.CodeClassId;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.PaperSlimFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
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
    @Mock
    private Page<PaperSlim> pageMock;

    private final PaperSlim paperSlim = new PaperSlim(ID, "firstAuthor", 2016, "title");

    private SortablePaperSlimProvider<? extends PaperSlimFilter> provider;

    @Override
    protected void setUpHook() {
        when(paperSlimServiceMock.countBySearchOrder(searchOrderMock)).thenReturn(1);
        when(paperSlimServiceMock.findBySearchOrder(eq(searchOrderMock), isA(Pageable.class))).thenReturn(pageMock);
        when(pageMock.iterator()).thenReturn(Arrays.asList(paperSlim).iterator());
    }

    @Override
    protected ResultPanel makePanel() {
        provider = new SearchOrderBasedSortablePaperSlimProvider(searchOrderMock);
        return new ResultPanel(PANEL_ID, provider);
    }

    @Override
    protected void assertSpecificComponents() {
        String b = PANEL_ID;
        getTester().assertComponent(b, Panel.class);
        String bb = b + ":table";
        getTester().assertComponent(bb, BootstrapDefaultDataTable.class);
        assertTableRow(bb + ":body:rows:1:cells");

        verify(paperSlimServiceMock, times(2)).countBySearchOrder(searchOrderMock);
        verify(paperSlimServiceMock).findBySearchOrder(eq(searchOrderMock), isA(Pageable.class));
        verify(pageMock).iterator();
    }

    private void assertTableRow(String bb) {
        getTester().assertLabel(bb + ":1:cell", String.valueOf(ID));
        getTester().assertLabel(bb + ":2:cell", "firstAuthor");
        getTester().assertLabel(bb + ":3:cell", "2016");
        getTester().assertLabel(bb + ":4:cell:link:label", "title");
    }

    @Test
    public void clickingLink_opensPaperEntryPage() {
        Paper paper = new Paper();
        paper.setId(ID);
        when(paperServiceMock.findById(ID)).thenReturn(Optional.of(paper));

        getTester().startComponentInPage(makePanel());
        getTester().clickLink(PANEL_ID + ":table:body:rows:1:cells:4:cell:link");
        getTester().assertRenderedPage(PaperEntryPage.class);

        verify(paperServiceMock).findById(ID);
        verify(codeClassServiceMock).find(anyString());
        verify(codeServiceMock, times(8)).findCodesOfClass(isA(CodeClassId.class), anyString());
    }

}
