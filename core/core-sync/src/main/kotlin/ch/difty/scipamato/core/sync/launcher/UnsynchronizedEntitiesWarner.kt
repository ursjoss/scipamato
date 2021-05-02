package ch.difty.scipamato.core.sync.launcher

import ch.difty.scipamato.core.db.tables.Paper
import ch.difty.scipamato.core.db.tables.PaperCode
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Qualifier

class UnsynchronizedEntitiesWarner @JvmOverloads constructor(
    @Qualifier("dslContext") jooqCore: DSLContext,
    val recordProvider: () -> List<Long> = { retrieveRecords(jooqCore) }
) : Warner {

    override fun findUnsynchronizedPapers(): String? {
        val numbers = recordProvider()
        return if (numbers.isEmpty()) null
        else "Papers not synchronized due to missing codes: Number " + numbers.joinToString(", ", postfix = ".")
    }
}

internal fun retrieveRecords(jooqCore: DSLContext): List<Long> =
    jooqCore.select(Paper.PAPER.NUMBER)
        .from(Paper.PAPER)
        .where(Paper.PAPER.ID.notIn(DSL
            .select(PaperCode.PAPER_CODE.PAPER_ID)
            .from(PaperCode.PAPER_CODE)))
        .fetchInto(Long::class.java)
