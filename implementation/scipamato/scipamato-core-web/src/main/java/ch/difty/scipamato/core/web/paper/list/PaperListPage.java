package ch.difty.scipamato.core.web.paper.list;

import static ch.difty.scipamato.core.entity.search.PaperFilter.PaperFilterFields.*;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import com.google.common.base.Strings;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCDNCSSReference;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.MaskType;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.PaperSlimFilter;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.persistence.PaperService;
import ch.difty.scipamato.core.persistence.ServiceResult;
import ch.difty.scipamato.core.pubmed.PubmedImporter;
import ch.difty.scipamato.core.web.common.BasePage;
import ch.difty.scipamato.core.web.common.pastemodal.XmlPasteModalPanel;
import ch.difty.scipamato.core.web.paper.AbstractPaperSlimProvider;
import ch.difty.scipamato.core.web.paper.NewsletterChangeEvent;
import ch.difty.scipamato.core.web.paper.PaperSlimByPaperFilterProvider;
import ch.difty.scipamato.core.web.paper.entry.PaperEntryPage;
import ch.difty.scipamato.core.web.paper.result.ResultPanel;

/**
 * Page to list all papers and apply simple filters to limit the results.
 * <p>
 * Offers the option to create new papers and also to process XML strings
 * exported (as file) from the PubMed User Interface. Processing those pubmed
 * articles results in inserting papers that are not yet available in scipamato
 * (based on PMID).
 *
 * @author u.joss
 */
@SuppressWarnings({ "SameParameterValue", "WicketForgeJavaIdInspection" })
@MountPath("/")
@WicketHomePage
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN, Roles.VIEWER })
public class PaperListPage extends BasePage<Void> {

    private static final long serialVersionUID = 1L;

    private static final int RESULT_PAGE_SIZE = 12;

    @SpringBean
    private PubmedImporter pubmedImportService;

    private final Mode                           mode;
    private       PaperFilter                    filter;
    private       PaperSlimByPaperFilterProvider dataProvider;
    private       ModalWindow                    xmlPasteModalWindow;
    private       ResultPanel                    resultPanel;

    public PaperListPage(PageParameters parameters) {
        super(parameters);
        initFilterAndProvider();
        this.mode = evaluateMode();
    }

    private Mode evaluateMode() {
        return hasOneOfRoles(Roles.USER, Roles.ADMIN) ? Mode.EDIT : Mode.VIEW;
    }

