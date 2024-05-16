package ch.difty.scipamato.common.web.component.table.column

import ch.difty.scipamato.common.web.component.SerializableBiConsumer
import ch.difty.scipamato.common.web.component.SerializableSupplier
import org.apache.wicket.model.IModel

/**
 * ClickablePropertyColumn, a lambdified version of the one provided in
 * `Apache Wicket Cookbook` - thanks to Igor Vaynberg.
 *
 * Adjusted to accept a BiConsumer instead of a Consumer, thus accepting two
 * arguments in the constructor of the referring page. In addition, accepting a
 * supplier to provide the additional argument for the bi-consumer.
 *
 * @param [T] the type of the object that will be rendered in this column's cells
 * @param [S] the type of the sort property. It will be passed into the constructor of this class and will be final.
 * @param [U] the type of an additional argument passed as supplier to the (bi)consumer
 */
class ClickablePropertyColumn2<T, S, U> @JvmOverloads constructor(
    displayModel: IModel<String?>,
    property: String,
    private val action: SerializableBiConsumer<IModel<T>, U>,
    val parameterSupplier: SerializableSupplier<U>,
    sort: S? = null,
    inNewTab: Boolean = false,
) : AbstractClickablePropertyColumn<T, S>(displayModel, sort, property, inNewTab) {

    override fun onClick(clicked: IModel<T>?) {
        clicked?.let { action.accept(it, parameterSupplier.get()) }
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
