package ch.difty.scipamato.publ.entity

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal abstract class PublicEntityTest<T : PublicEntity> {

    lateinit var entity: T

    protected abstract val toString: String

    @BeforeEach
    fun setUp() {
        entity = newEntity()
        entity.created = CREATED_DATE
        entity.lastModified = LASTMOD_DATE
        entity.version = 10
    }

    protected abstract fun newEntity(): T

    @Test
    fun setGet() {
        assertSpecificGetters()

        assertThat(entity.created).isEqualTo(CREATED_DATE)
        assertThat(entity.lastModified).isEqualTo(LASTMOD_DATE)
        assertThat(entity.version).isEqualTo(10)
    }

    protected abstract fun assertSpecificGetters()

    @Test
    fun testingToString() {
        assertThat(entity.toString()).isEqualTo(toString)
    }

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
