package ch.difty.scipamato.publ.persistence.newstudies;

import static ch.difty.scipamato.common.TestUtilsKt.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.publ.entity.NewStudyPageLink;
import ch.difty.scipamato.publ.entity.NewStudyTopic;
import ch.difty.scipamato.publ.entity.Newsletter;

@ExtendWith(MockitoExtension.class)
class JooqNewStudyTopicServiceTest {

    private static final int NL_ID = 17;

    @Mock
    private NewStudyRepository repoMock;

    private NewStudyTopic newStudyTopicDummy = new NewStudyTopic(1, "title");

    private JooqNewStudyTopicService service;

    private List<NewStudyTopic> studyTopics;

    @BeforeEach
    void setUp() {
        service = new JooqNewStudyTopicService(repoMock);

        studyTopics = new ArrayList<>();
        studyTopics.add(newStudyTopicDummy);
        studyTopics.add(newStudyTopicDummy);
    }

    @Test
    void findingMostRecentNewStudyTopics_withNullLanguage_throws() {
        Assertions.assertThrows(NullArgumentException.class, () -> service.findMostRecentNewStudyTopics(null));
    }

    @Test
    void findingMostRecentNewStudyTopics() {
        when(repoMock.findMostRecentNewsletterId()).thenReturn(Optional.of(NL_ID));
        when(repoMock.findNewStudyTopicsForNewsletter(NL_ID, "en")).thenReturn(studyTopics);

        assertThat(service.findMostRecentNewStudyTopics("en")).containsExactly(newStudyTopicDummy, newStudyTopicDummy);

        verify(repoMock).findMostRecentNewsletterId();
        verify(repoMock).findNewStudyTopicsForNewsletter(NL_ID, "en");
    }

    @Test
    void findNewStudyTopicsForNewsletterIssue_withNullIssue_throws() {
        assertDegenerateSupplierParameter(() -> service.findNewStudyTopicsForNewsletterIssue(null, "en"), "issue");
    }

    @Test
    void findNewStudyTopicsForNewsletterIssue_withNullLanguageCode_throws() {
        assertDegenerateSupplierParameter(() -> service.findNewStudyTopicsForNewsletterIssue("2018/06", null),
            "languageCode");
    }

    @Test
    void findNewStudyTopicsForNewsletterIssue() {
        when(repoMock.findIdOfNewsletterWithIssue("2018/06")).thenReturn(Optional.of(NL_ID));
        when(repoMock.findNewStudyTopicsForNewsletter(NL_ID, "en")).thenReturn(studyTopics);

        assertThat(service.findNewStudyTopicsForNewsletterIssue("2018/06", "en")).containsExactly(newStudyTopicDummy,
            newStudyTopicDummy);

        verify(repoMock).findIdOfNewsletterWithIssue("2018/06");
        verify(repoMock).findNewStudyTopicsForNewsletter(NL_ID, "en");
    }

    @Test
    void findingArchivedNewsletters_delegatesToRepo() {
        when(repoMock.findArchivedNewsletters(14, "de")).thenReturn(
            List.of(new Newsletter(2, "2018/06", LocalDate.of(2018, 6, 10)),
                new Newsletter(1, "2018/04", LocalDate.of(2018, 4, 10))));

        assertThat(service.findArchivedNewsletters(14, "de")).hasSize(2);

        verify(repoMock).findArchivedNewsletters(14, "de");
    }

    @Test
    void findingNewStudyPageLinks_delegatesToRepo() {
        when(repoMock.findNewStudyPageLinks("de")).thenReturn(
            List.of(new NewStudyPageLink("en", 1, "title1", "url1"), new NewStudyPageLink("en", 2, "title2", "url2")));

        assertThat(service.findNewStudyPageLinks("de")).hasSize(2);

        verify(repoMock).findNewStudyPageLinks("de");
    }
}
