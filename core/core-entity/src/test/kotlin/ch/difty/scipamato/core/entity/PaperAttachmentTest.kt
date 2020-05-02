package ch.difty.scipamato.core.entity

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.CREATOR_ID
import ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.MODIFIER_ID
import ch.difty.scipamato.core.entity.PaperAttachment.PaperAttachmentFields.CONTENT
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.junit.jupiter.api.Test

internal class PaperAttachmentTest {

    private val pa = PaperAttachment(1, 2L, "pdf1", "content".toByteArray(), "ct", 2048L)

    @Test
    fun test() {
        pa.id shouldBeEqualTo 1
        pa.paperId shouldBeEqualTo 2L
        pa.name shouldBeEqualTo "pdf1"
        pa.content shouldBeEqualTo "content".toByteArray()
        pa.contentType shouldBeEqualTo "ct"
        pa.size shouldBeEqualTo 2048L
        pa.sizeKiloBytes shouldBeEqualTo 2L
    }

    @Test
    fun sizeInKiloBytes_withNullSize_isNull() {
        PaperAttachment().sizeKiloBytes.shouldBeNull()
    }

    @Test
    fun sizeInKiloBytes_isRoundedUp() {
        pa.size = 2047L
        pa.sizeKiloBytes shouldBeEqualTo 2L
        pa.size = 2050L
        pa.sizeKiloBytes shouldBeEqualTo 3L
    }

    @Test
    fun displayValue_isName() {
        pa.displayValue shouldBeEqualTo pa.name
    }

    @Test
    fun toString_isMinimal() {
        pa.toString() shouldBeEqualTo "PaperAttachment[paperId=2,name=pdf1,id=1]"
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
