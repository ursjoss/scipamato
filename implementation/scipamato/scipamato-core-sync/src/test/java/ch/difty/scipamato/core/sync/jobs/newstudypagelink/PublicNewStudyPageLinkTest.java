package ch.difty.scipamato.core.sync.jobs.newstudypagelink;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.sync.jobs.PublicEntityTest;

public class PublicNewStudyPageLinkTest extends PublicEntityTest {

    @Test
    public void canSetGet_withStandardFieldsPopulated() {
        PublicNewStudyPageLink pp = PublicNewStudyPageLink
            .builder()
            .langCode("en")
            .sort(1)
            .title("title")
            .url("url")
            .lastSynched(SYNCHED)
            .build();

        assertThat(pp.getLangCode()).isEqualTo("en");
        assertThat(pp.getSort()).isEqualTo(1);
        assertThat(pp.getTitle()).isEqualTo("title");
        assertThat(pp.getUrl()).isEqualTo("url");
        assertThat(pp.getLastSynched()).isEqualTo(SYNCHED);
    }
}
