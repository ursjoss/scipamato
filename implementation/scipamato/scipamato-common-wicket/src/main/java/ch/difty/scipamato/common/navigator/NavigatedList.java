package ch.difty.scipamato.common.navigator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import ch.difty.scipamato.common.NullArgumentException;

/**
 * List based implementation of the {@link NavigatedItems} interface.
 *
 * @author u.joss
 *
 * @param <T>
 */
class NavigatedList<T> implements NavigatedItems<T> {
    private static final long serialVersionUID = 1L;

    private final List<T> items = new ArrayList<>();

    private volatile int index = 0;

    /**
     * Instantiate the {@link NavigatedList} with the provided collection of items.
     *
     * @param items
     *            collection of items, must not be null or empty.
     */
    public NavigatedList(final Collection<T> items) {
        if (items == null)
            throw new NullArgumentException("items");
        if (items.isEmpty())
            throw new IllegalArgumentException("items must not be empty");
        this.items.addAll(items.stream()
            .distinct()
            .filter(Objects::nonNull)
            .collect(Collectors.toList()));
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public List<T> getItems() {
        return new ArrayList<>(items);
    }

    @Override
    public T getItemWithFocus() {
        return items.get(index);
    }

    @Override
    public void setFocusToItem(final T item) {
        if (item == null)
            throw new NullArgumentException("item");
        final int idx = items.indexOf(item);
        if (idx == -1)
            throw new IllegalArgumentException(
                    "Cannot set focus to item that is not part of the managed list (item " + item + ").");
        this.index = idx;
    }

    @Override
    public void next() {
        if (hasNext())
            index++;
    }

    @Override
    public void previous() {
        if (hasPrevious())
            index--;
    }

    @Override
    public boolean hasNext() {
        return index < items.size() - 1;
    }

    @Override
    public boolean hasPrevious() {
        return index > 0;
    }

}