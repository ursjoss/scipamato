package ch.difty.scipamato.publ.entity

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal abstract class PublicDbEntityTest<T : PublicDbEntity> {

    lateinit var entity: T

    @BeforeEach
    fun setUp() {
        entity = newEntity(CREATED_DATE, LASTMOD_DATE, 10)
    }

    protected abstract fun newEntity(created: LocalDateTime, lastModified: LocalDateTime, version: Int): T

    @Test
    fun setGet() {
        assertSpecificGetters()

        entity.created shouldBeEqualTo CREATED_DATE
        entity.lastModified shouldBeEqualTo LASTMOD_DATE
        entity.version shouldBeEqualTo 10
    }

    protected abstract fun assertSpecificGetters()

    @Test
    fun equals() {
        verifyEquals()
    }

    protected abstract fun verifyEquals()

    companion object {
        private val CREATED_DATE = LocalDateTime.parse("2017-01-01T22:15:13.111")
        private val LASTMOD_DATE = LocalDateTime.parse("2017-01-10T22:15:13.111")
    }
}
