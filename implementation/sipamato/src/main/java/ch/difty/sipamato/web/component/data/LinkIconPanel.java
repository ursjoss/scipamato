/*
 * Copyright 2015 Viliam Repan (lazyman)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.difty.sipamato.web.component.data;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author Viliam Repan (lazyman)
 */
public abstract class LinkIconPanel extends Panel {

    private static final long serialVersionUID = 1L;

    private static final String ID_LINK = "link";
    private static final String ID_IMAGE = "image";

    private final IModel<String> titleModel;

    public LinkIconPanel(String id, IModel<String> model) {
        this(id, model, null);
    }

    public LinkIconPanel(String id, IModel<String> model, IModel<String> titleModel) {
        super(id, model);
        this.titleModel = titleModel;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(makeLink());
    }

    private AjaxLink<Void> makeLink() {
        AjaxLink<Void> link = new AjaxLink<Void>(ID_LINK) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                onClickPerformed(target);
            }
        };
        link.add(makeImage(ID_IMAGE));
        link.setOutputMarkupId(true);
        return link;
    }

    private Label makeImage(String id) {
        Label image = new Label(id);
        image.add(AttributeModifier.replace("class", getDefaultModel()));
        if (titleModel != null) {
            image.add(AttributeModifier.replace("title", titleModel));
        }
        return image;
    }

    @SuppressWarnings("unchecked")
    protected AjaxLink<Void> getLink() {
        return (AjaxLink<Void>) get(ID_LINK);
    }

    protected abstract void onClickPerformed(AjaxRequestTarget target);
}