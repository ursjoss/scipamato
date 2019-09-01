package ch.difty.scipamato.common.entity

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
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
        assertThat(e.created).isEqualTo(CD)
        assertThat(e.lastModified).isEqualTo(LM)
        assertThat(e.version).isEqualTo(VERSION)
    }

    @Test
    fun testingToString() {
        assertThat(e.toString()).isEqualTo("ScipamatoEntity[created=$CD,lastModified=$LM,version=$VERSION]")
    }

    @Test
    fun equals() {
        EqualsVerifier
                .forClass(ScipamatoEntity::class.java)
                .withIgnoredFields(CREATED.getName(), MODIFIED.getName())
                .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
                .verify()
    }

    companion object {
        private val VERSION = 10

        private val CD = LocalDateTime.now().minusDays(1)
        private val LM = LocalDateTime.now().plusDays(1)
    }

}
