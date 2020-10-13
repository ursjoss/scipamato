/*
 * Copyright 2015 Viliam Repan (lazyman)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.difty.scipamato.common.web.component.table.column

import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn
import org.apache.wicket.markup.repeater.Item
import org.apache.wicket.model.IModel
import java.io.Serializable

/**
 * @author Viliam Repan (lazyman)
 */
abstract class LinkIconColumn<T : Serializable>(
    displayModel: IModel<String>,
) : AbstractColumn<T, String>(displayModel) {

    override fun populateItem(
        cellItem: Item<ICellPopulator<T>>,
        componentId: String,
        rowModel: IModel<T>,
    ) {
        cellItem.add(object : LinkIconPanel(
            componentId,
            createIconModel(rowModel),
            createTitleModel(rowModel)
        ) {
            override fun onClickPerformed(target: AjaxRequestTarget) {
                this@LinkIconColumn.onClickPerformed(target, rowModel, link)
            }

            override fun onConfigure() {
                super.onConfigure()
                isVisible = shouldBeVisible()
            }
        })
    }

    protected abstract fun createIconModel(rowModel: IModel<T>): IModel<String>
    open fun createTitleModel(rowModel: IModel<T>): IModel<String>? = null
    protected open fun shouldBeVisible(): Boolean = true

    protected abstract fun onClickPerformed(
        target: AjaxRequestTarget,
        rowModel: IModel<T>,
        link: AjaxLink<Void>,
    )

    companion object {
        private const val serialVersionUID = 1L
    }
}
