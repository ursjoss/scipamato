package ch.difty.scipamato.core.web.paper.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.fileUpload.DropZoneFileUpload;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
import org.apache.commons.fileupload.FileItem;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.common.web.component.SerializableSupplier;
import ch.difty.scipamato.core.entity.PaperAttachment;
import ch.difty.scipamato.core.entity.search.SearchCondition;
import ch.difty.scipamato.core.web.paper.PaperAttachmentProvider;
import ch.difty.scipamato.core.web.paper.jasper.summary.PaperSummaryDataSource;
import ch.difty.scipamato.core.web.paper.jasper.summaryshort.PaperSummaryShortDataSource;

/**
 * The {@link SearchablePaperPanel} offers a look-alike panel to the paper entry
 * panel, but for entering search terms. It therefore hides some components that
 * are visible in the edit panel. It does not implement specific behavior and
 * therefore simply implements the abstract methods of the {@link PaperPanel} as
 * no-ops.
 *
 * @author u.joss
 */
public abstract class SearchablePaperPanel extends PaperPanel<SearchCondition> {

    private static final long serialVersionUID = 1L;

    protected SearchablePaperPanel(String id, IModel<SearchCondition> model) {
        super(id, model, Mode.SEARCH);
    }

    @Override
    protected PaperSummaryDataSource getSummaryDataSource() {
        return null;
    }

    @Override
    protected PaperSummaryShortDataSource getSummaryShortDataSource() {
        return null;
    }

    @Override
    protected boolean hasPubMedId() {
        return false;
    }

    @Override
    protected BootstrapButton newNavigationButton(String id, GlyphIconType icon,
        SerializableSupplier<Boolean> isEnabled, SerializableSupplier<Long> idSupplier) {
        final BootstrapButton btn = new BootstrapButton(id, Model.of(""), Buttons.Type.Default);
        btn.setVisible(false);
        return btn;
    }

    @Override
    protected BootstrapButton newExcludeButton(String id) {
        final BootstrapButton exclude = new BootstrapButton(id, new StringResourceModel("button.exclude.label"),
            Buttons.Type.Default);
        exclude.setVisible(false);
        return exclude;
    }

    @Override
    protected DropZoneFileUpload newDropZoneFileUpload() {
        DropZoneFileUpload dropZoneFileUpload = new DropZoneFileUpload("dropzone") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpload(AjaxRequestTarget target, Map<String, List<FileItem>> fileMap) {
                // no-op, as it's not visible anyway
            }
        };
        dropZoneFileUpload.setVisible(false);
        return dropZoneFileUpload;
    }

    @Override
    protected DataTable<PaperAttachment, String> newAttachmentTable(String id) {
        PaperAttachmentProvider provider = new PaperAttachmentProvider(Model.ofList(new ArrayList<>()));
        DataTable<PaperAttachment, String> attachments = new BootstrapDefaultDataTable<>(id, new ArrayList<>(),
            provider, 10);
        attachments.setVisible(false);
        return attachments;
    }

}
