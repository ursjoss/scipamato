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

import org.apache.wicket.AttributeModifier
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.IModel

private const val ID_LINK = "link"
private const val ID_IMAGE = "image"

/**
 * @author Viliam Repan (lazyman)
 */
abstract class LinkIconPanel protected constructor(
    id: String,
    iconModel: IModel<String>,
    private val titleModel: IModel<String>?,
) : Panel(id, iconModel) {

    override fun onInitialize() {
        super.onInitialize()
        add(makeLink())
    }

    private fun makeLink(): AjaxLink<Void> = object : AjaxLink<Void>(ID_LINK) {
        private val serialVersionUID: Long = 1L
        override fun onClick(target: AjaxRequestTarget) {
            onClickPerformed(target)
        }
    }.apply<AjaxLink<Void>> {
        add(makeImage(ID_IMAGE))
        outputMarkupId = true
    }

    protected abstract fun onClickPerformed(target: AjaxRequestTarget)

    @Suppress("SameParameterValue")
    private fun makeImage(id: String): Label = Label(id, "").apply {
        add(AttributeModifier.replace("class", this@LinkIconPanel.defaultModel))
        titleModel?.let { add(AttributeModifier.replace("title", it)) }
    }

    @Suppress("UNCHECKED_CAST")
    protected val link: AjaxLink<Void>
        get() = get(ID_LINK) as AjaxLink<Void>

    companion object {
        private const val serialVersionUID = 1L
    }
}
