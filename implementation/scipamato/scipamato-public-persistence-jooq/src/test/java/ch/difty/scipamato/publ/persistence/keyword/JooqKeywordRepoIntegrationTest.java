package ch.difty.scipamato.publ.persistence.keyword;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.publ.entity.Keyword;
import ch.difty.scipamato.publ.persistence.JooqTransactionalIntegrationTest;

@Slf4j
public class JooqKeywordRepoIntegrationTest extends JooqTransactionalIntegrationTest {

    @Autowired
    private JooqKeywordRepo repo;

    @Test
    public void findingAllKeywordsInGerman() {
        List<Keyword> keywords = repo.findKeywords("de");
        assertThat(keywords)
            .extracting("name")
            .containsExactly("Aerosol", "Aktivität, eingeschränkte", "Allergie");
        keywords.forEach((c) -> log.debug(c.toString()));
    }

    @Test
    public void findingAllKeywords1InEnglish() {
        List<Keyword> keywords = repo.findKeywords("en");
        assertThat(keywords)
            .extracting("name")
            .containsExactly("Aerosol", "Allergies", "Restricted activity");
        keywords.forEach((c) -> log.debug(c.toString()));
    }

    @Test
    public void findingAllKeywords1InFrench() {
        List<Keyword> keywords = repo.findKeywords("fr");
        assertThat(keywords)
            .extracting("name")
            .containsExactly("Activités réduites", "Aérosol", "Allérgie");
        keywords.forEach((c) -> log.debug(c.toString()));
    }

}
