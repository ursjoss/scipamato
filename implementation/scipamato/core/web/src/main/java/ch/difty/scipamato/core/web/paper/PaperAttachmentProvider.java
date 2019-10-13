package ch.difty.scipamato.core.web.paper;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.core.entity.PaperAttachment;

/**
 * Builds up the {@link PaperAttachment} provider based on the model passed into
 * the constructor.
 *
 * @author u.joss
 */
public class PaperAttachmentProvider extends SortableDataProvider<PaperAttachment, String> {

    private static final long serialVersionUID = 1L;

    private final IModel<List<PaperAttachment>> attachmentsModel;

    public PaperAttachmentProvider(@NotNull final IModel<List<PaperAttachment>> attachmentsModel) {
        Objects.requireNonNull(attachmentsModel.getObject());
        this.attachmentsModel = attachmentsModel;
    }

    /**
     * Returns paged {@link PaperAttachment} iterator
     *
     * @param offset
     *     skipping records
     * @param size
     *     page size
     * @return iterator
     */
    @NotNull
    @Override
    public Iterator<PaperAttachment> iterator(final long offset, final long size) {
        return attachmentsModel
            .getObject()
            .stream()
            .skip(offset)
            .limit(size)
            .iterator();
    }

    @Override
    public long size() {
        return attachmentsModel
            .getObject()
            .size();
    }

    @NotNull
    @Override
    public IModel<PaperAttachment> model(@NotNull final PaperAttachment attachment) {
        return new Model<>(attachment);
    }
}
