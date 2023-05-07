package ch.difty.scipamato.core.sync.jobs

import ch.difty.scipamato.common.logger
import org.jooq.DSLContext
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter

private val log = logger()

/**
 * Base class for ItemWriter implementations.
 *
 * [T] the type of the entity to be written
 */
abstract class ScipamatoItemWriter<T>(
    protected val dslContext: DSLContext,
    private val topic: String,
) : ItemWriter<T> {
    override fun write(chunk: Chunk<out T>) {
        var changeCount = 0
        for (i in chunk) changeCount += executeUpdate(i)
        log.info("$topic-sync: Successfully synced $changeCount $topic${if (changeCount == 1) "" else "s"}")
    }

    protected abstract fun executeUpdate(i: T): Int
}
