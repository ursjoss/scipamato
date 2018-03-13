package ch.difty.scipamato.publ.persistence.newstudies;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JooqNewStudyTopicServiceTest {

    private JooqNewStudyTopicService service = new JooqNewStudyTopicService();

    @Test
    public void findingMostRecentNewStudyTopics() {
        // currently only stubbed data, not yet going to database TODO fix
        assertThat(service.findMostRecentNewStudyTopics()).hasSize(4);
    }
}
