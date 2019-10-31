package ch.difty.scipamato.core.sync.launcher;

import org.jetbrains.annotations.NotNull;

/**
 * Launcher for spring-batch data synchronization from scipamato-core to
 * scipamato-public.
 *
 * @author u.joss
 */
@FunctionalInterface
public interface SyncJobLauncher {

    /**
     * Launch the synchronization
     *
     * @return {@link SyncJobResult}
     */
    @NotNull
    SyncJobResult launch();
}
