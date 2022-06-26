package ch.difty.scipamato.core.web.sync

import ch.difty.scipamato.core.sync.launcher.SyncJobLauncher
import org.apache.wicket.Application
import org.apache.wicket.Session
import org.apache.wicket.ThreadContext
import org.apache.wicket.injection.Injector
import java.io.Serializable

interface ISyncTask : Serializable {
    fun launchJob(
        setSuccess: (String) -> Unit,
        setFailure: (String) -> Unit,
        setWarning: (String) -> Unit,
        setDone: () -> Unit,
    )
}

class SyncBatchTask(private val jobLauncher: SyncJobLauncher) : ISyncTask {

    init {
        Injector.get().inject(this)
    }

    override fun launchJob(
        setSuccess: (String) -> Unit,
        setFailure: (String) -> Unit,
        setWarning: (String) -> Unit,
        setDone: () -> Unit,
    ) {
        jobLauncher.launch(setSuccess, setFailure, setWarning, setDone)
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}

class TasksRunnable(
    private val task: ISyncTask,
    private val setSuccess: (String) -> Unit,
    private val setFailure: (String) -> Unit,
    private val setWarning: (String) -> Unit,
    private val setDone: () -> Unit,
) : Runnable {
    private val application: Application = Application.get()
    private val session: Session? = if (Session.exists()) Session.get() else null

    override fun run() {
        try {
            ThreadContext.setApplication(application)
            ThreadContext.setSession(session)
            task.launchJob(setSuccess, setFailure, setWarning, setDone)
        } finally {
            ThreadContext.detach()
        }
    }
}
