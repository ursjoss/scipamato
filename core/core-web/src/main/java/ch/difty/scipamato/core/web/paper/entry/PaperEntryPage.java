package ch.difty.scipamato.core.web.paper.entry;

import static ch.difty.scipamato.core.web.CorePageParameters.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;
import ch.difty.scipamato.core.persistence.PaperService;
import ch.difty.scipamato.core.web.common.SelfUpdatingPage;
import ch.difty.scipamato.core.web.paper.NewsletterChangeEvent;

/**
 * Page to add new papers or modify existing papers. The page implements the
 * {@link SelfUpdatingPage}, implying that changes in individual fields will be
 * validated and persisted immediately.
 * <p>
 * There is a number of validations in place (backed by JSR 303 validations on
 * the {@link Paper} entity), that might prevent a save. This is useful if an
 * existing paper is modified in a way that fails validation. The feedback panel
 * on the BasePage immediately indicates the problem that prevents the save.
 * <p>
 * However, if you enter a new paper, you would automatically fail validation on
 * several fields, which is indicated to the user once you have entered the
 * content of the first field. We don't want several validation messages popping
 * up with every field change until you have reached the state where persisting
 * the paper is actually possible.
 * <p>
 * We therefore evaluate if the paper has been persisted already (either
 * previously after loading a saved paper - or once all required fields have
 * been successfully entered in the process of setting up a new paper). The
 * evaluation is based on whether the paper does have an ID (which is assigned
 * by the persistence layer upon saving). If the evaluation indicates the paper
 * has not yet been saved, the feedback messages are restricted to one message
 * at a time. If the paper has been persisted, several feedback messages would
 * be displayed to the user if the come up.
 * <p>
 * The initial solution turned out to have an important draw-back: Together with
 * the optimistic locking model on the database, SciPaMaTo ran into a race condition
 * in some scenarios (access through VPN over public internet - but not when running
 * the application server locally). If somebody changed a field in the EditablePaperPage
 * and then directly clicked the save button without losing the focus of the edited field
 * with another target first, the auto-save process and the manual save were not handled
 * sequentially. One of the two was changing the version in the database when the other
 * kicked in and tried to save again before the first process had been able to reload
 * the modified record into the wicket model. Hence the version number of the second save
 * attempt was outdated and resulted in an optimistic locking exception. As a side effect,
 * the paper was not accessible anymore from the ItemNavigator. In order to avoid these
 * silly issues, I resorted to disabling the save button upon changing a field and only
 * re-enable it after with the change event. This seems to resolve the issue but it's quite
 * a hack. I didn't find a cleaner way to work around this (the clients insist on the
 * auto-save behavior!).
 * <p>
 * In order to fetch fields from PubMed, the default model now contains specific
 * 'n.a.' values for the fields that can't be null. In case PubMed import is
 * enabled, those will be replaced with real values (except for the goals field,
 * which is not available in the PubMed export).
 * <p>
 * In order to be able to jump back to the calling page (search or paper list
 * page), the page accepts the calling page as PageReference and passes it down
 * to the respective panels. Even more, if called from the search page, the page
 * may need to offer a button to exclude a paper from a search, hence the
 * optional searchOrderId.
 *
 * @author u.joss
 */
