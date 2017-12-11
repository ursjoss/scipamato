package ch.difty.scipamato.core.web.pages.paper.provider;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.core.entity.PaperAttachment;

/**
 * Builds up the {@link PaperAttachment} provider based on the model passed into the constructor.
 *
 * @author u.joss
 */
public class PaperAttachmentProvider extends SortableDataProvider<PaperAttachment, String> {

    private static final long serialVersionUID = 1L;

    private final IModel<List<PaperAttachment>> attachmentsModel;

    public PaperAttachmentProvider(final IModel<List<PaperAttachment>> attachmentsModel) {
        AssertAs.notNull(attachmentsModel, "attachmentsModel");
        AssertAs.notNull(attachmentsModel.getObject(), "attachments");
        this.attachmentsModel = attachmentsModel;
    }

    /**
     * Returns paged {@link PaperAttachment} iterator
     * @param offset
     *          skipping records
     * @param size
     *          page size
     * @return iterator
     */
    @Override
    public Iterator<PaperAttachment> iterator(final long offset, final long size) {
        return attachmentsModel.getObject().stream().skip(offset).limit(size).iterator();
    }

    /** {@inheritDoc} */
    @Override
    public long size() {
        return attachmentsModel.getObject().size();
    }

    /** {@inheritDoc} */
    @Override
    public IModel<PaperAttachment> model(final PaperAttachment attachment) {
        return new Model<>(attachment);
    }

}
