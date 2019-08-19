package ch.difty.scipamato.publ.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import ch.difty.scipamato.publ.entity.Keyword;
import ch.difty.scipamato.publ.persistence.keyword.JooqKeywordRepo;

@Slf4j
@JooqTest
@Testcontainers
class JooqKeywordRepoIntegrationTest {

    @Autowired
    private JooqKeywordRepo repo;

    @Test
    void findingAllKeywordsInGerman() {
        List<Keyword> keywords = repo.findKeywords("de");
        assertThat(keywords)
            .extracting("name")
            .containsExactly("Aerosol", "Aktivität, eingeschränkte", "Allergie");
        keywords.forEach((c) -> log.debug(c.toString()));
    }

    @Test
    void findingAllKeywords1InEnglish() {
        List<Keyword> keywords = repo.findKeywords("en");
        assertThat(keywords)
            .extracting("name")
            .containsExactly("Aerosol", "Allergies", "Restricted activity");
        keywords.forEach((c) -> log.debug(c.toString()));
    }

    @Test
    void findingAllKeywords1InFrench() {
        List<Keyword> keywords = repo.findKeywords("fr");
        assertThat(keywords)
            .extracting("name")
            .containsExactly("Activités réduites", "Aérosol", "Allergie");
        keywords.forEach((c) -> log.debug(c.toString()));
    }

}
