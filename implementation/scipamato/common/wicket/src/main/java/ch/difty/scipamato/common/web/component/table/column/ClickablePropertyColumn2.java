package ch.difty.scipamato.common.web.component.table.column;

import org.apache.wicket.model.IModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.web.component.SerializableBiConsumer;
import ch.difty.scipamato.common.web.component.SerializableSupplier;

/**
 * ClickablePropertyColumn, a lambdified version of the one provided in
 * {@code Apache Wicket Cookbook} - thanks to Igor Vaynberg.
 * <p>
 * Adjusted to accept a BiConsumer instead of a Consumer, thus accepting two
 * arguments in the constructor of the referring page. In addition accepting a
 * supplier to provide the additional argument for the bi-consumer.
 *
 * @param <T>
 *     the type of the object that will be rendered in this column's
 *     cells
 * @param <S>
 *     the type of the sort property. It will be passed into the
 *     constructor of this class and will be final.
 * @param <U>
 *     the type of an additional argument passed as supplier to the
 *     (bi)consumer
 * @author u.joss
 */
@SuppressWarnings("ALL")
public class ClickablePropertyColumn2<T, S, U> extends AbstractClickablePropertyColumn<T, S> {
    private static final long serialVersionUID = 1L;

    private final SerializableBiConsumer<IModel<T>, U> biConsumer;
    private final SerializableSupplier<U>              supplier;

    public ClickablePropertyColumn2(@NotNull final IModel<String> displayModel, @Nullable final String property,
        @NotNull final SerializableBiConsumer<IModel<T>, U> biConsumer,
        @NotNull final SerializableSupplier<U> supplier) {
        this(displayModel, null, property, biConsumer, supplier, false);
    }

    public ClickablePropertyColumn2(@NotNull final IModel<String> displayModel, @Nullable final S sort,
        @NotNull final String property, @NotNull final SerializableBiConsumer<IModel<T>, U> biConsumer,
        @NotNull final SerializableSupplier<U> supplier) {
        this(displayModel, sort, property, biConsumer, supplier, false);
    }

    public ClickablePropertyColumn2(@NotNull final IModel<String> displayModel, @Nullable final S sort,
        @NotNull final String property, @NotNull final SerializableBiConsumer<IModel<T>, U> biConsumer,
        @NotNull final SerializableSupplier<U> supplier, final boolean inNewTab) {
        super(displayModel, sort, property, inNewTab);
        this.biConsumer = biConsumer;
        this.supplier = supplier;
    }

    @Override
    protected void onClick(@NotNull final IModel<T> clicked) {
        biConsumer.accept(clicked, supplier.get());
    }

}
