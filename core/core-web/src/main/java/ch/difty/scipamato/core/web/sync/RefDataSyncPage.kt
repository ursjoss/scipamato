package ch.difty.scipamato.core.web.sync

import ch.difty.scipamato.core.ScipamatoCoreApplication
import ch.difty.scipamato.core.ScipamatoSession
import ch.difty.scipamato.core.auth.Roles
import ch.difty.scipamato.core.sync.launcher.SyncJobLauncher
import ch.difty.scipamato.core.web.common.BasePage
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation
import org.apache.wicket.event.Broadcast
import org.apache.wicket.event.IEvent
import org.apache.wicket.model.StringResourceModel
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.spring.injection.annot.SpringBean
import java.time.Duration

@Suppress("serial")
@AuthorizeInstantiation(Roles.USER, Roles.ADMIN)
class RefDataSyncPage(parameters: PageParameters) : BasePage<Void>(parameters) {

    @SpringBean
    private lateinit var jobLauncher: SyncJobLauncher

    override fun onInitialize() {
        super.onInitialize()
        queue(BootstrapForm<Void>("synchForm"))
        queue(newButton("synchronize"))
        queue(SyncResultListPanel("syncResults"))
    }

    @Suppress("SameParameterValue")
    private fun newButton(id: String): BootstrapAjaxButton {
        val labelModel = StringResourceModel("button.$id.label", this, null)
        return object : BootstrapAjaxButton(id, labelModel, Buttons.Type.Primary) {
            override fun onSubmit(target: AjaxRequestTarget) {
                super.onSubmit(target)
                ScipamatoCoreApplication.getApplication().launchSyncTask(SyncBatchTask(jobLauncher))
                page.send(page, Broadcast.DEPTH, BatchJobLaunchedEvent(target))
                info(StringResourceModel("feedback.msg.started", this, null).string)
                target.add(this)
            }

            override fun onConfigure() {
                super.onConfigure()
                if (ScipamatoSession.get().syncJobResult.isRunning) {
                    isEnabled = false
                    label = StringResourceModel("button.$id-wip.label", this, null)
                } else {
                    isEnabled = true
                    label = StringResourceModel("button.$id.label", this, null)
                }
            }
        }
    }

    @Suppress("MagicNumber")
    override fun onEvent(event: IEvent<*>) {
        (event.payload as? BatchJobLaunchedEvent)?.let { jobLaunchedEvent ->
            this@RefDataSyncPage.add(AjaxSelfUpdatingTimerBehavior(Duration.ofSeconds(5)))
            jobLaunchedEvent.target.add(this@RefDataSyncPage)
        }
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
