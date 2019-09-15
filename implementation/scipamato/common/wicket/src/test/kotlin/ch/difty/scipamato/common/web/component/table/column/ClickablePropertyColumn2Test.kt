package ch.difty.scipamato.common.web.component.table.column

import ch.difty.scipamato.common.web.component.SerializableBiConsumer
import ch.difty.scipamato.common.web.component.SerializableSupplier
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify

internal class ClickablePropertyColumn2Test {

    private val biConsumerMock = mock<SerializableBiConsumer<IModel<String>, Int>>()
    private val supplierMock = mock<SerializableSupplier<Int>>()

    private val displayModel = Model("foo")
    private val property = "prop"
    private val suppliedValue = 5
    private val clickModel = Model.of("bar")

    private lateinit var c: ClickablePropertyColumn2<String, String, Int>

    @Test
    fun testOnClick_withSortProperty() {
        val sort = "sort"

        whenever(supplierMock.get()).thenReturn(suppliedValue)

        c = ClickablePropertyColumn2(displayModel, sort, property, biConsumerMock, supplierMock)
        c.onClick(clickModel)

        verify(supplierMock).get()
        verify<SerializableBiConsumer<IModel<String>, Int>>(biConsumerMock).accept(clickModel, suppliedValue)
    }

    @Test
    fun testOnClick_inNewTab() {
        val sort = "sort"

        whenever(supplierMock.get()).thenReturn(suppliedValue)

        c = ClickablePropertyColumn2(displayModel, sort, property, biConsumerMock, supplierMock, true)
        c.onClick(clickModel)

        verify(supplierMock).get()
        verify<SerializableBiConsumer<IModel<String>, Int>>(biConsumerMock).accept(clickModel, suppliedValue)
    }

    @Test
    fun testOnClick_withoutSortProperty() {
        whenever(supplierMock.get()).thenReturn(suppliedValue)

        c = ClickablePropertyColumn2(displayModel, property, biConsumerMock, supplierMock)
        c.onClick(clickModel)

        verify(supplierMock).get()
        verify<SerializableBiConsumer<IModel<String>, Int>>(biConsumerMock).accept(clickModel, suppliedValue)
    }

    @Test
    fun gettingProperty() {
        c = ClickablePropertyColumn2(displayModel, property, biConsumerMock, supplierMock)
        assertThat(c.property).isEqualTo(property)
    }

}
