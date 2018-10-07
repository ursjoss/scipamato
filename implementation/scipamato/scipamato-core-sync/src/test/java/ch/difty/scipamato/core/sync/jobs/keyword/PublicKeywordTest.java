package ch.difty.scipamato.core.sync.jobs.keyword;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.core.sync.jobs.PublicEntityTest;

public class PublicKeywordTest extends PublicEntityTest {

    @Test
    public void canSetGet() {
        PublicKeyword pc = PublicKeyword
            .builder()
            .id(1)
            .keywordId(2)
            .langCode("lc")
            .name("name")
            .version(3)
            .created(CREATED)
            .lastModified(MODIFIED)
            .lastSynched(SYNCHED)
            .searchOverride("so")
            .build();

        assertThat(pc.getId()).isEqualTo(1);
        assertThat(pc.getKeywordId()).isEqualTo(2);
        assertThat(pc.getLangCode()).isEqualTo("lc");
        assertThat(pc.getName()).isEqualTo("name");
        assertThat(pc.getVersion()).isEqualTo(3);
        assertThat(pc.getCreated()).isEqualTo(CREATED);
        assertThat(pc.getLastModified()).isEqualTo(MODIFIED);
        assertThat(pc.getLastSynched()).isEqualTo(SYNCHED);
        assertThat(pc.getSearchOverride()).isEqualTo("so");
    }
}