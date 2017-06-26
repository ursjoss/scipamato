package ch.difty.scipamato.pubmed.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

import ch.difty.scipamato.lib.NullArgumentException;
import ch.difty.scipamato.pubmed.Article;
import ch.difty.scipamato.pubmed.ArticleTitle;
import ch.difty.scipamato.pubmed.Journal;
import ch.difty.scipamato.pubmed.JournalIssue;
import ch.difty.scipamato.pubmed.MedlineCitation;
import ch.difty.scipamato.pubmed.MedlineDate;
import ch.difty.scipamato.pubmed.MedlineJournalInfo;
import ch.difty.scipamato.pubmed.Month;
import ch.difty.scipamato.pubmed.PMID;
import ch.difty.scipamato.pubmed.PubDate;
import ch.difty.scipamato.pubmed.PubmedArticle;
import ch.difty.scipamato.pubmed.Year;

public class ScipamatoPubmedArticleTest {

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
            new ScipamatoPubmedArticle(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("pubmedArticle must not be null.");
        }
    }

    @Test
    public void degenerateConstruction_withNullMedlineCitation() {
        pubmedArticle.setMedlineCitation(null);
        try {
            new ScipamatoPubmedArticle(pubmedArticle);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("pubmedArticle.medlineCitation must not be null.");
        }
    }

    @Test
    public void degenerateConstruction_withNullArticle() {
        pubmedArticle.getMedlineCitation().setArticle(null);
        try {
            new ScipamatoPubmedArticle(pubmedArticle);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("pubmedArticle.medlineCitation.article must not be null.");
        }
    }

    @Test
    public void degenerateConstruction_withNullJournal() {
        pubmedArticle.getMedlineCitation().getArticle().setJournal(null);
        try {
            new ScipamatoPubmedArticle(pubmedArticle);
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
            new ScipamatoPubmedArticle(pubmedArticle);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("pubmedArticle.medlineCitation.pmid must not be null.");
        }
    }

    @Test
    public void degenerateConstruction_withNullJournalIssue() {
        pubmedArticle.getMedlineCitation().getArticle().getJournal().setJournalIssue(null);
        try {
            new ScipamatoPubmedArticle(pubmedArticle);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("pubmedArticle.medlineCitation.article.journal.journalIssue must not be null.");
        }
    }

    @Test
    public void degenerateConstruction_withNullPubDate() {
        pubmedArticle.getMedlineCitation().getArticle().getJournal().getJournalIssue().setPubDate(null);
        try {
            new ScipamatoPubmedArticle(pubmedArticle);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("pubmedArticle.medlineCitation.article.journal.journalIssue.pubDate must not be null.");
        }
    }

    @Test
    public void degenerateConstruction_withNullMedlineJournalInfo() {
        pubmedArticle.getMedlineCitation().setMedlineJournalInfo(null);
        try {
            new ScipamatoPubmedArticle(pubmedArticle);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("pubmedArticle.medlineCitation.medlineJournalInfo must not be null.");
        }
    }

    @Test
    public void validConstruction() {
        assertThat(new ScipamatoPubmedArticle(pubmedArticle)).isNotNull();
    }

    @Test
    public void extractingYearFromNeitherYearObjectNorMedlineDate_returnsYear0() {
        assertThat(pubmedArticle.getMedlineCitation().getArticle().getJournal().getJournalIssue().getPubDate().getYearOrMonthOrDayOrSeasonOrMedlineDate()).isEmpty();
        Month month = new Month();
        month.setvalue("2016");
        pubmedArticle.getMedlineCitation().getArticle().getJournal().getJournalIssue().getPubDate().getYearOrMonthOrDayOrSeasonOrMedlineDate().add(month);
        ScipamatoPubmedArticle spa = new ScipamatoPubmedArticle(pubmedArticle);
        assertThat(spa.getPublicationYear()).isEqualTo("0");
    }

    @Test
    public void extractYearFromYearObject() {
        Year year = new Year();
        year.setvalue("2016");
        pubmedArticle.getMedlineCitation().getArticle().getJournal().getJournalIssue().getPubDate().getYearOrMonthOrDayOrSeasonOrMedlineDate().add(year);
        ScipamatoPubmedArticle spa = new ScipamatoPubmedArticle(pubmedArticle);
        assertThat(spa.getPublicationYear()).isEqualTo("2016");
    }

    @Test
    public void extractYearFromMedlineDate() {
        MedlineDate md = new MedlineDate();
        md.setvalue("2016 Nov-Dec");
        pubmedArticle.getMedlineCitation().getArticle().getJournal().getJournalIssue().getPubDate().getYearOrMonthOrDayOrSeasonOrMedlineDate().add(md);

        ScipamatoPubmedArticle spa = new ScipamatoPubmedArticle(pubmedArticle);
        assertThat(spa.getPublicationYear()).isEqualTo("2016");
    }

}
