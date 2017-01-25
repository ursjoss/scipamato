package ch.difty.sipamato.web.jasper.review;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.lib.NullArgumentException;

@RunWith(MockitoJUnitRunner.class)
public class PaperReviewTest {

    private PaperReview pr;

    @Mock
    private Paper paperMock;

    @Before
    public void setUp() {
        when(paperMock.getId()).thenReturn(5l);
        when(paperMock.getFirstAuthor()).thenReturn("fa");
        when(paperMock.getPublicationYear()).thenReturn(2017);
        when(paperMock.getLocation()).thenReturn("l");
        when(paperMock.getMethodOutcome()).thenReturn("mo");
        when(paperMock.getExposurePollutant()).thenReturn("ep");
        when(paperMock.getMethodStudyDesign()).thenReturn("msd");
        when(paperMock.getPopulationDuration()).thenReturn("pd");
        when(paperMock.getMethodStatistics()).thenReturn("ms");
        when(paperMock.getPopulationParticipants()).thenReturn("pp");
        when(paperMock.getExposureAssessment()).thenReturn("ea");
        when(paperMock.getResultExposureRange()).thenReturn("rer");
        when(paperMock.getMethodConfounders()).thenReturn("mc");
        when(paperMock.getResultEffectEstimate()).thenReturn("ree");
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(paperMock);
    }

    @Test
    public void degenerateConstruction_withNullPaper_throws() {
        try {
            new PaperReview(null, "idl", "ayl", "ll", "mol", "epl", "msdl", "pdl", "msl", "ppl", "eal", "rerl", "mcl", "reel", "b", "cb");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("paper must not be null.");
        }
    }

