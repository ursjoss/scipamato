package ch.difty.scipamato.core.persistence.newsletter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.persistence.JooqTransactionalIntegrationTest;

public class JooqNewsletterTopicRepoIntegrationTest extends JooqTransactionalIntegrationTest {

    @Autowired
    private JooqNewsletterTopicRepo repo;

    @Test
    public void findingAll() {
        assertThat(repo.findAll("en")).hasSize(3);
    }

//    @Test
//    public void findingById_withNonExistingId_returnsNull() {
//        assertThat(repo.findById(-1)).isNull();
//    }
//
//    @Test
//    public void findById_withExistingId_returnsRecord() {
//        final NewsletterTopic nl = repo.findById(1);
//        assertThat(nl).isNotNull();
//        assertThat(nl.getId()).isEqualTo(1);
//        assertThat(nl.getTitle()).isEqualTo("Ultrafeine Partikel");
//    }
//
//    @Test
//    public void addingRecord_savesRecordAndRefreshesId() {
//        NewsletterTopic nlt = makeMinimalNewsletterTopic();
//        assertThat(nlt.getId()).isNull();
//
//        NewsletterTopic saved = repo.add(nlt);
//        assertThat(saved).isNotNull();
//        assertThat(saved.getId())
//            .isNotNull()
//            .isGreaterThan(0);
//        assertThat(saved.getTitle()).isEqualTo("new-title");
//    }
//
//    private NewsletterTopic makeMinimalNewsletterTopic() {
//        final NewsletterTopic nlt = new NewsletterTopic();
//        nlt.setTitle("new-title");
//        return nlt;
//    }
//
//    @Test
//    public void updatingRecord() {
//        NewsletterTopic nlt = repo.add(makeMinimalNewsletterTopic());
//        assertThat(nlt).isNotNull();
//        assertThat(nlt.getId())
//            .isNotNull()
//            .isGreaterThan(0);
//        final int id = nlt.getId();
//        assertThat(nlt.getTitle()).isEqualTo("new-title");
//
//        nlt.setTitle("mod-title");
//        repo.update(nlt);
//        assertThat(nlt.getId()).isEqualTo(id);
//
//        NewsletterTopic newCopy = repo.findById(id);
//        assertThat(newCopy).isNotEqualTo(nlt);
//        assertThat(newCopy.getId()).isEqualTo(id);
//        assertThat(newCopy.getTitle()).isEqualTo("mod-title");
//    }
//
//    @Test
//    public void deletingRecord() {
//        NewsletterTopic nlt = repo.add(makeMinimalNewsletterTopic());
//        assertThat(nlt).isNotNull();
//        assertThat(nlt.getId())
//            .isNotNull()
//            .isGreaterThan(0);
//        final int id = nlt.getId();
//        assertThat(nlt.getTitle()).isEqualTo("new-title");
//
//        NewsletterTopic deleted = repo.delete(id, nlt.getVersion());
//        assertThat(deleted.getId()).isEqualTo(id);
//
//        assertThat(repo.findById(id)).isNull();
//    }

}
