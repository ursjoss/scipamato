package ch.difty.scipamato.web.pages.portal;

import java.util.Optional;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.ScipamatoPublicSession;
import ch.difty.scipamato.entity.PublicPaper;
import ch.difty.scipamato.navigator.ItemNavigator;
import ch.difty.scipamato.persistence.PublicPaperService;
import ch.difty.scipamato.web.component.SerializableSupplier;
import ch.difty.scipamato.web.pages.BasePage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;

@MountPath("/details")
public class PublicPaperDetailPage extends BasePage<PublicPaper> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private PublicPaperService publicPaperService;

    private final PageReference callingPageRef;

    public PublicPaperDetailPage(final IModel<PublicPaper> paperModel, final PageReference callingPageRef) {
        super(paperModel);
        this.callingPageRef = callingPageRef;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        queue(new Form<Void>("form"));

        final ItemNavigator<Long> pm = ScipamatoPublicSession.get().getPaperIdManager();
        queue(newNavigationButton("previous", GlyphIconType.stepbackward, pm::hasPrevious, () -> {
            pm.previous();
            return pm.getItemWithFocus();
        }));
        queue(newNavigationButton("next", GlyphIconType.stepforward, pm::hasNext, () -> {
            pm.next();
            return pm.getItemWithFocus();
        }));
        makeAndQueueBackButton("back");

        queueTopic(newLabel("caption", getModel()));
        queueTopic(null, newField("title", PublicPaper.TITLE));
        queueTopic(newLabel("reference"), newField("authors", PublicPaper.AUTHORS), newField("title2", PublicPaper.TITLE), newField("location", PublicPaper.LOCATION));
        queueTopic(newLabel("goals"), newField("goals", PublicPaper.GOALS));
        queueTopic(newLabel("population"), newField("population", PublicPaper.POPULATION));
        queueTopic(newLabel("methods"), newField("methods", PublicPaper.METHODS));
        queueTopic(newLabel("result"), newField("result", PublicPaper.RESULT));
        queueTopic(newLabel("comment"), newField("comment", PublicPaper.COMMENT));
    }

    protected BootstrapButton newNavigationButton(String id, GlyphIconType icon, SerializableSupplier<Boolean> isEnabled, SerializableSupplier<Long> idSupplier) {
        final BootstrapButton btn = new BootstrapButton(id, Model.of(""), Buttons.Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                final Long id = idSupplier.get();
                if (id != null) {
                    final Optional<PublicPaper> p = publicPaperService.findById(id);
                    if (p.isPresent())
                        setResponsePage(new PublicPaperDetailPage(Model.of(p.get()), callingPageRef));
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
        btn.add(new AttributeModifier("title", new StringResourceModel("button." + id + ".title", this, null).getString()));
        btn.setType(Buttons.Type.Primary);
        return btn;
    }

    private void makeAndQueueBackButton(String id) {
        BootstrapButton back = new BootstrapButton(id, new StringResourceModel("button.back.label"), Buttons.Type.Default) {
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
        back.add(new AttributeModifier("title", new StringResourceModel("button.back.title", this, null).getString()));
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
        return new Label(idPart + LABEL_TAG, new StringResourceModel(idPart + LABEL_RESOURCE_TAG, this, parameterModel).getString());
    }

    private Label newLabel(final String idPart) {
        return new Label(idPart + LABEL_TAG, new StringResourceModel(idPart + LABEL_RESOURCE_TAG, this, null).getString() + ":");
    }

    private Label newField(final String id, final String property) {
        return new Label(id, new PropertyModel<PublicPaper>(getModel(), property));
    }

}
