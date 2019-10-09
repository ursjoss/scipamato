package ch.difty.scipamato.core.entity

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.CREATOR_ID
import ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.MODIFIER_ID
import ch.difty.scipamato.core.entity.PaperAttachment.PaperAttachmentFields.CONTENT
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class PaperAttachmentTest {

    private val pa = PaperAttachment(1, 2L, "pdf1", "content".toByteArray(), "ct", 2048L)

    @Test
    fun test() {
        assertThat(pa.id).isEqualTo(1)
        assertThat(pa.paperId).isEqualTo(2L)
        assertThat(pa.name).isEqualTo("pdf1")
        assertThat(pa.content).isEqualTo("content".toByteArray())
        assertThat(pa.contentType).isEqualTo("ct")
        assertThat(pa.size).isEqualTo(2048L)
        assertThat(pa.sizeKiloBytes).isEqualTo(2L)
    }

    @Test
    fun sizeInKiloBytes_withNullSize_isNull() {
        assertNull(PaperAttachment().sizeKiloBytes)
    }

    @Test
    fun sizeInKiloBytes_isRoundedUp() {
        pa.size = 2047L
        assertThat(pa.sizeKiloBytes).isEqualTo(2L)
        pa.size = 2050L
        assertThat(pa.sizeKiloBytes).isEqualTo(3L)
    }

    @Test
    fun displayValue_isName() {
        assertThat(pa.displayValue).isEqualTo(pa.name)
    }

    @Test
    fun toString_isMinimal() {
        assertThat(pa.toString()).isEqualTo("PaperAttachment[paperId=2,name=pdf1,id=1]")
    }

    @Test
    fun equals() {
        EqualsVerifier
            .forClass(PaperAttachment::class.java)
            .withRedefinedSuperclass()
            .usingGetClass()
            .withIgnoredFields(
                CONTENT.fieldName, CREATED.fieldName, CREATOR_ID.fieldName, MODIFIED.fieldName, MODIFIER_ID.fieldName
            )
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }
}
