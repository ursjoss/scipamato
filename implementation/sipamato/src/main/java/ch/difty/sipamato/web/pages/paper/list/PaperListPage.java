package ch.difty.sipamato.web.pages.paper.list;

import java.util.List;
import java.util.Optional;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.sipamato.auth.Roles;
import ch.difty.sipamato.persistance.jooq.paper.PaperFilter;
import ch.difty.sipamato.pubmed.entity.PubmedArticleFacade;
import ch.difty.sipamato.service.PubmedArticleService;
import ch.difty.sipamato.web.pages.BasePage;
import ch.difty.sipamato.web.pages.paper.entry.PaperEntryPage;
import ch.difty.sipamato.web.pages.paper.provider.FilterBasedSortablePaperSlimProvider;
import ch.difty.sipamato.web.panel.pastemodal.XmlPasteModalPanel;
import ch.difty.sipamato.web.panel.result.ResultPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;

@MountPath("list")
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
public class PaperListPage extends BasePage<Void> {

    private static final long serialVersionUID = 1L;

    private static final int RESULT_PAGE_SIZE = 12;

    @SpringBean
    private PubmedArticleService pubmedArticleService;

    private PaperFilter filter;
    private FilterBasedSortablePaperSlimProvider dataProvider;

    public PaperListPage(PageParameters parameters) {
        super(parameters);
        initFilterAndProvider();
    }

    private void initFilterAndProvider() {
        filter = new PaperFilter();
        dataProvider = new FilterBasedSortablePaperSlimProvider(filter, RESULT_PAGE_SIZE);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        makeAndQueueFilterForm("searchForm");
        makeAndQueueResultPanel("resultPanel");
    }

    private void makeAndQueueFilterForm(final String id) {
        queue(new FilterForm<PaperFilter>(id, dataProvider));

        queueFieldAndLabel(new TextField<String>("authorsSearch", PropertyModel.of(filter, PaperFilter.AUTHOR_MASK)), Optional.empty());
        queueFieldAndLabel(new TextField<String>("methodsSearch", PropertyModel.of(filter, PaperFilter.METHODS_MASK)), Optional.empty());
        queueFieldAndLabel(new TextField<String>("fieldSearch", PropertyModel.of(filter, PaperFilter.SEARCH_MASK)), Optional.empty());
        queueFieldAndLabel(new TextField<String>("pubYearFrom", PropertyModel.of(filter, PaperFilter.PUB_YEAR_FROM)), Optional.empty());
        queueFieldAndLabel(new TextField<String>("pubYearUntil", PropertyModel.of(filter, PaperFilter.PUB_YEAR_UNTIL)), Optional.empty());

        queueResponsePageButton("newPaper", new PaperEntryPage(getPageParameters()));
        queueXmlPasteModalPanelAndLink("xmlPasteModal", "showXmlPasteModalLink");
    }

    private void makeAndQueueResultPanel(String id) {
        queue(new ResultPanel(id, dataProvider));
    }

    private void queueXmlPasteModalPanelAndLink(String modalId, String linkId) {
        final ModalWindow xmlPasteModal = new ModalWindow(modalId);
        XmlPasteModalPanel xmlPastePanel = new XmlPasteModalPanel(xmlPasteModal.getContentId());
        xmlPasteModal.setContent(xmlPastePanel);
        xmlPasteModal.setTitle(new StringResourceModel("xmlPasteModal.title", this, null).getString());
        xmlPasteModal.setResizable(true);
        xmlPasteModal.setAutoSize(true);
        xmlPasteModal.setCookieName("xmlPasteModal-1");
        xmlPasteModal.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean onCloseButtonClicked(AjaxRequestTarget target) {
                return true;
            }
        });

        xmlPasteModal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClose(AjaxRequestTarget target) {
                onXmlPasteModalPanelClose(xmlPastePanel, target);
            }

        });

        queue(xmlPasteModal);

        BootstrapAjaxLink<Void> showXmlPasteModal = new BootstrapAjaxLink<Void>(linkId, Buttons.Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                xmlPasteModal.show(target);
            }

        };
        showXmlPasteModal.setOutputMarkupPlaceholderTag(true);
        showXmlPasteModal.setLabel(new StringResourceModel("xmlPasteModalLink.label", this, null));
        showXmlPasteModal.add(new AttributeModifier("title", new StringResourceModel("xmlPasteModalLink.title", this, null).getString()));
        queue(showXmlPasteModal);
    }

    private void onXmlPasteModalPanelClose(XmlPasteModalPanel xmlPastePanel, AjaxRequestTarget target) {
        // TODO implement proper behavior
        List<PubmedArticleFacade> articles = pubmedArticleService.extractArticlesFrom(xmlPastePanel.getPastedContent());
        if (articles.isEmpty()) {
            warn("XML could not be parsed...");
        } else {
            info("Extracted " + articles.size() + " articles from PubMed. 1st with pmid " + articles.get(0).getPmId() + " and title " + articles.get(0).getTitle());
        }
        target.add(getFeedbackPanel());
    }

}
