package ch.difty.scipamato.core.web.paper.search;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.web.component.SerializableBiConsumer;
import ch.difty.scipamato.common.web.component.SerializableBiFunction;
import ch.difty.scipamato.common.web.component.SerializableConsumer;
import ch.difty.scipamato.core.entity.search.SearchCondition;
import ch.difty.scipamato.core.web.paper.PageFactory;

@Component
public class PaperPageFactory implements PageFactory {

    @NotNull
    @Override
    public SerializableBiConsumer<IModel<SearchCondition>, Long> setResponsePageToPaperSearchCriteriaPageConsumer(
        @NotNull final MarkupContainer container) {
        return (model, searchOrderId) -> container.setResponsePage(new PaperSearchCriteriaPage(model, searchOrderId));
    }

    @NotNull
    @Override
    public SerializableConsumer<PageParameters> setResponsePageToPaperSearchPageConsumer(
        @NotNull final MarkupContainer container) {
        return pp -> container.setResponsePage(new PaperSearchPage(pp));
    }

    @NotNull
    @Override
    public SerializableBiFunction<IModel<SearchCondition>, Long, GenericWebPage<SearchCondition>> newPaperSearchCriteriaPage() {
        return PaperSearchCriteriaPage::new;
    }
}
