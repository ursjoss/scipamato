package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.core.persistence.OptimisticLockingException.Type
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
internal class OptimisticLockingExceptionTest {

    private val ole = OptimisticLockingException("tablefoo", "foo 1", Type.DELETE)

    @Test
    fun validateTableName() {
        ole.tableName shouldBeEqualTo "tablefoo"
    }

    @Test
    fun validateRecord() {
        ole.record shouldBeEqualTo "foo 1"
    }

    @Test
    fun validateMessage() {
        ole.message shouldBeEqualTo
            "Record in table 'tablefoo' has been modified prior to the delete attempt. Aborting.... [foo 1]"
    }

    @Test
    fun validateMessage_withUpdateException() {
        val ole2 = OptimisticLockingException("tablebar", "bar 2", Type.UPDATE)
        ole2.message shouldBeEqualTo
            "Record in table 'tablebar' has been modified prior to the update attempt. Aborting.... [bar 2]"
    }

    @Test
    fun validateMessage_withoutRecordParameter() {
        val ole3 = OptimisticLockingException("tablebar", Type.UPDATE)
        ole3.message shouldBeEqualTo
            "Record in table 'tablebar' has been modified prior to the update attempt. Aborting...."
    }
}