@SuppressWarnings("ALL")
@MountPath("entry")
@Slf4j
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN, Roles.VIEWER })
public class PaperEntryPage extends SelfUpdatingPage<Paper> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    @SpringBean
    private PaperService service;

    private EditablePaperPanel contentPanel;

    private final Mode           mode;
    private final PageReference  callingPage;
    private final Long           searchOrderId;
    private final boolean        showingExclusions;
    private final Model<Integer> tabIndexModel;

    /**
     * Instantiates the page with the paper passed in as model. Allows the page to
     * jump back to the calling page.
     *
     * @param paperModel
     *     model of the paper that shall be displayed.
     * @param callingPage
     *     page reference to the page that called this page. Can be null.
     */
    @SuppressWarnings({ "SameParameterValue" })
    public PaperEntryPage(IModel<Paper> paperModel, PageReference callingPage) {
        this(paperModel, callingPage, null, false, Model.of(0));
    }

    /**
     * Instantiates the page with the paper passed in as model. Allows the page to
     * jump back to the calling page. Allows to exclude or re-include the paper from
     * the search order where it was found.
     *
     * @param paperModel
     *     model of the paper that shall be displayed.
     * @param callingPage
     *     page reference to the page that called this page. Can be null.
     * @param searchOrderId
     *     the id of the search order that found this paper. Offers a button
     *     to exclude the paper from the search order.
     * @param showingExclusions
     *     if false, the paper is part of the search result and can be
     *     excluded from it. If true, the current paper has already been
     *     excluded from the search order. You can re-include it.
     */
    public PaperEntryPage(@Nullable IModel<Paper> paperModel, @Nullable PageReference callingPage, @Nullable Long searchOrderId,
        boolean showingExclusions, @Nullable Model<Integer> tabIndexModel) {
        super(paperModel);
        this.callingPage = callingPage;
        this.searchOrderId = searchOrderId;
        this.showingExclusions = showingExclusions;
        this.mode = evaluateMode();
        this.tabIndexModel = tabIndexModel;
    }

    private Mode evaluateMode() {
        return hasOneOfRoles(Roles.USER, Roles.ADMIN) ? Mode.EDIT : Mode.VIEW;
    }

    /**
     * Instantiates the page with a new paper with a free number and n.a. values on
     * all non-nullable fields. Allows the page to jump back to the calling page.
     * Allows to exclude/re-include papers from search orders, but only if the page
     * parameters contain the respective information (searchOrderId,
     * showingExclusions)
     *
     * @param parameters
     *     page parameters
     * @param callingPage
     *     page reference to the page that called this page. Can be null.
     */
    public PaperEntryPage(@Nullable PageParameters parameters, @Nullable PageReference callingPage) {
        super(parameters);
        initDefaultModel();
        this.callingPage = callingPage;
        this.searchOrderId = searchOrderIdFromPageParameters();
        this.showingExclusions = showExcludedFromPageParameters();
        this.mode = evaluateMode();
        this.tabIndexModel = tabIndexFromPageParameters();
    }

    /**
     * Default constructor, used from DefaultPageFactory
     *
     * @param parameters
     *     page parameters
     */
    public PaperEntryPage(PageParameters parameters) {
        this(parameters, null);
    }

    private Long searchOrderIdFromPageParameters() {
        final StringValue sv = getPageParameters().get(SEARCH_ORDER_ID.getName());
        return sv.isNull() ? null : sv.toLong();
    }

    private boolean showExcludedFromPageParameters() {
        final StringValue ieString = getPageParameters().get(SHOW_EXCLUDED.getName());
        final Boolean ie = ieString.isNull() ? null : ieString.toBoolean();
        return ie != null && ie;
    }

    private Model<Integer> tabIndexFromPageParameters() {
        final StringValue sv = getPageParameters().get(TAB_INDEX.getName());
        return Model.of(sv.isNull() ? 0 : sv.toInt(0));
    }

    /**
     * Sets the n.a. values so the paper could be saved or filled with PubMed information
     */
    private void initDefaultModel() {
        Paper paper = new Paper();
        paper.setNumber(findEmptyNumber());
        paper.setAuthors(Paper.NA_AUTHORS);
        paper.setTitle(Paper.NA_STRING);
        paper.setLocation(Paper.NA_STRING);
        paper.setPublicationYear(Paper.NA_PUBL_YEAR);
        paper.setGoals(Paper.NA_STRING);
        paper.setResultExposureRange("μg/m3");
        setDefaultModel(Model.of(paper));
    }

    private long findEmptyNumber() {
        return service.findLowestFreeNumberStartingFrom(getApplicationProperties().getMinimumPaperNumberToBeRecycled());
    }

    @Override
    protected void implSpecificOnInitialize() {
        contentPanel = new EditablePaperPanel("contentPanel", getModel(), callingPage, searchOrderId, showingExclusions, mode, tabIndexModel) {
            @java.io.Serial
            private static final long serialVersionUID = 1L;

            @Override
            protected void onFormSubmit() {
                PaperEntryPage.this.doUpdate();
            }

            @NotNull
            @Override
            protected PaperEntryPage getResponsePage(@NotNull Paper p, @Nullable Long searchOrderId, boolean showingExclusions) {
                return new PaperEntryPage(Model.of(p), getCallingPage(), searchOrderId, showingExclusions, tabIndexModel);
            }
        };
        contentPanel.setOutputMarkupId(true);
        queue(contentPanel);

        adaptFeedbackMessageCountBasedOnId();
    }

    private void doUpdate() {
        doUpdate(getModelObject());
    }

    private void doUpdate(Paper paper) {
        try {
            if (mode == Mode.EDIT) {
                final boolean wasNew = getNullSafeId() == 0;
                final Paper persisted = service.saveOrUpdate(paper);
                if (persisted != null) {
                    setModelObject(persisted);
                    resetFeedbackMessages();
                    if (wasNew)
                        getPaperIdManager().setIdToHeadIfNotPresent(getNullSafeId());
                } else {
                    error(new StringResourceModel("save.error.hint", this, null)
                        .setParameters(getNullSafeId(), "")
                        .getString());
                }
            }
        } catch (OptimisticLockingException ole) {
            @SuppressWarnings("SpellCheckingInspection") final String msg = new StringResourceModel("save.optimisticlockexception.hint", this, null)
                .setParameters(ole.getTableName(), getNullSafeId())
                .getString();
            log.error(msg);
            error(msg);
        } catch (Exception ex) {
            String msg = new StringResourceModel("save.error.hint", this, null)
                .setParameters(getNullSafeId(), ex.getMessage())
                .getString();
            log.error(msg);
            error(msg);
        }
    }

    private long getNullSafeId() {
        return (getModelObject() != null && getModelObject().getId() != null) ? getModelObject().getId() : 0L;
    }

    /**
     * If the id is present, the paper has been persisted. Full feedback messages
     * are enabled. If the paper has not yet been persisted, we restrict the
     * feedback messages to only one item at a time, so the user is not flooded with
     * messages while entering a paper that fails several validations anyhow.
     */
    private void adaptFeedbackMessageCountBasedOnId() {
        if (getNullSafeId() == 0)
            tuneDownFeedbackMessages();
        else
            resetFeedbackMessages();
    }

    @NotNull
    @Override
    protected Form<Paper> getForm() {
        return contentPanel.getForm();
    }

    @Override
    public void onEvent(@NotNull final IEvent<?> event) {
        if (event
                .getPayload()
                .getClass() == NewsletterChangeEvent.class) {
            final AjaxRequestTarget target = ((NewsletterChangeEvent) event.getPayload()).getTarget();
            target.add(contentPanel);
            target.add(getFeedbackPanel());
            event.dontBroadcastDeeper();
        }
    }
}
