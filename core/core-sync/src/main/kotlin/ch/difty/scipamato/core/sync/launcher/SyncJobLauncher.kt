package ch.difty.scipamato.core.sync.launcher

/**
 * Launcher for spring-batch data synchronization from scipamato-core to scipamato-public.
 */
fun interface SyncJobLauncher {
    /**
     * Launch the synchronization and return [SyncJobResult]
     */
    fun launch(): SyncJobResult
}
