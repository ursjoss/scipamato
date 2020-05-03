package ch.difty.scipamato.core.web.paper

import ch.difty.scipamato.core.entity.PaperAttachment
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldThrow
import org.apache.wicket.model.Model
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.ArrayList

@ExtendWith(MockKExtension::class)
internal class PaperAttachmentProviderTest {

    private lateinit var provider: PaperAttachmentProvider

    @MockK
    private lateinit var mockAttachment1: PaperAttachment

    @MockK
    private lateinit var mockAttachment2: PaperAttachment

    @MockK
    private lateinit var mockAttachment3: PaperAttachment

    @MockK
    private lateinit var mockAttachment4: PaperAttachment

    private val attachments: MutableList<PaperAttachment> = ArrayList()

    @BeforeEach
    fun setUp() {
        attachments.addAll(listOf(mockAttachment1, mockAttachment2, mockAttachment3, mockAttachment4))
        provider = PaperAttachmentProvider(Model.ofList(attachments))
    }

    @Test
    fun degenerateConstruction_withNullSearchOrderModel1() {
        invoking { PaperAttachmentProvider(Model.ofList(null)) } shouldThrow NullPointerException::class
    }

    @Test
    fun providerSize_equals_conditionSize() {
        provider.size() shouldBeEqualTo attachments.size.toLong()
    }

    @Test
    fun iterator_fromStartWithPageSizeLargerThanActualSize_returnsAll() {
        provider.iterator(0, 100).asSequence() shouldContainSame
            sequenceOf(mockAttachment1, mockAttachment2, mockAttachment3, mockAttachment4)
    }

    @Test
    fun iterator_fromStartWithLimitingPageSize_returnsPageFullFromStart() {
        provider.iterator(0, 2).asSequence() shouldContainSame sequenceOf(mockAttachment1, mockAttachment2)
    }

    @Test
    fun iterator_fromIndex1WithLimitingPageSize_returnsPageFullFromIndex() {
        provider.iterator(1, 2).asSequence() shouldContainSame sequenceOf(mockAttachment2, mockAttachment3)
    }

    @Test
    fun gettingModel() {
        val model = provider.model(mockAttachment1)
        model.getObject() shouldBeEqualTo mockAttachment1
    }
}
