package ch.difty.scipamato.core.web.paper.search

import ch.difty.scipamato.core.entity.search.SearchCondition
import ch.difty.scipamato.core.web.WicketTest
import io.mockk.impl.annotations.MockK
import org.amshove.kluent.shouldBeInstanceOf
import org.apache.wicket.MarkupContainer
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.junit.jupiter.api.Test

private const val SEARCH_COND_ID = 5L

internal class PaperPageFactoryTest : WicketTest() {

    @MockK(relaxed = true)
    private lateinit var container: MarkupContainer

    private val factory = PaperPageFactory()
    private val sc = SearchCondition()

    @Test
    fun assertingNewPaperSearchCriteriaPage() {
        val function = factory.newPaperSearchCriteriaPage()
        val page = function.apply(Model.of(sc), SEARCH_COND_ID)
        page shouldBeInstanceOf PaperSearchCriteriaPage::class
    }

    @Test
    fun settingResponsePageToPaperSearchCriteriaPageConsumer() {
        val consumer =
            factory.setResponsePageToPaperSearchCriteriaPageConsumer(container)
        consumer.accept(Model.of(sc), SEARCH_COND_ID)
        // TODO get test running
        // Mockito.verify(container)
        // .setResponsePage(ArgumentMatchers.argThat((Page p) ->
        // "PaperSearchCriteriaPage".equals(p.getClass()
        // .getSimpleName())));
    }

    @Test
    fun settingResponsePageToPaperSearchPageConsumer() {
        val pp = PageParameters()
        val consumer = factory.setResponsePageToPaperSearchPageConsumer(container)
        consumer.accept(pp)
        // TODO get test running
        // Mockito.verify(container)
        // .setResponsePage(ArgumentMatchers.argThat((Page p) ->
        // "PaperSearchPage".equals(p.getClass()
        // .getSimpleName())));
    }
}
