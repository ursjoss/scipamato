package ch.difty.scipamato.core.web.common;

import java.util.Collection;
import java.util.Iterator;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.markup.repeater.util.ModelIteratorAdapter;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import ch.difty.scipamato.common.entity.DefinitionEntity;
import ch.difty.scipamato.common.entity.DefinitionTranslation;

@SuppressWarnings("SameParameterValue")
public abstract class DefinitionEditTranslationPanel<E extends DefinitionEntity, T extends DefinitionTranslation>
    extends BasePanel<E> {

    private static final long serialVersionUID = 1L;

    protected DefinitionEditTranslationPanel(final String id, final IModel<E> model) {
        super(id, model);
    }

    @Override
    protected final void onInitialize() {
        super.onInitialize();
        queue(newRefreshingView("translations"));
    }

    private RefreshingView<T> newRefreshingView(final String id) {
        final RefreshingView<T> translations = new RefreshingView<>(id) {
            @Override
            protected Iterator<IModel<T>> getItemModels() {
                final Collection<T> translations = getModelObject()
                    .getTranslations()
                    .values();
                return new ModelIteratorAdapter<>(translations) {
                    @Override
                    protected IModel<T> model(final T codeTopicTranslation) {
                        return new CompoundPropertyModel<>(codeTopicTranslation);
                    }
                };
            }

            @Override
            protected void populateItem(final Item<T> item) {
                addColumns(item);
            }

        };
        translations.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance());
        return translations;
    }

    protected abstract void addColumns(final Item<T> item);
}
