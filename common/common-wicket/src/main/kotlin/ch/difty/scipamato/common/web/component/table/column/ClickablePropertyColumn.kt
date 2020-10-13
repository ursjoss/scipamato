package ch.difty.scipamato.common.web.component.table.column

import ch.difty.scipamato.common.web.component.SerializableConsumer
import org.apache.wicket.model.IModel

/**
 * ClickablePropertyColumn, a lambdified version of the one provided in
 * `Apache Wicket Cookbook` - thanks to Igor Vaynberg.
 *
 * @param [T] the type of the object that will be rendered in this column's cells
 * @param [S] the type of the sort property
 */
open class ClickablePropertyColumn<T, S> @JvmOverloads constructor(
    displayModel: IModel<String?>,
    property: String,
    private val action: SerializableConsumer<IModel<T>>,
    sort: S? = null,
    inNewTab: Boolean = false,
) : AbstractClickablePropertyColumn<T, S>(displayModel, sort, property, inNewTab) {

    override fun onClick(clicked: IModel<T>?) {
        clicked?.let { action.accept(it) }
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
