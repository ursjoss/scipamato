package ch.difty.scipamato.core.web.paper.search;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.scipamato.common.web.component.SerializableBiConsumer;
import ch.difty.scipamato.common.web.component.SerializableBiFunction;
import ch.difty.scipamato.common.web.component.SerializableConsumer;
import ch.difty.scipamato.core.entity.search.SearchCondition;
import ch.difty.scipamato.core.web.WicketTest;

public class PaperPageFactoryTest extends WicketTest {

    private final PaperPageFactory factory = new PaperPageFactory();

    private final SearchCondition sc                = new SearchCondition();
    private final Long            searchConditionId = 5L;

    @Mock
    private MarkupContainer container;

    @Test
    public void assertingNewPaperSearchCriteriaPage() {
        SerializableBiFunction<IModel<SearchCondition>, Long, GenericWebPage<SearchCondition>> function = factory
            .newPaperSearchCriteriaPage();

        GenericWebPage<SearchCondition> page = function.apply(Model.of(sc), searchConditionId);
        assertThat(page).isInstanceOf(PaperSearchCriteriaPage.class);
    }

    @Test
    public void settingResponsePageToPaperSearchCriteriaPageConsumer() {
        SerializableBiConsumer<IModel<SearchCondition>, Long> consumer = factory
            .setResponsePageToPaperSearchCriteriaPageConsumer(container);
        consumer.accept(Model.of(sc), searchConditionId);
        // TODO get test running
        // Mockito.verify(container)
        // .setResponsePage(ArgumentMatchers.argThat((Page p) ->
        // "PaperSearchCriteriaPage".equals(p.getClass()
        // .getSimpleName())));
    }

    @Test
    public void settingResponsePageToPaperSearchPageConsumer() {
        PageParameters pp = new PageParameters();
        SerializableConsumer<PageParameters> consumer = factory.setResponsePageToPaperSearchPageConsumer(container);
        consumer.accept(pp);
        // TODO get test running
        // Mockito.verify(container)
        // .setResponsePage(ArgumentMatchers.argThat((Page p) ->
        // "PaperSearchPage".equals(p.getClass()
        // .getSimpleName())));
    }
}
