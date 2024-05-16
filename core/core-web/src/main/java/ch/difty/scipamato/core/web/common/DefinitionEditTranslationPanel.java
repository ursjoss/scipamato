package ch.difty.scipamato.core.web.common;

import java.util.Collection;
import java.util.Iterator;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.markup.repeater.util.ModelIteratorAdapter;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.DefinitionEntity;
import ch.difty.scipamato.common.entity.DefinitionTranslation;

@SuppressWarnings("SameParameterValue")
public abstract class DefinitionEditTranslationPanel<E extends DefinitionEntity<?, T>, T extends DefinitionTranslation> extends BasePanel<E> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    protected DefinitionEditTranslationPanel(@NotNull final String id, @Nullable final IModel<E> model) {
        super(id, model);
    }

    @Override
    protected final void onInitialize() {
        super.onInitialize();
        queue(newRefreshingView("translations"));
    }

    private RefreshingView<T> newRefreshingView(@NotNull final String id) {
        final RefreshingView<T> translations = new RefreshingView<>(id) {
            @java.io.Serial
            private static final long serialVersionUID = 1L;

            @Override
            protected Iterator<IModel<T>> getItemModels() {
                final Collection<T> translations = getModelObject().getTranslations(null);
                return new ModelIteratorAdapter<>(translations) {
                    @Override
                    protected IModel<T> model(final T codeTopicTranslation) {
                        return new CompoundPropertyModel<>(codeTopicTranslation);
                    }
                };
            }

            @Override
            protected void populateItem(@NotNull final Item<T> item) {
                addColumns(item);
            }

        };
        translations.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance());
        return translations;
    }

    protected abstract void addColumns(@NotNull final Item<T> item);
}
