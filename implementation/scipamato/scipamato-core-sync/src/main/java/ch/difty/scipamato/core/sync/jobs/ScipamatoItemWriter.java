package ch.difty.scipamato.core.sync.jobs;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.batch.item.ItemWriter;

import ch.difty.scipamato.common.AssertAs;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for ItemWriter implementations.
 * @author u.joss
 *
 * @param <T> 
 */
@Slf4j
public abstract class ScipamatoItemWriter<T> implements ItemWriter<T> {

    private final DSLContext dslContext;
    private final String topic;

    public ScipamatoItemWriter(final DSLContext jooqDslContextPublic, final String topic) {
        this.dslContext = AssertAs.notNull(jooqDslContextPublic, "jooqDslContextPublic");
        this.topic = AssertAs.notNull(topic, "topic");
    }

    protected DSLContext getDslContext() {
        return dslContext;
    }

    @Override
    public void write(final List<? extends T> items) throws Exception {
        int changeCount = 0;
        for (final T i : items)
            changeCount += executeUpdate(i);
        log.info("{}-sync: Sucessfully synched {} {}{}", topic, changeCount, topic, (changeCount == 1 ? "" : "s"));
    }

    protected abstract int executeUpdate(T i);

}
