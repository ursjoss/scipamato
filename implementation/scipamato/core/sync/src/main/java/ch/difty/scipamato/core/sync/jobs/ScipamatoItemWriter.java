package ch.difty.scipamato.core.sync.jobs;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.springframework.batch.item.ItemWriter;

/**
 * Base class for ItemWriter implementations.
 *
 * @param <T>
 *     the type of the entity to be written
 * @author u.joss
 */
@Slf4j
public abstract class ScipamatoItemWriter<T> implements ItemWriter<T> {

    private final DSLContext dslContext;
    private final String     topic;

    protected ScipamatoItemWriter(@NotNull final DSLContext jooqDslContextPublic, @NotNull final String topic) {
        this.dslContext = jooqDslContextPublic;
        this.topic = topic;
    }

    @NotNull
    protected DSLContext getDslContext() {
        return dslContext;
    }

    @Override
    public void write(@NotNull final List<? extends T> items) {
        int changeCount = 0;
        for (final T i : items)
            changeCount += executeUpdate(i);
        log.info("{}-sync: Successfully synced {} {}{}", topic, changeCount, topic, (changeCount == 1 ? "" : "s"));
    }

    protected abstract int executeUpdate(@NotNull T i);
}
