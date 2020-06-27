package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.core.persistence.OptimisticLockingException.Type
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
internal class OptimisticLockingExceptionTest {

    private val ole = OptimisticLockingException("tablefoo", "foo 1", Type.DELETE)

    @Test
    fun validateTableName() {
        assertThat(ole.tableName).isEqualTo("tablefoo")
    }

    @Test
    fun validateRecord() {
        assertThat(ole.record).isEqualTo("foo 1")
    }

    @Test
    fun validateMessage() {
        assertThat(ole.message).isEqualTo(
            "Record in table 'tablefoo' has been modified prior to the delete attempt. Aborting.... [foo 1]"
        )
    }

    @Test
    fun validateMessage_withUpdateException() {
        val ole2 = OptimisticLockingException("tablebar", "bar 2", Type.UPDATE)
        assertThat(ole2.message).isEqualTo(
            "Record in table 'tablebar' has been modified prior to the update attempt. Aborting.... [bar 2]"
        )
    }

    @Test
    fun validateMessage_withoutRecordParameter() {
        val ole3 = OptimisticLockingException("tablebar", Type.UPDATE)
        assertThat(ole3.message).isEqualTo(
            "Record in table 'tablebar' has been modified prior to the update attempt. Aborting...."
        )
    }
}
