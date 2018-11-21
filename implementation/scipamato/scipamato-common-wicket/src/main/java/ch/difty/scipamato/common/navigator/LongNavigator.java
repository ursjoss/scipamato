package ch.difty.scipamato.common.navigator;

import java.util.ArrayList;
import java.util.List;

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
    public void initialize(final List<? extends Long> items) {
        if (items != null && !items.isEmpty())
            this.items = new NavigatedList<>(items);
        modified = false;
    }

    @Override
    public void setFocusToItem(final Long item) {
        if (items != null && item != null)
            items.setFocusToItem(item);
    }

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
    public void setIdToHeadIfNotPresent(final Long idCandidate) {
        if (idCandidate != null && !items.containsId(idCandidate)) {
            final List<Long> newItems = new ArrayList<>();
            newItems.add(idCandidate);
            newItems.addAll(items.getItems());
            initialize(newItems);
            setFocusToItem(idCandidate);
        }
    }

    @Override
    public void remove(final Long id) {
        if (id == null || !items.containsId(id))
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
