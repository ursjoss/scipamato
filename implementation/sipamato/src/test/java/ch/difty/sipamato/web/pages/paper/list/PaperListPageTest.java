package ch.difty.sipamato.web.pages.paper.list;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.sipamato.persistance.jooq.paper.slim.PaperSlimRepository;
import ch.difty.sipamato.service.CodeClassService;
import ch.difty.sipamato.service.CodeService;
import ch.difty.sipamato.service.PubmedArticleService;
import ch.difty.sipamato.web.pages.BasePageTest;
import ch.difty.sipamato.web.pages.paper.entry.PaperEntryPage;
import ch.difty.sipamato.web.panel.pastemodal.XmlPasteModalPanel;
import ch.difty.sipamato.web.panel.result.ResultPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

public class PaperListPageTest extends BasePageTest<PaperListPage> {

    @MockBean
    private PaperSlimRepository paperSlimRepoMock;

    @MockBean
    private CodeService codeServiceMock;

    @MockBean
    private CodeClassService codeClassServiceMock;

    @MockBean
    private PubmedArticleService pubmedArticleServiceMock;

    @Override
    protected PaperListPage makePage() {
        return new PaperListPage(null);
    }

    @Override
    protected Class<PaperListPage> getPageClass() {
        return PaperListPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        assertSearchForm("searchForm");
        assertPateModal("xmlPasteModal");
        assertResultPanel("resultPanel");
    }

    private void assertSearchForm(String b) {
        getTester().assertComponent(b, Form.class);

        assertLabeledTextField(b, "authorsSearch");
        assertLabeledTextField(b, "methodsSearch");
        assertLabeledTextField(b, "fieldSearch");
        assertLabeledTextField(b, "pubYearFrom");
        assertLabeledTextField(b, "pubYearUntil");

        getTester().assertComponent(b + ":newPaper", BootstrapAjaxButton.class);
        getTester().assertComponent(b + ":showXmlPasteModalLink", BootstrapAjaxLink.class);
    }

    private void assertPateModal(String id) {
        getTester().assertComponent(id, ModalWindow.class);
        getTester().assertInvisible(id + ":content");
    }

    private void assertResultPanel(String b) {
        getTester().assertComponent(b, ResultPanel.class);
        getTester().assertComponent(b + ":table", BootstrapDefaultDataTable.class);
    }

    @Test
    public void clickingNewPaper_forwardsToPaperEntryPage() {
        getTester().startPage(getPageClass());
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("searchForm");
        formTester.submit("newPaper");

        getTester().assertRenderedPage(PaperEntryPage.class);
    }

    @Test
    public void clickingOnShowXmlPastePanelButton_opensModalWindow() {
        getTester().startPage(getPageClass());
        getTester().debugComponentTrees();

        String b = "searchForm:";
        getTester().executeAjaxEvent("searchForm:showXmlPasteModalLink", "click");

        b = "xmlPasteModal";
        getTester().assertComponent(b, ModalWindow.class);
        b += ":content";
        getTester().isVisible(b);
        getTester().assertComponent(b, XmlPasteModalPanel.class);
        b += ":form";
        getTester().assertComponent(b, Form.class);
        getTester().assertComponent(b + ":content", TextArea.class);
        getTester().assertComponent(b + ":submit", BootstrapAjaxButton.class);
    }
}
