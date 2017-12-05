package ch.difty.scipamato.core.sync.jobs.codeclass;

import static org.assertj.core.api.Assertions.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.Test;

public class PublicCodeClassTest {

    private PublicCodeClass pcc;

    @Test
    public void canInstantiate() {
        final Timestamp created = Timestamp.valueOf(LocalDateTime.now().minusDays(2));
        final Timestamp mod = Timestamp.valueOf(LocalDateTime.now().minusDays(1));
        final Timestamp synch = Timestamp.valueOf(LocalDateTime.now());

        pcc = new PublicCodeClass(1, "lc", "name", "description", 2, created, mod, synch);

        assertThat(pcc.getCodeClassId()).isEqualTo(1);
        assertThat(pcc.getLangCode()).isEqualTo("lc");
        assertThat(pcc.getName()).isEqualTo("name");
        assertThat(pcc.getDescription()).isEqualTo("description");
        assertThat(pcc.getVersion()).isEqualTo(2);
        assertThat(pcc.getCreated()).isEqualTo(created);
        assertThat(pcc.getLastModified()).isEqualTo(mod);
        assertThat(pcc.getLastSynched()).isEqualTo(synch);
    }
}
