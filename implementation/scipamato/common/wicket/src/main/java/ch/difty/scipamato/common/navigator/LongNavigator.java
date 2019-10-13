package ch.difty.scipamato.common.navigator;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * {@link ItemNavigator} managing long items.
 * <p>
 * {@inheritDoc}
 *
 * @author u.joss
 */
public class LongNavigator implements ItemNavigator<Long> {

    private static final long serialVersionUID = 1L;

    private NavigatedItems<Long> items;
    private boolean              modified = false;

    @Override
    public void initialize(@NotNull final List<? extends Long> items) {
        if (!items.isEmpty())
            this.items = new NavigatedList<>(items);
        modified = false;
    }

    @Override
    public void setFocusToItem(@Nullable final Long item) {
        if (items != null && item != null)
            items.setFocusToItem(item);
    }

    @Nullable
    @Override
    public Long getItemWithFocus() {
        return items != null ? items.getItemWithFocus() : null;
    }

    @Override
    public boolean hasPrevious() {
        return items != null && items.hasPrevious();
    }

    @Override
    public boolean hasNext() {
        return items != null && items.hasNext();
    }

    @Override
    public void previous() {
        if (hasPrevious())
            items.previous();
    }

    @Override
    public void next() {
        if (hasNext())
            items.next();
    }

    @Override
    public void setIdToHeadIfNotPresent(@NotNull final Long idCandidate) {
        if (!items.containsId(idCandidate)) {
            final List<Long> newItems = new ArrayList<>();
            newItems.add(idCandidate);
            newItems.addAll(items.getItems());
            initialize(newItems);
            setFocusToItem(idCandidate);
        }
    }

    @Override
    public void remove(@NotNull final Long id) {
        if (!items.containsId(id))
            return;
        if (id.equals(items.getItemWithFocus()))
            moveFocus();
        final Long focus = items.getItemWithFocus();
        final List<Long> newItems = items.without(id);
        if (newItems.isEmpty())
            items = null;
        else
            initialize(newItems);
        modified = true;
        setFocusToItem(focus);
    }

    private void moveFocus() {
        if (items.hasNext())
            items.next();
        else
            items.previous();
    }

    @Override
    public boolean isModified() {
        return modified;
    }
}
