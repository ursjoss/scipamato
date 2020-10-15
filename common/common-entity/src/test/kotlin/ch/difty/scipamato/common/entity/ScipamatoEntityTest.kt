package ch.difty.scipamato.common.entity

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ScipamatoEntityTest {

    private val e = ScipamatoEntity()

    @BeforeEach
    fun setUp() {
        e.created = CD
        e.lastModified = LM
        e.version = VERSION
    }

    @Test
    fun get() {
        e.created shouldBeEqualTo CD
        e.lastModified shouldBeEqualTo LM
        e.version shouldBeEqualTo VERSION
    }

    @Test
    fun testingToString() {
        e.toString() shouldBeEqualTo "ScipamatoEntity[created=$CD,lastModified=$LM,version=$VERSION]"
    }

    @Test
    fun equalsScipamatoEntity() {
        EqualsVerifier.simple()
            .forClass(ScipamatoEntity::class.java)
            .usingGetClass()
            .withIgnoredFields(CREATED.fieldName, MODIFIED.fieldName)
            .verify()
    }

    companion object {
        private const val VERSION = 10

        private val CD = LocalDateTime.now().minusDays(1)
        private val LM = LocalDateTime.now().plusDays(1)
    }
}
