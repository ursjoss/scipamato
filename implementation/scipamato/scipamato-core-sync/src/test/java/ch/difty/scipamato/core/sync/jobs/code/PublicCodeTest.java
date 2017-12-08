package ch.difty.scipamato.core.sync.jobs.code;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import ch.difty.scipamato.core.sync.jobs.PublicEntityTest;

public class PublicCodeTest extends PublicEntityTest {

    @Test
    public void canSetGet() {
        PublicCode pc = PublicCode
            .builder()
            .code("c")
            .langCode("lc")
            .codeClassId(1)
            .name("name")
            .comment("comment")
            .sort(2)
            .version(3)
            .created(CREATED)
            .lastModified(MODIFIED)
            .lastSynched(SYNCHED)
            .build();

        assertThat(pc.getCode()).isEqualTo("c");
        assertThat(pc.getLangCode()).isEqualTo("lc");
        assertThat(pc.getCodeClassId()).isEqualTo(1);
        assertThat(pc.getName()).isEqualTo("name");
        assertThat(pc.getComment()).isEqualTo("comment");
        assertThat(pc.getSort()).isEqualTo(2);
        assertThat(pc.getVersion()).isEqualTo(3);
        assertThat(pc.getCreated()).isEqualTo(CREATED);
        assertThat(pc.getLastModified()).isEqualTo(MODIFIED);
        assertThat(pc.getLastSynched()).isEqualTo(SYNCHED);
    }
}
