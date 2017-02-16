package ch.difty.sipamato.pubmed.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.pubmed.Article;
import ch.difty.sipamato.pubmed.ArticleTitle;
import ch.difty.sipamato.pubmed.Journal;
import ch.difty.sipamato.pubmed.JournalIssue;
import ch.difty.sipamato.pubmed.MedlineCitation;
import ch.difty.sipamato.pubmed.MedlineJournalInfo;
import ch.difty.sipamato.pubmed.PMID;
import ch.difty.sipamato.pubmed.PubDate;
import ch.difty.sipamato.pubmed.PubmedArticle;

public class SipamatoPubmedArticleTest {

    private final PubmedArticle pubmedArticle = makeMinimalValidPubmedArticle();

    public static PubmedArticle makeMinimalValidPubmedArticle() {
        PubmedArticle pa = new PubmedArticle();
        MedlineCitation medlineCitation = new MedlineCitation();
        Article article = new Article();
        Journal journal = new Journal();
        JournalIssue journalIssue = new JournalIssue();
        journalIssue.setPubDate(new PubDate());
        journal.setJournalIssue(journalIssue);
        article.setJournal(journal);
        article.setArticleTitle(new ArticleTitle());
        medlineCitation.setArticle(article);
        medlineCitation.setPMID(new PMID());
        medlineCitation.setMedlineJournalInfo(new MedlineJournalInfo());
        pa.setMedlineCitation(medlineCitation);
        return pa;
    }

    @Test
    public void degenerateConstruction_withNullPubmedArticle() {
        try {
            new SipamatoPubmedArticle(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("pubmedArticle must not be null.");
        }
    }

    @Test
    public void degenerateConstruction_withNullMedlineCitation() {
        pubmedArticle.setMedlineCitation(null);
        try {
            new SipamatoPubmedArticle(pubmedArticle);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("pubmedArticle.medlineCitation must not be null.");
        }
    }

    @Test
    public void degenerateConstruction_withNullArticle() {
        pubmedArticle.getMedlineCitation().setArticle(null);
        try {
            new SipamatoPubmedArticle(pubmedArticle);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("pubmedArticle.medlineCitation.article must not be null.");
        }
    }

    @Test
    public void degenerateConstruction_withNullJournal() {
        pubmedArticle.getMedlineCitation().getArticle().setJournal(null);
        try {
            new SipamatoPubmedArticle(pubmedArticle);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("pubmedArticle.medlineCitation.article.journal must not be null.");
        }
    }

    @Test
    public void degenerateConstruction_withNullPmid() {
        pubmedArticle.getMedlineCitation().setPMID(null);
        ;
        try {
            new SipamatoPubmedArticle(pubmedArticle);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("pubmedArticle.medlineCitation.pmid must not be null.");
        }
    }

    @Test
    public void degenerateConstruction_withNullJournalIssue() {
        pubmedArticle.getMedlineCitation().getArticle().getJournal().setJournalIssue(null);
        try {
            new SipamatoPubmedArticle(pubmedArticle);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("pubmedArticle.medlineCitation.article.journal.journalIssue must not be null.");
        }
    }

    @Test
    public void degenerateConstruction_withNullPubDate() {
        pubmedArticle.getMedlineCitation().getArticle().getJournal().getJournalIssue().setPubDate(null);
        try {
            new SipamatoPubmedArticle(pubmedArticle);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("pubmedArticle.medlineCitation.article.journal.journalIssue.pubDate must not be null.");
        }
    }

    @Test
    public void degenerateConstruction_withNullMedlineJournalInfo() {
        pubmedArticle.getMedlineCitation().setMedlineJournalInfo(null);
        try {
            new SipamatoPubmedArticle(pubmedArticle);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("pubmedArticle.medlineCitation.medlineJournalInfo must not be null.");
        }
    }

    @Test
    public void validConstruction() {
        assertThat(new SipamatoPubmedArticle(pubmedArticle)).isNotNull();
    }

}
