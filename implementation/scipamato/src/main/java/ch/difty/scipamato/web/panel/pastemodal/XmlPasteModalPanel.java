package ch.difty.scipamato.web.panel.pastemodal;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;

public class XmlPasteModalPanel extends Panel {
    private static final long serialVersionUID = 1L;

    private String pastedContent;

    private Form<Void> form;

    public XmlPasteModalPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        queue(newForm("form"));
        queue(newTextArea("pastedContent"));
        queue(newButton("submit"));
    }

    private Form<Void> newForm(String id) {
        form = new Form<>(id);
        return form;
    }

    private TextArea<String> newTextArea(String id) {
        return new TextArea<>("content", new PropertyModel<String>(this, id));
    }

    private BootstrapAjaxButton newButton(String id) {
        return new BootstrapAjaxButton(id, new StringResourceModel(id + ".label", this, null), form, Buttons.Type.Primary) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onAfterSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onAfterSubmit(target, form);
                ModalWindow.closeCurrent(target);
            }
        };
    }

    public String getPastedContent() {
        return pastedContent;
    }

}