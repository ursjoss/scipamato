package ch.difty.scipamato.core.persistence

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.sql.Timestamp
import java.time.LocalDateTime

@Suppress("UsePropertyAccessSyntax")
internal class AuditFieldsTest {

    @Test
    fun instantiating_withNullValues_providesNullValues_withoutThrowing() {
        val af = AuditFields(null, null, null, null, null)
        assertThat(af.created == null).isTrue()
        assertThat(af.createdBy == null).isTrue()
        assertThat(af.lastModified == null).isTrue()
        assertThat(af.lastModifiedBy == null).isTrue()
    }

    @Test
    fun instantiating_withNonNullValues_providesValues() {
        val af = AuditFields(Timestamp.valueOf("2017-01-01 10:11:12.123"), 1,
            Timestamp.valueOf("2017-01-02 10:11:12.123"), 2, 3)
        assertThat(af.created).isEqualTo(LocalDateTime.parse("2017-01-01T10:11:12.123"))
        assertThat(af.createdBy).isEqualTo(1)
        assertThat(af.lastModified).isEqualTo(LocalDateTime.parse("2017-01-02T10:11:12.123"))
        assertThat(af.lastModifiedBy).isEqualTo(2)
        assertThat(af.version).isEqualTo(3)
    }
}
