package ch.difty.scipamato.web.pages.portal;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.entity.PublicPaper;
import ch.difty.scipamato.web.pages.BasePage;

@MountPath("/details")
public class PublicPaperDetailPage extends BasePage<PublicPaper> {

    private static final long serialVersionUID = 1L;

    public PublicPaperDetailPage(final IModel<PublicPaper> paperModel) {
        super(paperModel);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        queueTopic(newLabel("caption", getModel()));
        queueTopic(null, newField("title", PublicPaper.TITLE));
        queueTopic(newLabel("reference"), newField("authors", PublicPaper.AUTHORS), newField("title2", PublicPaper.TITLE), newField("location", PublicPaper.LOCATION));
        queueTopic(newLabel("goals"), newField("goals", PublicPaper.GOALS));
        queueTopic(newLabel("population"), newField("population", PublicPaper.POPULATION));
        queueTopic(newLabel("methods"), newField("methods", PublicPaper.METHODS));
        queueTopic(newLabel("result"), newField("result", PublicPaper.RESULT));
        queueTopic(newLabel("comment"), newField("comment", PublicPaper.COMMENT));
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
