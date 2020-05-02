package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.core.entity.keyword.KeywordDefinition
import ch.difty.scipamato.core.entity.keyword.KeywordTranslation
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.junit.jupiter.api.Test
import java.sql.Timestamp
import java.time.LocalDateTime

@Suppress("UsePropertyAccessSyntax")
internal class AuditFieldsTest {

    @Test
    fun instantiating_withNullValues_providesNullValues_withoutThrowing() {
        val af = AuditFields(null, null, null, null, null)
        af.created.shouldBeNull()
        af.createdBy.shouldBeNull()
        af.lastModified.shouldBeNull()
        af.lastModifiedBy.shouldBeNull()
    }

    @Test
    fun instantiating_withNonNullValues_providesValues() {
        val af = AuditFields(Timestamp.valueOf("2017-01-01 10:11:12.123"), 1,
            Timestamp.valueOf("2017-01-02 10:11:12.123"), 2, 3)
        af.created shouldBeEqualTo LocalDateTime.parse("2017-01-01T10:11:12.123")
        af.createdBy shouldBeEqualTo 1
        af.lastModified shouldBeEqualTo LocalDateTime.parse("2017-01-02T10:11:12.123")
        af.lastModifiedBy shouldBeEqualTo 2
        af.version shouldBeEqualTo 3
    }

    @Test
    @Suppress("LocalVariableName", "VariableNaming")
    fun insertingRecord_savesRecordAndRefreshesId() {
        val kt_de = KeywordTranslation(null, "de", "foo_de", 0)
        val kt_en = KeywordTranslation(null, "en", "foo1_en", 0)
        val kt_fr = KeywordTranslation(null, "fr", "foo1_fr", 0)
        val kd = KeywordDefinition(null, "de", 0, kt_de, kt_en, kt_fr)

        kd.id.shouldBeNull()
    }
}
