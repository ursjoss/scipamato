package ch.difty.scipamato.core.sync.jobs.language;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.sync.jobs.PublicEntityTest;

class PublicLanguageTest extends PublicEntityTest {

    @Test
    void canSetGet_withStandardFieldsPopulated() {
        PublicLanguage pp = PublicLanguage
            .builder()
            .code("en")
            .mainLanguage(true)
            .lastSynched(SYNCHED)
            .build();

        assertThat(pp.getCode()).isEqualTo("en");
        assertThat(pp.getMainLanguage()).isEqualTo(true);
        assertThat(pp.getLastSynched()).isEqualTo(SYNCHED);
    }
}
