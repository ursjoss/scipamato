package ch.difty.scipamato.publ.web.paper.browse;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapExternalLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapExternalLink.Target;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.common.navigator.ItemNavigator;
import ch.difty.scipamato.common.web.component.SerializableSupplier;
import ch.difty.scipamato.publ.entity.PublicPaper;
import ch.difty.scipamato.publ.persistence.api.PublicPaperService;
import ch.difty.scipamato.publ.web.PublicPageParameters;
import ch.difty.scipamato.publ.web.common.BasePage;

@SuppressWarnings("SameParameterValue")
@MountPath("/paper/number/${number}")
public class PublicPaperDetailPage extends BasePage<PublicPaper> {

    private static final long serialVersionUID = 1L;

    private static final String LINK_RESOURCE_PREFIX   = "link.";
    private static final String BUTTON_RESOURCE_PREFIX = "button.";
    private static final String AM_TITLE               = "title";

    @SpringBean
    private PublicPaperService publicPaperService;

    private final PageReference callingPageRef;

    /**
     * Loads the page with the record specified by the 'id' passed in via
     * PageParameters. If the parameter 'no' contains a valid business key number
     * instead, the page will be loaded by number.
     *
     * @param parameters
     *     page parameters
     */
    PublicPaperDetailPage(final PageParameters parameters) {
        this(parameters, null);
    }

    /**
     * Loads the page with the record specified by the 'id' passed in via
     * PageParameters. If the parameter 'no' contains a valid business key number
     * instead, the page will be loaded by number.
     *
     * @param parameters
     *     page parameters
     * @param callingPageRef
     *     PageReference that will be used to forward to if the user clicks
     *     the back button.
     */
    public PublicPaperDetailPage(final PageParameters parameters, final PageReference callingPageRef) {
        super(parameters);
        this.callingPageRef = callingPageRef;

        tryLoadingRecord(parameters);
    }

    PublicPaperDetailPage(final IModel<PublicPaper> paperModel, final PageReference callingPageRef) {
        super(paperModel);
        this.callingPageRef = callingPageRef;
    }