    private PaperListPage(PageParameters parameters, ServiceResult result) {
        this(parameters);
        if (result != null)
            translateServiceResultMessagesToLocalizedUserMessages(result, null);
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

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(FontAwesomeCDNCSSReference.instance()));
    }

    @Override
    public void onEvent(final IEvent<?> event) {
        if (event
                .getPayload()
                .getClass() == NewsletterChangeEvent.class) {
            final AjaxRequestTarget target = ((NewsletterChangeEvent) event.getPayload()).getTarget();
            if (target != null)
                target.add(getFeedbackPanel());
            event.dontBroadcastDeeper();
        }
    }

    private void makeAndQueueFilterForm(final String id) {
        queue(new FilterForm<>(id, dataProvider) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                super.onSubmit();
                updateNavigateable();
            }
        });

        queueFieldAndLabel(new TextField<String>("number", PropertyModel.of(filter, NUMBER.getName())));
        queueFieldAndLabel(new TextField<String>("authorsSearch", PropertyModel.of(filter, AUTHOR_MASK.getName())));
        queueFieldAndLabel(new TextField<String>("methodsSearch", PropertyModel.of(filter, METHODS_MASK.getName())));
        queueFieldAndLabel(new TextField<String>("fieldSearch", PropertyModel.of(filter, SEARCH_MASK.getName())));
        queueFieldAndLabel(new TextField<String>("pubYearFrom", PropertyModel.of(filter, PUB_YEAR_FROM.getName())));
        queueFieldAndLabel(new TextField<String>("pubYearUntil", PropertyModel.of(filter, PUB_YEAR_UNTIL.getName())));

        queueNewPaperButton("newPaper");

        queueXmlPasteModalPanelAndLink("xmlPasteModal", "showXmlPasteModalLink");
    }

    /**
     * Have the provider provide a list of all paper ids matching the current
     * filter. Construct a navigateable with this list and set it into the session
     */
    private void updateNavigateable() {
        getPaperIdManager().initialize(dataProvider.findAllPaperIdsByFilter());
    }

    private void makeAndQueueResultPanel(String id) {
        resultPanel = new ResultPanel(id, dataProvider) {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean isOfferingSearchComposition() {
                return false;
            }

            @Override
            protected PaperEntryPage getResponsePage(IModel<PaperSlim> m, String languageCode,
                PaperService paperService, AbstractPaperSlimProvider<? extends PaperSlimFilter> dataProvider) {
                return new PaperEntryPage(Model.of(paperService
                    .findByNumber(m
                        .getObject()
                        .getNumber(), languageCode)
                    .orElse(new Paper())), getPage().getPageReference(), dataProvider.getSearchOrderId(),
                    dataProvider.isShowExcluded());
            }

        };
        resultPanel.setOutputMarkupId(true);
        queue(resultPanel);
    }

    private void queueXmlPasteModalPanelAndLink(String modalId, String linkId) {
        queue(newXmlPasteModalPanel(modalId));
        queue(newXmlPasteModalLink(linkId));
    }

    private ModalWindow newXmlPasteModalPanel(String modalId) {
        xmlPasteModalWindow = new ModalWindow(modalId);
        XmlPasteModalPanel panel = new XmlPasteModalPanel(xmlPasteModalWindow.getContentId());
        xmlPasteModalWindow.setContent(panel);
        xmlPasteModalWindow.setTitle(new StringResourceModel("xmlPasteModal.title", this, null).getString());
        xmlPasteModalWindow.setResizable(true);
        xmlPasteModalWindow.setAutoSize(true);
        xmlPasteModalWindow.setInitialWidth(600);
        xmlPasteModalWindow.setInitialHeight(500);
        xmlPasteModalWindow.setMinimalWidth(600);
        xmlPasteModalWindow.setMinimalHeight(500);
        xmlPasteModalWindow.setMaskType(MaskType.SEMI_TRANSPARENT);
        xmlPasteModalWindow.setCssClassName(ModalWindow.CSS_CLASS_BLUE);
        xmlPasteModalWindow.setWindowClosedCallback(
            target -> onXmlPasteModalPanelClose(panel.getPastedContent(), target));
        return xmlPasteModalWindow;
    }

    private BootstrapAjaxLink<Void> newXmlPasteModalLink(String linkId) {
        BootstrapAjaxLink<Void> link = new BootstrapAjaxLink<>(linkId, Buttons.Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                xmlPasteModalWindow.show(target);
            }
        };
        link.setOutputMarkupPlaceholderTag(true);
        link.setLabel(new StringResourceModel("xmlPasteModalLink.label", this, null));
        link.add(
            new AttributeModifier("title", new StringResourceModel("xmlPasteModalLink.title", this, null).getString()));
        link.setVisible(mode != Mode.VIEW);
        return link;
    }

    /**
     * Converts the XML string to articles and dump the new papers into the db.
     * Present the service result messages. Protected for test
     *
     * @param pubmedContent
     *     the xml content as string
     * @param target
     *     the AjaxRequestTarget
     */
    void onXmlPasteModalPanelClose(final String pubmedContent, final AjaxRequestTarget target) {
        ServiceResult result = null;
        if (!Strings.isNullOrEmpty(pubmedContent)) {
            result = pubmedImportService.persistPubmedArticlesFromXml(pubmedContent);
            translateServiceResultMessagesToLocalizedUserMessages(result, target);
            target.add(resultPanel);
            updateNavigateable();
        }
        setResponsePage(new PaperListPage(getPageParameters(), result));
    }

    /**
     * @param result
     *     the {@link ServiceResult} to be translated
     * @param target
     *     the AjaxRequestTarget, may be null if called from constructor
     */
    private void translateServiceResultMessagesToLocalizedUserMessages(final ServiceResult result,
        final AjaxRequestTarget target) {
        result
            .getErrorMessages()
            .stream()
            .map(msg -> new StringResourceModel("xmlPasteModal.xml.invalid", this, null).getString())
            .forEach(this::error);
        result
            .getWarnMessages()
            .stream()
            .map(msg -> new StringResourceModel("xmlPasteModal.exists", this, null)
                .setParameters(msg)
                .getString())
            .forEach(this::warn);
        result
            .getInfoMessages()
            .stream()
            .map(msg -> new StringResourceModel("xmlPasteModal.saved", this, null)
                .setParameters(msg)
                .getString())
            .forEach(this::info);
        if (target != null)
            target.add(getFeedbackPanel());
    }

    private void queueNewPaperButton(final String id) {
        BootstrapAjaxButton button = queueResponsePageButton(id,
            () -> new PaperEntryPage(getPageParameters(), getPage().getPageReference()));
        button.setType(Buttons.Type.Primary);
        button.setVisible(mode != Mode.VIEW);
        button.add(new LoadingBehavior(new StringResourceModel(id + LOADING_RESOURCE_TAG, this, null)));
    }
}
