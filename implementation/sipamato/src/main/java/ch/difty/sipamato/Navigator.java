package ch.difty.sipamato;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import ch.difty.sipamato.lib.NullArgumentException;

public class Navigator<T> implements Navigateable<T> {
    private static final long serialVersionUID = 1L;

    private final List<T> items = new ArrayList<>();

    private volatile int index = 0;

    /**
     * Instantiate the {@link Navigator} with the provided collection of items.
     * @param items collection of items, must not be null or empty.
     */
    public Navigator(final Collection<T> items) {
        if (items == null)
            throw new NullArgumentException("items");
        if (items.isEmpty())
            throw new IllegalArgumentException("items must not be empty");
        this.items.addAll(items.stream().distinct().filter(i -> i != null).collect(Collectors.toList()));
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public List<T> getItems() {
        return new ArrayList<T>(items);
    }

    @Override
    public T getCurrentItem() {
        return items.get(index);
    }

    @Override
    public void setCurrentItem(final T item) {
        if (item == null)
            throw new NullArgumentException("item");
        final int idx = items.indexOf(item);
        if (idx == -1)
            throw new IllegalArgumentException("item " + item + " is not part of the managed items");
        this.index = idx;
    }

    @Override
    public void advance() {
        if (index < items.size() - 1)
            index++;
    }

    @Override
    public void retreat() {
        if (index > 0)
            index--;
    }

}