    /**
     * Try loading the record by ID. If not reasonable id is supplied, try by
     * number.
     */
    private void tryLoadingRecord(final PageParameters parameters) {
        final long number = parameters
            .get(PublicPageParameters.NUMBER.getName())
            .toLong(0L);
        if (number > 0) {
            publicPaperService
                .findByNumber(number)
                .ifPresent((p -> setModel(Model.of(p))));
        }
        if (getModelObject() == null) {
            warn("Page parameter " + PublicPageParameters.NUMBER.getName()
                 + " was missing or invalid. No paper loaded.");
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        queue(new Form<Void>("form"));

        final ItemNavigator<Long> pm = getPaperIdManager();
        queue(newNavigationButton("previous", GlyphIconType.stepbackward, pm::hasPrevious, () -> {
            pm.previous();
            return pm.getItemWithFocus();
        }));
        queue(newNavigationButton("next", GlyphIconType.stepforward, pm::hasNext, () -> {
            pm.next();
            return pm.getItemWithFocus();
        }));
        makeAndQueueBackButton("back");

        queuePubmedLink("pubmed");

        queueTopic(newLabel("caption", getModel()));
        queueTopic(null, newField("title", PublicPaper.PublicPaperFields.TITLE.getName()));
        queueTopic(newLabel("reference"), newField("authors", PublicPaper.PublicPaperFields.AUTHORS.getName()),
            newField("title2", PublicPaper.PublicPaperFields.TITLE.getName()),
            newField("location", PublicPaper.PublicPaperFields.LOCATION.getName()));
        queueTopic(newLabel("goals"), newField("goals", PublicPaper.PublicPaperFields.GOALS.getName()));
        queueTopic(newLabel("population"), newField("population", PublicPaper.PublicPaperFields.POPULATION.getName()));
        queueTopic(newLabel("methods"), newField("methods", PublicPaper.PublicPaperFields.METHODS.getName()));
        queueTopic(newLabel("result"), newField("result", PublicPaper.PublicPaperFields.RESULT.getName()));
        queueTopic(newLabel("comment"), newField("comment", PublicPaper.PublicPaperFields.COMMENT.getName()));
    }

    private void queuePubmedLink(final String id) {
        if (getModelObject() != null) {
            final Integer pmId = getModelObject().getPmId();
            final IModel<String> href = Model.of(getProperties().getPubmedBaseUrl() + pmId);
            final BootstrapExternalLink link = new BootstrapExternalLink(id, href, Type.Default) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onConfigure() {
                    super.onConfigure();
                    setVisible(pmId != null);
                }
            };
            link.setTarget(Target.blank);
            link.setLabel(new StringResourceModel(LINK_RESOURCE_PREFIX + id + LABEL_RESOURCE_TAG, this, null));
            link.add(new AttributeModifier(AM_TITLE,
                new StringResourceModel(LINK_RESOURCE_PREFIX + id + TITLE_RESOURCE_TAG, this, null).getString()));
            queue(link);
        } else {
            queue(new BootstrapExternalLink(id, Model.of(""), Type.Default) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onConfigure() {
                    super.onConfigure();
                    setVisible(false);
                }
            });
        }
    }

    private BootstrapButton newNavigationButton(String id, GlyphIconType icon, SerializableSupplier<Boolean> isEnabled,
        SerializableSupplier<Long> idSupplier) {
        final BootstrapButton btn = new BootstrapButton(id, Model.of(""), Buttons.Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                final Long number = idSupplier.get();
                if (number != null) {
                    PageParameters pp = getPageParameters();
                    pp.set(PublicPageParameters.NUMBER.getName(), number);
                    setResponsePage(new PublicPaperDetailPage(pp, callingPageRef));
                }
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setEnabled(isEnabled.get());
            }
        };
        btn.setDefaultFormProcessing(false);
        btn.setIconType(icon);
        btn.add(new AttributeModifier(AM_TITLE,
            new StringResourceModel(BUTTON_RESOURCE_PREFIX + id + TITLE_RESOURCE_TAG, this, null).getString()));
        btn.setType(Buttons.Type.Primary);
        return btn;
    }

    private void makeAndQueueBackButton(final String id) {
        BootstrapButton back = new BootstrapButton(id,
            new StringResourceModel(BUTTON_RESOURCE_PREFIX + id + LABEL_RESOURCE_TAG), Buttons.Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                if (callingPageRef != null)
                    setResponsePage(callingPageRef.getPage());
                else
                    setResponsePage(PublicPage.class);
            }
        };
        back.setDefaultFormProcessing(false);
        back.add(new AttributeModifier(AM_TITLE,
            new StringResourceModel(BUTTON_RESOURCE_PREFIX + id + TITLE_RESOURCE_TAG, this, null).getString()));
        queue(back);
    }

    private void queueTopic(final Label label, final Label... fields) {
        boolean hasValues = fields.length == 0;
        for (final Label f : fields) {
            if (f.getDefaultModelObject() != null) {
                hasValues = true;
            }
        }
        for (final Label f : fields) {
            f.setVisible(hasValues);
            queue(f);
        }
        if (label != null) {
            label.setVisible(hasValues);
            queue(label);
        }
    }

    private Label newLabel(final String idPart, IModel<?> parameterModel) {
        return new Label(idPart + LABEL_TAG,
            new StringResourceModel(idPart + LABEL_RESOURCE_TAG, this, parameterModel).getString());
    }

    private Label newLabel(final String idPart) {
        return new Label(idPart + LABEL_TAG,
            new StringResourceModel(idPart + LABEL_RESOURCE_TAG, this, null).getString() + ":");
    }

    private Label newField(final String id, final String property) {
        return new Label(id, new PropertyModel<PublicPaper>(getModel(), property));
    }

}
