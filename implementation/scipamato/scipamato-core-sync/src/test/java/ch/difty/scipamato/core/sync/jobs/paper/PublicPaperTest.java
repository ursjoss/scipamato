package ch.difty.scipamato.core.sync.jobs.paper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.core.sync.jobs.PublicEntityTest;

public class PublicPaperTest extends PublicEntityTest {

    @Test
    public void canSetGet_withStandardFieldsPopulated() {
        PublicPaper pp = PublicPaper
            .builder()
            .id(1L)
            .number(2L)
            .pmId(10000)
            .authors("authors")
            .title("title")
            .location("location")
            .publicationYear(2017)
            .goals("goals")
            .methods("methods")
            .population("population")
            .result("result")
            .comment("comment")
            .codesPopulation(new Short[] { (short) 1, (short) 2 })
            .codesStudyDesign(new Short[] { (short) 3, (short) 4 })
            .codes(new String[] { "1A", "2B", "3C" })
            .version(3)
            .created(CREATED)
            .lastModified(MODIFIED)
            .lastSynched(SYNCHED)
            .build();

        assertThat(pp.getId()).isEqualTo(1L);
        assertThat(pp.getNumber()).isEqualTo(2L);
        assertThat(pp.getPmId()).isEqualTo(10000);
        assertThat(pp.getAuthors()).isEqualTo("authors");
        assertThat(pp.getTitle()).isEqualTo("title");
        assertThat(pp.getLocation()).isEqualTo("location");
        assertThat(pp.getPublicationYear()).isEqualTo(2017);
        assertThat(pp.getGoals()).isEqualTo("goals");
        assertThat(pp.getMethods()).isEqualTo("methods");
        assertThat(pp.getPopulation()).isEqualTo("population");
        assertThat(pp.getResult()).isEqualTo("result");
        assertThat(pp.getComment()).isEqualTo("comment");
        assertThat(pp.getVersion()).isEqualTo(3);
        assertThat(pp.getCreated()).isEqualTo(CREATED);
        assertThat(pp.getLastModified()).isEqualTo(MODIFIED);
        assertThat(pp.getCodesPopulation()).containsExactly((short) 1, (short) 2);
        assertThat(pp.getCodesStudyDesign()).containsExactly((short) 3, (short) 4);
        assertThat(pp.getCodes()).containsExactly("1A", "2B", "3C");
        assertThat(pp.getLastSynched()).isEqualTo(SYNCHED);
    }

    @Test
    public void canSetGet_withAllShortFieldsPopulated() {
        PublicPaper pp = PublicPaper
            .builder()
            .methodStudyDesign("methodStudyDesign")
            .methodOutcome("methodOutcome")
            .exposurePollutant("exposurePollutant")
            .exposureAssessment("exposureAssessment")
            .methodStatistics("methodStatistics")
            .methodConfounders("methodConfounders")
            .populationPlace("populationPlace")
            .populationParticipants("populationParticipants")
            .populationDuration("populationDuration")
            .resultExposureRange("resultExposureRange")
            .resultEffectEstimate("resultEffectEstimate")
            .resultMeasuredOutcome("resultMeasuredOutcome")
            .build();

        assertThat(pp.getMethods()).isEqualTo(
            "methodStudyDesign - methodOutcome - exposurePollutant - exposureAssessment - methodStatistics - methodConfounders");
        assertThat(pp.getPopulation()).isEqualTo("populationPlace - populationParticipants - populationDuration");
        assertThat(pp.getResult()).isEqualTo("resultExposureRange - resultEffectEstimate - resultMeasuredOutcome");
    }

    @Test
    public void canSetGet_withSomeShortFieldsPopulated_usesThoseAvailable() {
        PublicPaper pp = PublicPaper
            .builder()
            .methodStudyDesign("methodStudyDesign")
            .methodStatistics("methodStatistics")
            .populationParticipants("populationParticipants")
            .populationDuration("populationDuration")
            .resultExposureRange("resultExposureRange")
            .resultEffectEstimate("resultEffectEstimate")
            .build();

        assertThat(pp.getMethods()).isEqualTo("methodStudyDesign - methodStatistics");
        assertThat(pp.getPopulation()).isEqualTo("populationParticipants - populationDuration");
        assertThat(pp.getResult()).isEqualTo("resultExposureRange - resultEffectEstimate");
    }

    @Test
    public void canSetGet_withBothNormalAndShortFieldPresents_normalFieldWins() {
        PublicPaper pp = PublicPaper
            .builder()
            .methods("methods")
            .exposurePollutant("exposurePollutant")
            .exposureAssessment("exposureAssessment")
            .methodStudyDesign("methodStudyDesign")
            .methodStatistics("methodStatistics")
            .population("population")
            .populationParticipants("populationParticipants")
            .populationDuration("populationDuration")
            .result("result")
            .resultExposureRange("resultExposureRange")
            .resultEffectEstimate("resultEffectEstimate")
            .build();

        assertThat(pp.getMethods()).isEqualTo("methods");
        assertThat(pp.getPopulation()).isEqualTo("population");
        assertThat(pp.getResult()).isEqualTo("result");
    }
}