    @Test
    public void instantiatingWithAllNullFields_returnsBlankValues() {
        Mockito.reset(paperMock);

        pr = new PaperReview(paperMock, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        assertThat(pr.getId()).isEmpty();
        assertThat(pr.getAuthorYear()).isEmpty();
        assertThat(pr.getLocation()).isEmpty();
        assertThat(pr.getMethodOutcome()).isEmpty();
        assertThat(pr.getExposurePollutant()).isEmpty();
        assertThat(pr.getMethodStudyDesign()).isEmpty();
        assertThat(pr.getPopulationDuration()).isEmpty();
        assertThat(pr.getMethodStatistics()).isEmpty();
        assertThat(pr.getPopulationParticipants()).isEmpty();
        assertThat(pr.getExposureAssessment()).isEmpty();
        assertThat(pr.getResultExposureRange()).isEmpty();
        assertThat(pr.getMethodConfounders()).isEmpty();
        assertThat(pr.getResultEffectEstimate()).isEmpty();

        assertBlankLabels();

        verifyPaperCalls(1);
    }

    private void assertBlankLabels() {
        assertThat(pr.getIdLabel()).isEmpty();
        assertThat(pr.getAuthorYearLabel()).isEmpty();
        assertThat(pr.getLocationLabel()).isEmpty();
        assertThat(pr.getMethodOutcomeLabel()).isEmpty();
        assertThat(pr.getExposurePollutantLabel()).isEmpty();
        assertThat(pr.getMethodStudyDesignLabel()).isEmpty();
        assertThat(pr.getPopulationDurationLabel()).isEmpty();
        assertThat(pr.getMethodStatisticsLabel()).isEmpty();
        assertThat(pr.getPopulationParticipantsLabel()).isEmpty();
        assertThat(pr.getExposureAssessmentLabel()).isEmpty();
        assertThat(pr.getResultExposureRangeLabel()).isEmpty();
        assertThat(pr.getMethodConfoundersLabel()).isEmpty();
        assertThat(pr.getResultEffectEstimateLabel()).isEmpty();
        assertThat(pr.getBrand()).isEmpty();
        assertThat(pr.getCreatedBy()).isEmpty();
    }

    private void verifyPaperCalls(int callsToId) {
        verify(paperMock, times(callsToId)).getId();
        verify(paperMock).getFirstAuthor();
        verify(paperMock).getPublicationYear();
        verify(paperMock).getLocation();
        verify(paperMock).getMethodOutcome();
        verify(paperMock).getExposurePollutant();
        verify(paperMock).getMethodStudyDesign();
        verify(paperMock).getPopulationDuration();
        verify(paperMock).getMethodStatistics();
        verify(paperMock).getPopulationParticipants();
        verify(paperMock).getExposureAssessment();
        verify(paperMock).getResultExposureRange();
        verify(paperMock).getMethodConfounders();
        verify(paperMock).getResultEffectEstimate();
    }

    @Test
    public void instantiatingWithValidFieldsButNullLabels() {
        pr = new PaperReview(paperMock, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        assertFieldValues();
        assertBlankLabels();

        verifyPaperCalls(2);
    }

    private void assertFieldValues() {
        assertThat(pr.getId()).isEqualTo("5");
        assertThat(pr.getAuthorYear()).isEqualTo("fa 2017");
        assertThat(pr.getLocation()).isEqualTo("l");
        assertThat(pr.getMethodOutcome()).isEqualTo("mo");
        assertThat(pr.getExposurePollutant()).isEqualTo("ep");
        assertThat(pr.getMethodStudyDesign()).isEqualTo("msd");
        assertThat(pr.getPopulationDuration()).isEqualTo("pd");
        assertThat(pr.getMethodStatistics()).isEqualTo("ms");
        assertThat(pr.getPopulationParticipants()).isEqualTo("pp");
        assertThat(pr.getExposureAssessment()).isEqualTo("ea");
        assertThat(pr.getResultExposureRange()).isEqualTo("rer");
        assertThat(pr.getMethodConfounders()).isEqualTo("mc");
        assertThat(pr.getResultEffectEstimate()).isEqualTo("ree");
    }

    @Test
    public void instantiatingWithValidFieldsAndvalidLabels() {
        pr = new PaperReview(paperMock, "idl", "ayl", "ll", "mol", "epl", "msdl", "pdl", "msl", "ppl", "eal", "rerl", "mcl", "reel", "b", "cb");

        assertFieldValues();

        assertThat(pr.getIdLabel()).isEqualTo("idl");
        assertThat(pr.getAuthorYearLabel()).isEqualTo("ayl");
        assertThat(pr.getLocationLabel()).isEqualTo("ll");
        assertThat(pr.getMethodOutcomeLabel()).isEqualTo("mol");
        assertThat(pr.getExposurePollutantLabel()).isEqualTo("epl");
        assertThat(pr.getMethodStudyDesignLabel()).isEqualTo("msdl");
        assertThat(pr.getPopulationDurationLabel()).isEqualTo("pdl");
        assertThat(pr.getMethodStatisticsLabel()).isEqualTo("msl");
        assertThat(pr.getPopulationParticipantsLabel()).isEqualTo("ppl");
        assertThat(pr.getExposureAssessmentLabel()).isEqualTo("eal");
        assertThat(pr.getResultExposureRangeLabel()).isEqualTo("rerl");
        assertThat(pr.getMethodConfoundersLabel()).isEqualTo("mcl");
        assertThat(pr.getResultEffectEstimateLabel()).isEqualTo("reel");
        assertThat(pr.getBrand()).isEqualTo("b");
        assertThat(pr.getCreatedBy()).isEqualTo("cb");

        verifyPaperCalls(2);
    }

    @Test
    public void authorYear_withNullFirstAuthorAndYear() {
        Mockito.reset(paperMock);
        when(paperMock.getFirstAuthor()).thenReturn(null);
        when(paperMock.getPublicationYear()).thenReturn(null);

        pr = new PaperReview(paperMock, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        assertThat(pr.getAuthorYear()).isEqualTo("");

        verifyPaperCalls(1);
    }

    @Test
    public void authorYear_withOnlyFirstAuthor() {
        Mockito.reset(paperMock);
        when(paperMock.getFirstAuthor()).thenReturn("fa");
        when(paperMock.getPublicationYear()).thenReturn(null);

        pr = new PaperReview(paperMock, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        assertThat(pr.getAuthorYear()).isEqualTo("fa");

        verifyPaperCalls(1);
    }

    @Test
    public void authorYear_withOnlyPubYear() {
        Mockito.reset(paperMock);
        when(paperMock.getFirstAuthor()).thenReturn(null);
        when(paperMock.getPublicationYear()).thenReturn(2017);

        pr = new PaperReview(paperMock, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        assertThat(pr.getAuthorYear()).isEqualTo("2017");

        verifyPaperCalls(1);
    }

}
