package ch.difty.scipamato.core.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.persistence.OptimisticLockingException.Type;

public class OptimisticLockingExceptionTest {

    private final OptimisticLockingException ole = new OptimisticLockingException("tablefoo", "foo 1", Type.DELETE);

    @Test
    public void validateTableName() {
        assertThat(ole.getTableName()).isEqualTo("tablefoo");
    }

    @Test
    public void validateRecord() {
        assertThat(ole.getRecord()).isEqualTo("foo 1");
    }

    @Test
    public void validateMessage() {
        assertThat(ole.getMessage()).isEqualTo(
            "Record in table 'tablefoo' has been modified prior to the delete attempt. Aborting.... [foo 1]");
    }

    @Test
    public void validateMessage_withUpdateException() {
        OptimisticLockingException ole2 = new OptimisticLockingException("tablebar", "bar 2", Type.UPDATE);
        assertThat(ole2.getMessage()).isEqualTo(
            "Record in table 'tablebar' has been modified prior to the update attempt. Aborting.... [bar 2]");
    }

    @Test
    public void validateMessage_withoutRecordParameter() {
        OptimisticLockingException ole3 = new OptimisticLockingException("tablebar", Type.UPDATE);
        assertThat(ole3.getMessage()).isEqualTo(
            "Record in table 'tablebar' has been modified prior to the update attempt. Aborting....");
    }

}
