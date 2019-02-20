package ch.difty.scipamato.core.web.common.pastemodal;

import java.util.List;
import java.util.Map;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.fileUpload.DropZoneFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;

@SuppressWarnings("SameParameterValue")
public class XmlPasteModalPanel extends Panel {

    private static final long serialVersionUID = 1L;

    private static final String TEXT_XML = "text/xml";
    private static final String KEY_FILE = "file";

    private String content;

    private Form<Object>     form;
    private TextArea<String> contentField;

    public XmlPasteModalPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        queue(newForm("form"));
        queue(newTextArea("content"));
        queue(newDropZoneFileUpload("dropzone"));
        queue(newButton("submit"));
        queue(newCancelButton("cancel"));
    }

    private Form<Object> newForm(String id) {
        form = new Form<>(id, new CompoundPropertyModel<>(this));
        return form;
    }

    private TextArea<String> newTextArea(String id) {
        contentField = new TextArea<>(id);
        contentField.setOutputMarkupId(true);
        return contentField;
    }

    private DropZoneFileUpload newDropZoneFileUpload(String id) {
        DropZoneFileUpload upload = new DropZoneFileUpload(id) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpload(AjaxRequestTarget target, Map<String, List<FileItem>> fileMap) {
                doOnUpdate(target, fileMap);
            }
        };
        upload
            .getConfig()
            .withMaxFileSize(1)
            .withThumbnailHeight(80)
            .withThumbnailWidth(80)
            .withParallelUploads(1)
            .withAutoQueue(true)
            .withAcceptedFiles(TEXT_XML);
        return upload;
    }

    /** package private for test purposes */
    void doOnUpdate(final AjaxRequestTarget target, final Map<String, List<FileItem>> fileMap) {
        if (fileMap != null && fileMap.containsKey(KEY_FILE)) {
            for (final FileItem file : fileMap.get(KEY_FILE)) {
                if (file
                    .getContentType()
                    .equals(TEXT_XML)) {
                    content = file.getString();
                    info(new StringResourceModel("dropzone.upload.successful", this, null)
                        .setParameters(file.getName(), file.getContentType())
                        .getString());
                }
            }
            target.add(contentField);
        }
    }

    private BootstrapAjaxButton newButton(String id) {
        final BootstrapAjaxButton button = new BootstrapAjaxButton(id,
            new StringResourceModel(id + ".label", this, null), form, Buttons.Type.Primary) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onAfterSubmit(AjaxRequestTarget target) {
                super.onAfterSubmit(target);
                ModalWindow.closeCurrent(target);
            }
        };
        button.add(new LoadingBehavior(new StringResourceModel(id + ".loading", this, null)));
        return button;
    }

    private BootstrapAjaxButton newCancelButton(String id) {
        return new BootstrapAjaxButton(id, new StringResourceModel(id + ".label", this, null), form,
            Buttons.Type.Primary) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);
                content = null;
                getFeedbackMessages().clear();
            }

            @Override
            protected void onAfterSubmit(AjaxRequestTarget target) {
                super.onAfterSubmit(target);
                ModalWindow.closeCurrent(target);
            }
        };
    }

    public String getPastedContent() {
        return content;
    }

}