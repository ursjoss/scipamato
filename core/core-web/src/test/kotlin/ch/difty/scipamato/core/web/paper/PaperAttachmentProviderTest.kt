package ch.difty.scipamato.core.web.paper

import ch.difty.scipamato.core.entity.PaperAttachment
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainSame
import org.apache.wicket.model.Model
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PaperAttachmentProviderTest {

    private lateinit var provider: PaperAttachmentProvider

    private val att1Dummy = PaperAttachment().apply { id = 1 }
    private val att2Dummy = PaperAttachment().apply { id = 2 }
    private val att3Dummy = PaperAttachment().apply { id = 3 }
    private val att4Dummy = PaperAttachment().apply { id = 4 }

    private val attachments = listOf(att1Dummy, att2Dummy, att3Dummy, att4Dummy)

    @BeforeEach
    fun setUp() {
        provider = PaperAttachmentProvider(Model.ofList(attachments))
    }

    @Test
    fun providerSize_equals_conditionSize() {
        provider.size() shouldBeEqualTo attachments.size.toLong()
    }

    @Test
    fun iterator_fromStartWithPageSizeLargerThanActualSize_returnsAll() {
        provider.iterator(0, 100).asSequence() shouldContainSame
            sequenceOf(att1Dummy, att2Dummy, att3Dummy, att4Dummy)
    }

    @Test
    fun iterator_fromStartWithLimitingPageSize_returnsPageFullFromStart() {
        provider.iterator(0, 2).asSequence() shouldContainSame sequenceOf(att1Dummy, att2Dummy)
    }

    @Test
    fun iterator_fromIndex1WithLimitingPageSize_returnsPageFullFromIndex() {
        provider.iterator(1, 2).asSequence() shouldContainSame sequenceOf(att2Dummy, att3Dummy)
    }

    @Test
    fun gettingModel() {
        val model = provider.model(att1Dummy)
        model.getObject() shouldBeEqualTo att1Dummy
    }
}
