package ch.difty.scipamato.core.sync.launcher;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

public interface Warner {

    /**
     * @return optional of a message indicating papers that cannot be synchronized
     *     because it has no codes assigned.
     */
    @NotNull
    Optional<String> findUnsynchronizedPapers();
}
