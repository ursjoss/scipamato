package ch.difty.scipamato.core.sync.launcher

/**
 * Launcher for spring-batch data synchronization from scipamato-core to scipamato-public.
 */
fun interface SyncJobLauncher {

    /** Launch the synchronization */
    fun launch(
        setSuccess: (String) -> Unit,
        setFailure: (String) -> Unit,
        setWarning: (String) -> Unit,
        setDone: () -> Unit,
    )
}
