package ch.difty.scipamato.publ.entity

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal abstract class PublicDbEntityTest<T : PublicDbEntity> {

    lateinit var entity: T

    @BeforeEach
    fun setUp() {
        entity = newEntity()
    }

    protected abstract fun newEntity(): T

    @Test
    fun setGet() {
        assertSpecificGetters()
    }

    protected abstract fun assertSpecificGetters()

    @Test
    fun equals() {
        verifyEquals()
    }

    protected abstract fun verifyEquals()
}
