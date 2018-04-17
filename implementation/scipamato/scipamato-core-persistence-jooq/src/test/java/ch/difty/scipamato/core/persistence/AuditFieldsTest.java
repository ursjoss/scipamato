package ch.difty.scipamato.core.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.Test;

public class AuditFieldsTest {

    @Test
    public void instantiating_withNullValues_providesNullValues_withoutThrowing() {
        AuditFields af = new AuditFields(null, null, null, null, null);
        assertThat(af.created).isNull();
        assertThat(af.createdBy).isNull();
        assertThat(af.lastModified).isNull();
        assertThat(af.lastModifiedBy).isNull();
        assertThat(af.version).isEqualTo(1);
    }

    @Test
    public void instantiating_withNonNullValues_providesValues() {
        AuditFields af = new AuditFields(Timestamp.valueOf("2017-01-01 10:11:12.123"), 1,
            Timestamp.valueOf("2017-01-02 10:11:12.123"), 2, 3);
        assertThat(af.created).isEqualTo(LocalDateTime.parse("2017-01-01T10:11:12.123"));
        assertThat(af.createdBy).isEqualTo(1);
        assertThat(af.lastModified).isEqualTo(LocalDateTime.parse("2017-01-02T10:11:12.123"));
        assertThat(af.lastModifiedBy).isEqualTo(2);
        assertThat(af.version).isEqualTo(3);
    }
}
