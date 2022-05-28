package ch.difty.scipamato.core.web.paper.common

import ch.difty.scipamato.common.web.Mode
import ch.difty.scipamato.common.web.component.SerializableSupplier
import ch.difty.scipamato.core.entity.PaperAttachment
import ch.difty.scipamato.core.entity.search.SearchCondition
import ch.difty.scipamato.core.web.paper.PaperAttachmentProvider
import ch.difty.scipamato.core.web.paper.jasper.summary.PaperSummaryDataSource
import ch.difty.scipamato.core.web.paper.jasper.summaryshort.PaperSummaryShortDataSource
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType
import de.agilecoders.wicket.extensions.markup.html.bootstrap.fileUpload.DropZoneFileUpload
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable
import org.apache.commons.fileupload.FileItem
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.model.StringResourceModel
import java.util.ArrayList

/**
 * The [SearchablePaperPanel] offers a look-alike panel to the paper entry
 * panel, but for entering search terms. It therefore hides some components that
 * are visible in the edit panel. It does not implement specific behavior and
 * therefore simply implements the abstract methods of the [PaperPanel] as no-ops.
 */
abstract class SearchablePaperPanel protected constructor(
    id: String,
    model: IModel<SearchCondition>?,
) : PaperPanel<SearchCondition>(id, model, Mode.SEARCH) {

    override val summaryDataSource: PaperSummaryDataSource?
        get() = null
    override val summaryShortDataSource: PaperSummaryShortDataSource?
        get() = null

    override fun hasPubMedId(): Boolean = false

    override fun newNavigationButton(
        id: String, icon: IconType,
        isEnabled: SerializableSupplier<Boolean>, idSupplier: SerializableSupplier<Long?>?,
    ): BootstrapButton = BootstrapButton(id, Model.of(""), Buttons.Type.Default).apply {
        isVisible = false
    }

    override fun newExcludeButton(id: String): BootstrapButton =
        BootstrapButton(id, StringResourceModel("button.exclude.label"),
            Buttons.Type.Default).apply {
            isVisible = false
        }

    override fun newDropZoneFileUpload(): DropZoneFileUpload =
        object : DropZoneFileUpload("dropzone") {
            override fun onUpload(target: AjaxRequestTarget, fileMap: Map<String, List<FileItem>>) {
                // no-op, as it's not visible anyway
            }
        }.apply<DropZoneFileUpload> {
            isVisible = false
        }

    override fun newAttachmentTable(id: String): DataTable<PaperAttachment, String> {
        val provider = PaperAttachmentProvider(Model.ofList(ArrayList()))
        return BootstrapDefaultDataTable(id, ArrayList(), provider, 10)
            .apply<BootstrapDefaultDataTable<PaperAttachment, String>> {
                isVisible = false
            }
    }

    override val isAssociatedWithNewsletter: Boolean
        get() = false
    override val isAssociatedWithWipNewsletter: Boolean
        get() = false

    override fun modifyNewsletterAssociation(target: AjaxRequestTarget) {
        // no-op
    }

    override fun isaNewsletterInStatusWip(): Boolean = false

    companion object {
        private const val serialVersionUID = 1L
    }
}
