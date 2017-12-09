package ch.difty.scipamato.core.sync.launcher;

/**
 * Launcher for spring-batch data synchronization from scipamato-core to scipamato-public.
 *
 * @author u.joss
 */
@FunctionalInterface
public interface SyncJobLauncher {

    /**
     * Launche the synchronization
     * @return {@link SyncJobResult}
     */
    public SyncJobResult launch();
}
