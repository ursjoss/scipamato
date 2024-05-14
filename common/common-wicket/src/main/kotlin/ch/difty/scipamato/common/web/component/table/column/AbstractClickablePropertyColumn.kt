package ch.difty.scipamato.common.web.component.table.column

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.repeater.Item
import org.apache.wicket.model.IModel
import org.apache.wicket.model.PropertyModel

/**
 * Abstract BaseClass for ClickablePropertyColumn implementations
 * `Apache Wicket Cookbook` - thanks to Igor Vaynberg.
 *
 * @param [T] the type of the object that will be rendered in this column's cells
 * @param [S] the type of the sort property
 */
abstract class AbstractClickablePropertyColumn<T, S> internal constructor(
    displayModel: IModel<String?>,
    sort: S?,
    val property: String,
    private val inNewTab: Boolean,
) : AbstractColumn<T, S>(displayModel, sort) {

    override fun populateItem(
        cellItem: Item<ICellPopulator<T>>,
        componentId: String,
        rowModel: IModel<T>?,
    ) {
        cellItem.add(LinkPanel(
            id = componentId,
            rowModel = rowModel,
            labelModel = PropertyModel<Any>(rowModel, property)
        ))
    }

    abstract fun onClick(clicked: IModel<T>?)

    @Suppress("serial")
    private inner class LinkPanel(
        id: String,
        rowModel: IModel<T>?,
        labelModel: IModel<*>?,
    ) : Panel(id) {

        init {
            add(object : Link<T>("link", rowModel) {
                private val serialVersionUID: Long = 1L
                override fun onClick() {
                    onClick(model)
                }

                override fun onComponentTag(tag: ComponentTag) {
                    super.onComponentTag(tag)
                    if (inNewTab) tag.put("target", "_blank")
                }

            }.apply<Link<T>> {
                add(Label("label", labelModel))
            })
        }
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
