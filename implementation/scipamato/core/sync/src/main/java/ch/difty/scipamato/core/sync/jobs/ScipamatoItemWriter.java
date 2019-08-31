package ch.difty.scipamato.core.sync.jobs;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.batch.item.ItemWriter;

import ch.difty.scipamato.common.AssertAs;

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

    protected ScipamatoItemWriter(final DSLContext jooqDslContextPublic, final String topic) {
        this.dslContext = AssertAs.INSTANCE.notNull(jooqDslContextPublic, "jooqDslContextPublic");
        this.topic = AssertAs.INSTANCE.notNull(topic, "topic");
    }

    protected DSLContext getDslContext() {
        return dslContext;
    }

    @Override
    public void write(final List<? extends T> items) {
        int changeCount = 0;
        for (final T i : items)
            changeCount += executeUpdate(i);
        log.info("{}-sync: Successfully synced {} {}{}", topic, changeCount, topic, (changeCount == 1 ? "" : "s"));
    }

    protected abstract int executeUpdate(T i);

}
