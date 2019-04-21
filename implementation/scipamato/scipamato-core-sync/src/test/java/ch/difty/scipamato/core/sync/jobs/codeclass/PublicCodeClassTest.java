package ch.difty.scipamato.core.sync.jobs.codeclass;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.sync.jobs.PublicEntityTest;

public class PublicCodeClassTest extends PublicEntityTest {

    @Test
    public void canSetGet() {
        PublicCodeClass pcc = PublicCodeClass
            .builder()
            .codeClassId(1)
            .langCode("lc")
            .name("name")
            .description("description")
            .version(2)
            .created(CREATED)
            .lastModified(MODIFIED)
            .lastSynched(SYNCHED)
            .build();

        assertThat(pcc.getCodeClassId()).isEqualTo(1);
        assertThat(pcc.getLangCode()).isEqualTo("lc");
        assertThat(pcc.getName()).isEqualTo("name");
        assertThat(pcc.getDescription()).isEqualTo("description");
        assertThat(pcc.getVersion()).isEqualTo(2);
        assertThat(pcc.getCreated()).isEqualTo(CREATED);
        assertThat(pcc.getLastModified()).isEqualTo(MODIFIED);
        assertThat(pcc.getLastSynched()).isEqualTo(SYNCHED);
    }
}
