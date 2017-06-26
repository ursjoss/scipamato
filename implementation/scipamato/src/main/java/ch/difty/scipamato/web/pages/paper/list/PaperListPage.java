package ch.difty.scipamato.web.pages.paper.list;

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

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import ch.difty.scipamato.ScipamatoSession;
import ch.difty.scipamato.auth.Roles;
import ch.difty.scipamato.persistance.jooq.paper.PaperFilter;
import ch.difty.scipamato.service.PubmedImporter;
import ch.difty.scipamato.service.ServiceResult;
import ch.difty.scipamato.web.pages.BasePage;
import ch.difty.scipamato.web.pages.paper.entry.PaperEntryPage;
import ch.difty.scipamato.web.pages.paper.provider.PaperSlimByPaperFilterProvider;
import ch.difty.scipamato.web.panel.pastemodal.XmlPasteModalPanel;
import ch.difty.scipamato.web.panel.result.ResultPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;

/**
 * Page to list all papers and apply simple filters to limit the results.
 *
 * <p>Offers the option to create new papers and also to process XML strings exported (as file) from the PubMed User Interface.
 * Processing those pubmed articles results in inserting papers that are not yet available in scipamato (based on PMID).
 *
 * @author u.joss
 */
@MountPath("/")
@WicketHomePage
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
public class PaperListPage extends BasePage<Void> {

    private static final long serialVersionUID = 1L;

    private static final int RESULT_PAGE_SIZE = 12;

    @SpringBean
    private PubmedImporter pubmedImportService;

    private PaperFilter filter;
    private PaperSlimByPaperFilterProvider dataProvider;

    public PaperListPage(PageParameters parameters) {
        super(parameters);
        initFilterAndProvider();
    }

    private void initFilterAndProvider() {
        filter = new PaperFilter();
        dataProvider = new PaperSlimByPaperFilterProvider(filter, RESULT_PAGE_SIZE);
        updateNavigateable();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        makeAndQueueFilterForm("searchForm");
        makeAndQueueResultPanel("resultPanel");
    }

    private void makeAndQueueFilterForm(final String id) {
        queue(new FilterForm<PaperFilter>(id, dataProvider) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                super.onSubmit();
                updateNavigateable();
            }
        });

        queueFieldAndLabel(new TextField<String>("number", PropertyModel.of(filter, PaperFilter.NUMBER)), Optional.empty());
        queueFieldAndLabel(new TextField<String>("authorsSearch", PropertyModel.of(filter, PaperFilter.AUTHOR_MASK)), Optional.empty());
        queueFieldAndLabel(new TextField<String>("methodsSearch", PropertyModel.of(filter, PaperFilter.METHODS_MASK)), Optional.empty());
        queueFieldAndLabel(new TextField<String>("fieldSearch", PropertyModel.of(filter, PaperFilter.SEARCH_MASK)), Optional.empty());
        queueFieldAndLabel(new TextField<String>("pubYearFrom", PropertyModel.of(filter, PaperFilter.PUB_YEAR_FROM)), Optional.empty());
        queueFieldAndLabel(new TextField<String>("pubYearUntil", PropertyModel.of(filter, PaperFilter.PUB_YEAR_UNTIL)), Optional.empty());

        queueResponsePageButton("newPaper", () -> new PaperEntryPage(getPageParameters(), getPage().getPageReference()));
        queueXmlPasteModalPanelAndLink("xmlPasteModal", "showXmlPasteModalLink");
    }

    /**
     * Have the provider provide a list of all paper ids matching the current filter.
     * Construct a navigateable with this list and set it into the 
     */
    private void updateNavigateable() {
        ScipamatoSession.get().getPaperIdManager().initialize(dataProvider.findAllPaperIdsByFilter());
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
                onXmlPasteModalPanelClose(xmlPastePanel.getPastedContent(), target);
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

    /**
     * Converts the XML string to articles and dump the new papers into the db. Present the service result messages. Protected for test
     *
     * @param pubmedContent the xml content as string
     * @param target
     */
    protected void onXmlPasteModalPanelClose(final String pubmedContent, final AjaxRequestTarget target) {
        final ServiceResult result = pubmedImportService.persistPubmedArticlesFromXml(pubmedContent);
        translateServiceResultMessagesToLocalizedUserMessages(result, target);
    }

    private void translateServiceResultMessagesToLocalizedUserMessages(final ServiceResult result, final AjaxRequestTarget target) {
        result.getErrorMessages().stream().map(msg -> new StringResourceModel("xmlPasteModal.xml.invalid", this, null).getString()).forEach(this::error);
        result.getWarnMessages().stream().map(msg -> new StringResourceModel("xmlPasteModal.exists", this, null).setParameters(msg).getString()).forEach(this::warn);
        result.getInfoMessages().stream().map(msg -> new StringResourceModel("xmlPasteModal.saved", this, null).setParameters(msg).getString()).forEach(this::info);
        target.add(getFeedbackPanel());
    }

}
