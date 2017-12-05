package ch.difty.scipamato.core.sync.jobs.code;

import static org.assertj.core.api.Assertions.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.Test;

public class PublicCodeTest {

    private PublicCode pc;

    @Test
    public void canInstantiate() {
        final Timestamp created = Timestamp.valueOf(LocalDateTime.now().minusDays(2));
        final Timestamp mod = Timestamp.valueOf(LocalDateTime.now().minusDays(1));
        final Timestamp synch = Timestamp.valueOf(LocalDateTime.now());

        pc = new PublicCode("c", "lc", 1, "name", "comment", 2, 3, created, mod, synch);

        assertThat(pc.getCode()).isEqualTo("c");
        assertThat(pc.getLangCode()).isEqualTo("lc");
        assertThat(pc.getCodeClassId()).isEqualTo(1);
        assertThat(pc.getName()).isEqualTo("name");
        assertThat(pc.getComment()).isEqualTo("comment");
        assertThat(pc.getSort()).isEqualTo(2);
        assertThat(pc.getVersion()).isEqualTo(3);
        assertThat(pc.getCreated()).isEqualTo(created);
        assertThat(pc.getLastModified()).isEqualTo(mod);
        assertThat(pc.getLastSynched()).isEqualTo(synch);
    }
}
