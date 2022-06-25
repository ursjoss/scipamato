package ch.difty.scipamato.core.web.sync

import ch.difty.scipamato.core.sync.launcher.SyncJobLauncher
import ch.difty.scipamato.core.sync.launcher.SyncJobResult
import ch.difty.scipamato.core.sync.launcher.SyncJobResult.MessageLevel
import ch.difty.scipamato.core.web.common.BasePage
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaBehavior
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.model.StringResourceModel
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.spring.injection.annot.SpringBean

class RefDataSyncPage(parameters: PageParameters) : BasePage<Void>(parameters) {

    @SpringBean
    private lateinit var jobLauncher: SyncJobLauncher

    override fun onInitialize() {
        super.onInitialize()
        queue(BootstrapForm<Void>("synchForm"))
        queue(newButton("synchronize"))
    }

    private fun newButton(id: String): BootstrapAjaxButton {
        val labelModel = StringResourceModel("button.$id.label", this, null)
        return object : LaddaAjaxButton(id, labelModel, Buttons.Type.Primary) {
            override fun onSubmit(target: AjaxRequestTarget) {
                super.onSubmit(target)
                jobLauncher.launch().let {
                    reportJobResult(it)
                    reportLogMessages(it)
                }
                target.add(feedbackPanel)
            }

            private fun reportJobResult(result: SyncJobResult) =
                if (result.isSuccessful)
                    info(StringResourceModel("feedback.msg.success", this, null).string)
                else
                    error(StringResourceModel("feedback.msg.failed", this, null).string)

            private fun reportLogMessages(result: SyncJobResult) {
                for ((message, messageLevel) in result.messages) {
                    when (messageLevel) {
                        MessageLevel.INFO -> info(message)
                        MessageLevel.WARNING -> warn(message)
                        else -> error(message)
                    }
                }
            }
        }.setEffect(LaddaBehavior.Effect.ZOOM_IN)
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
