package ch.difty.scipamato.common.web.component.table.column

import ch.difty.scipamato.common.web.component.SerializableBiConsumer
import ch.difty.scipamato.common.web.component.SerializableSupplier
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.junit.jupiter.api.Test

internal class ClickablePropertyColumn2Test {

    private val biConsumerMock = mockk<SerializableBiConsumer<IModel<String>, Int>>(relaxed = true)
    private val supplierMock = mockk<SerializableSupplier<Int>>()

    private val displayModel = Model("foo")
    private val property = "prop"
    private val suppliedValue = 5
    private val clickModel = Model.of("bar")

    private lateinit var c: ClickablePropertyColumn2<String, String, Int>

    @Test
    fun testOnClick_withSortProperty() {
        val sort = "sort"

        every { supplierMock.get() } returns suppliedValue

        c = ClickablePropertyColumn2(displayModel, sort, property, biConsumerMock, supplierMock)
        c.onClick(clickModel)

        verify { supplierMock.get() }
        verify { biConsumerMock.accept(clickModel, suppliedValue) }
    }

    @Test
    fun testOnClick_inNewTab() {
        val sort = "sort"

        every { supplierMock.get() } returns suppliedValue

        c = ClickablePropertyColumn2(displayModel, sort, property, biConsumerMock, supplierMock, true)
        c.onClick(clickModel)

        verify { supplierMock.get() }
        verify { biConsumerMock.accept(clickModel, suppliedValue) }
    }

    @Test
    fun testOnClick_withoutSortProperty() {
        every { supplierMock.get() } returns suppliedValue

        c = ClickablePropertyColumn2(displayModel, property, biConsumerMock, supplierMock)
        c.onClick(clickModel)

        verify { supplierMock.get() }
        verify { biConsumerMock.accept(clickModel, suppliedValue) }
    }

    @Test
    fun gettingProperty() {
        c = ClickablePropertyColumn2(displayModel, property, biConsumerMock, supplierMock)
        c.property shouldBeEqualTo property
    }
}
