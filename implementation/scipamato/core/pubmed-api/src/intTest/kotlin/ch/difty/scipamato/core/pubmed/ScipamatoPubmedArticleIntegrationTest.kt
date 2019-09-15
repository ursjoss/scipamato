package ch.difty.scipamato.core.pubmed

import ch.difty.scipamato.core.pubmed.api.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection", "FunctionName")
internal class ScipamatoPubmedArticleIntegrationTest : PubmedIntegrationTest() {

    @Test
    fun feedIntoScipamatoArticle_25395026() {
        val articles = getPubmedArticles(XML_2539026)
        assertThat(articles).hasSize(1)
        assertArticle239026(articles.first())
    }

    private fun assertArticle239026(sa: PubmedArticleFacade) {
        with(sa) {
            assertThat(pmId).isEqualTo("25395026")
            assertThat(authors).isEqualTo(
                    "Turner MC, Cohen A, Jerrett M, Gapstur SM, Diver WR, Pope CA 3rd, Krewski D, Beckerman BS, Samet JM."
            )
            assertThat(firstAuthor).isEqualTo("Turner")
            assertThat(publicationYear).isEqualTo("2014")
            assertThat(location).isEqualTo("Am J Epidemiol. 2014; 180 (12): 1145-1149.")
            assertThat(title).isEqualTo(
                    "Interactions between cigarette smoking and fine particulate matter in the Risk of Lung Cancer Mortality in Cancer Prevention Study II."
            )
            assertThat(doi).isEqualTo("10.1093/aje/kwu275")
            assertThat(originalAbstract).startsWith(
                    "The International Agency for Research on Cancer recently classified outdoor air pollution"
            )
            assertThat(originalAbstract.trim { it <= ' ' }).endsWith(
                    "based on reducing exposure to either risk factor alone."
            )
        }
    }

    @Test
    fun feedIntoScipamatoArticle_23454700_withoutIssue_stillHasColon() {
        val articles = getPubmedArticles(XML_23454700)
        assertThat(articles).hasSize(1)
        with(articles.first()) {
            assertThat(pmId).isEqualTo("23454700")
            assertThat(authors).isEqualTo(
                    "Pascal M, Corso M, Chanel O, Declercq C, Badaloni C, Cesaroni G, Henschel S, Meister K, Haluza D, Martin-Olmedo P, Medina S; Aphekom group.")
            assertThat(firstAuthor).isEqualTo("Pascal")
            assertThat(publicationYear).isEqualTo("2013")
            assertThat(location).isEqualTo("Sci Total Environ. 2013; 449: 390-400.")
            assertThat(title).isEqualTo(
                    "Assessing the public health impacts of urban air pollution in 25 European cities: results of the Aphekom project.")
            assertThat(doi).isEqualTo("10.1016/j.scitotenv.2013.01.077")
            assertThat(originalAbstract).startsWith(
                    "INTRODUCTION: The Aphekom project aimed to provide new, clear, and meaningful information on the health effects of air pollution in Europe.")
            assertThat(originalAbstract).endsWith("EU legislation is being revised for an update in 2013.")
        }
    }

    @Test
    fun feedIntoScipamatoArticle_26607712_notHavingPaginationButElocationIdOfTypePii_usesThat() {
        val articles = getPubmedArticles(XML_26607712)
        assertThat(articles).hasSize(1)
        with(articles.first()) {
            assertThat(pmId).isEqualTo("26607712")
            assertThat(authors).isEqualTo("Hart JE, Puett RC, Rexrode KM, Albert CM, Laden F.")
            assertThat(firstAuthor).isEqualTo("Hart")
            assertThat(publicationYear).isEqualTo("2015")
            assertThat(location).isEqualTo("J Am Heart Assoc. 2015; 4 (12). pii: e002301.")
            assertThat(title).isEqualTo(
                    "Effect Modification of Long-Term Air Pollution Exposures and the Risk of Incident Cardiovascular Disease in US Women.")
            assertThat(doi).isEqualTo("10.1161/JAHA.115.002301")
            assertThat(originalAbstract).startsWith(
                    "BACKGROUND: Ambient air pollution exposures have been frequently linked to cardiovascular disease (CVD) morbidity and mortality. However, less is known about the populations most susceptible to these adverse effects.")
            assertThat(originalAbstract).endsWith(
                    "women with diabetes were identified as the subpopulation most sensitive to the adverse cardiovascular health effects of PM.")
        }
    }

    @Test
    fun feedIntoScipamatoArticle_27214671_yearFromMedDate() {
        val articles = getPubmedArticles(XML_27214671)
        assertThat(articles).hasSize(1)
        with(articles.first()) {
            assertThat(pmId).isEqualTo("27214671")
            assertThat(authors).isEqualTo("Clark-Reyna SE, Grineski SE, Collins TW.")
            assertThat(firstAuthor).isEqualTo("Clark-Reyna")
            assertThat(publicationYear).isEqualTo("2016")
            assertThat(location).isEqualTo("Fam Community Health. 2016; 39 (3): 160-168.")
            assertThat(title).isEqualTo(
                    "Health Status and Residential Exposure to Air Toxics: What Are the Effects on Children's Academic Achievement?")
            assertThat(doi).isEqualTo("10.1097/FCH.0000000000000112")
            assertThat(originalAbstract).startsWith(
                    "This article examines the effects of children's subjective health status and exposure to residential environmental toxins")
            assertThat(originalAbstract).endsWith(
                    "there is an independent effect of air pollution on children's academic achievement that cannot be explained by poor health alone.")
        }
    }

    @Test
    fun feedIntoScipamatoArticle_27224452_authorWithCollectiveName() {
        val articles = getPubmedArticles(XML_27224452)
        assertThat(articles).hasSize(1)
        with(articles.first()) {
            assertThat(pmId).isEqualTo("27224452")
            assertThat(authors).isEqualTo(
                    "Lanzinger S, Schneider A, Breitner S, Stafoggia M, Erzen I, Dostal M, Pastorkova A, Bastian S, Cyrys J, Zscheppang A, Kolodnitska T, Peters A; UFIREG study group.")
            assertThat(firstAuthor).isEqualTo("Lanzinger")
            assertThat(publicationYear).isEqualTo("2016")
            assertThat(location).isEqualTo("Am J Respir Crit Care Med. 2016; 194 (10): 1233-1241.")
            assertThat(title).isEqualTo(
                    "Ultrafine and Fine Particles and Hospital Admissions in Central Europe. Results from the UFIREG Study.")
            assertThat(doi).isEqualTo("10.1164/rccm.201510-2042OC")
            assertThat(originalAbstract).startsWith(
                    "RATIONALE: Evidence of short-term effects of ultrafine particles (UFP) on health is still inconsistent and few multicenter studies have been conducted so far especially in Europe.")
            assertThat(originalAbstract).endsWith(
                    "harmonized UFP measurements to draw definite conclusions on health effects of UFP.")
        }
    }

    @Test
    fun feedIntoScipamatoArticle_27258721() {
        val articles = getPubmedArticles(XML_27258721)
        assertThat(articles).hasSize(1)
        with(articles.first()) {
            assertThat(pmId).isEqualTo("27258721")
            assertThat(authors).isEqualTo(
                    "Aguilera I, Dratva J, Caviezel S, Burdet L, de Groot E, Ducret-Stich RE, Eeftens M, Keidel D, Meier R, Perez L, Rothe T, Schaffner E, Schmit-Trucksäss A, Tsai MY, Schindler C, Künzli N, Probst-Hensch N.")
            assertThat(firstAuthor).isEqualTo("Aguilera")
            assertThat(publicationYear).isEqualTo("2016")
            assertThat(location).isEqualTo("Environ Health Perspect. 2016; 124 (11): 1700-1706.")
            assertThat(title).isEqualTo(
                    "Particulate Matter and Subclinical Atherosclerosis: Associations between Different Particle Sizes and Sources with Carotid Intima-Media Thickness in the SAPALDIA Study.")
            assertThat(doi).isEqualTo("10.1289/EHP161")
            assertThat(originalAbstract).startsWith(
                    "BACKGROUND: Subclinical atherosclerosis has been associated with long-term exposure to particulate matter (PM)")
            assertThat(originalAbstract).endsWith(
                    "SAPALDIA study. Environ Health Perspect 124:1700-1706; http://dx.doi.org/10.1289/EHP161.")
        }
    }

    @Test
    fun manualExplorationOfFile_25395026() {
        val articleSet = getPubmedArticleSet(XML_2539026)
        assertThat(articleSet.pubmedArticleOrPubmedBookArticle).hasSize(1)

        val pubmedArticleObject = articleSet.pubmedArticleOrPubmedBookArticle.first()
        assertThat(pubmedArticleObject).isInstanceOf(PubmedArticle::class.java)

        val pubmedArticle = articleSet.pubmedArticleOrPubmedBookArticle.first() as PubmedArticle

        val medlineCitation = pubmedArticle.medlineCitation
        assertThat(medlineCitation.pmid.getvalue()).isEqualTo("25395026")

        val article = medlineCitation.article
        val journal = article.journal
        val journalIssue = journal.journalIssue
        assertThat(journalIssue.volume).isEqualTo("180")
        assertThat(journalIssue.issue).isEqualTo("12")

        assertThat(journalIssue.pubDate.yearOrMonthOrDayOrSeasonOrMedlineDate).hasSize(3)
        assertThat(journalIssue.pubDate.yearOrMonthOrDayOrSeasonOrMedlineDate[0]).isInstanceOf(Year::class.java)
        assertThat(journalIssue.pubDate.yearOrMonthOrDayOrSeasonOrMedlineDate[1]).isInstanceOf(Month::class.java)
        assertThat(journalIssue.pubDate.yearOrMonthOrDayOrSeasonOrMedlineDate[2]).isInstanceOf(Day::class.java)
        val year = journalIssue.pubDate.yearOrMonthOrDayOrSeasonOrMedlineDate[0] as Year
        assertThat(year.getvalue()).isEqualTo("2014")

        assertThat(journal.title).isEqualTo("American journal of epidemiology")
        assertThat(journal.isoAbbreviation).isEqualTo("Am. J. Epidemiol.")
        assertThat(article.articleTitle.getvalue()).isEqualTo(
                "Interactions between cigarette smoking and fine particulate matter in the Risk of Lung Cancer Mortality in Cancer Prevention Study II."
        )

        assertThat(article.paginationOrELocationID).hasSize(2)
        assertThat(article.paginationOrELocationID[0]).isInstanceOf(Pagination::class.java)
        assertThat(article.paginationOrELocationID[1]).isInstanceOf(ELocationID::class.java)

        val pagination = article.paginationOrELocationID[0] as Pagination
        assertThat(pagination.startPageOrEndPageOrMedlinePgn).hasSize(1)
        assertThat(pagination.startPageOrEndPageOrMedlinePgn[0]).isInstanceOf(MedlinePgn::class.java)
        val pgn = pagination.startPageOrEndPageOrMedlinePgn[0] as MedlinePgn
        assertThat(pgn.getvalue()).isEqualTo("1145-9")

        val elocationId = article.paginationOrELocationID[1] as ELocationID
        assertThat(elocationId.validYN).isEqualTo("Y")
        assertThat(elocationId.getvalue()).isEqualTo("10.1093/aje/kwu275")
        assertThat(elocationId.eIdType).isEqualTo("doi")

        assertThat(article.abstract.abstractText).hasSize(1)
        assertThat(article.abstract.abstractText[0].mixedContent[0].toString()).startsWith(
                "The International Agency for Research on Cancer recently classified outdoor air pollution"
        )

        val authorList = article.authorList
        assertThat(authorList.completeYN).isEqualTo("Y")
        assertThat(authorList.type).isNull()
        assertThat(authorList.author).hasSize(9)
        assertThat(authorList.author.map { it.validYN }).containsOnly("Y")

        val authorNames = authorList.author
                .flatMap { it.lastNameOrForeNameOrInitialsOrSuffixOrCollectiveName }
                .filterIsInstance<LastName>().map { it.getvalue() }
        assertThat(authorNames).contains("Turner", "Cohen", "Jerrett", "Gapstur", "Diver", "Pope", "Krewski",
                "Beckerman", "Samet")

        assertThat(article.articleDate).hasSize(1)
        val articleDate = article.articleDate[0]
        assertThat(articleDate.dateType).isEqualTo("Electronic")
        assertThat(articleDate.year.getvalue()).isEqualTo("2014")

        val medlineJournalInfo = medlineCitation.medlineJournalInfo
        assertThat(medlineJournalInfo.country).isEqualTo("United States")
        assertThat(medlineJournalInfo.medlineTA).isEqualTo("Am J Epidemiol")
        assertThat(medlineJournalInfo.nlmUniqueID).isEqualTo("7910653")
        assertThat(medlineJournalInfo.issnLinking).isEqualTo("0002-9262")

        val chemicalList = medlineCitation.chemicalList
        assertThat(chemicalList.chemical).hasSize(3)

        val meshHeadingList = medlineCitation.meshHeadingList
        assertThat(meshHeadingList.meshHeading).hasSize(20)

        val keywordList = medlineCitation.keywordList
        assertThat(keywordList).hasSize(1)
    }

    @Test
    fun feedIntoScipamatoArticle_30124840() {
        val articles = getPubmedArticles(XML_30124840)
        assertThat(articles).hasSize(1)
        with(articles.first()) {
            assertThat(pmId).isEqualTo("30124840")
            assertThat(authors).isEqualTo(
                    "Münzel T, Gori T, Al-Kindi S, Deanfield J, Lelieveld J, Daiber A, Rajagopalan S.")
            assertThat(firstAuthor).isEqualTo("Münzel")
            assertThat(publicationYear).isEqualTo("2018")
            assertThat(location).isEqualTo(
                    "Eur Heart J. 2018 Aug 14. doi: 10.1093/eurheartj/ehy481. [Epub ahead of print]")
            assertThat(title).isEqualTo(
                    "Effects of gaseous and solid constituents of air pollution on endothelial function.")
            assertThat(doi).isEqualTo("10.1093/eurheartj/ehy481")
        }
    }

    @Test
    fun feedIntoScipamatoArticle_29144419_withTagsInAbstract_canExtractAbstract() {
        val articles = getPubmedArticles(XML_29144419)
        assertThat(articles).hasSize(1)
        with(articles.first()) {
            assertThat(pmId).isEqualTo("29144419")
            assertThat(authors).isEqualTo("Oudin A, Bråbäck L, Oudin Åström D, Forsberg B.")
            assertThat(firstAuthor).isEqualTo("Oudin")
            assertThat(publicationYear).isEqualTo("2017")
            assertThat(location).isEqualTo("Int J Environ Res Public Health. 2017; 14 (11). pii: E1392.")
            assertThat(title).isEqualTo(
                    "Air Pollution and Dispensed Medications for Asthma, and Possible Effect Modifiers Related to Mental Health and Socio-Economy: A Longitudinal Cohort Study of Swedish Children and Adolescents.")
            assertThat(doi).isEqualTo("10.3390/ijerph14111392")
            assertThat(originalAbstract).startsWith(
                    "It has been suggested that children that are exposed to a stressful environment at home have an increased susceptibility for air pollution-related asthma.")
            assertThat(originalAbstract).endsWith(
                    "We did not observe support for our hypothesis that stressors linked to socio-economy or mental health problems would increase susceptibility to the effects of air pollution on the development of asthma.")
            assertThat(originalAbstract).hasSize(1844)
        }
    }

    @Test
    fun feedIntoScipamatoArticle_30077140_withTagsInTitleAndAbstract_canExtractFullTitleAndAbstract() {
        val articles = getPubmedArticles(XML_30077140)
        assertThat(articles).hasSize(1)
        with(articles.first()) {
            assertThat(pmId).isEqualTo("30077140")
            assertThat(authors).isEqualTo("Vodonos A, Awad YA, Schwartz J.")
            assertThat(firstAuthor).isEqualTo("Vodonos")
            assertThat(publicationYear).isEqualTo("2018")
            assertThat(location).isEqualTo("Environ Res. 2018; 166: 677-689.")
            assertThat(title).isEqualTo(
                    "The concentration-response between long-term PM2.5 exposure and mortality; A meta-regression approach.")
            assertThat(doi).isEqualTo("10.1016/j.envres.2018.06.021")
            assertThat(originalAbstract).startsWith(
                    "BACKGROUND: Long-term exposure to ambient fine particulate matter (≤ 2.5 μg/m3 in")
            assertThat(originalAbstract).endsWith(
                    "The concentration -response function produced here can be further applied in the global health risk assessment of air particulate matter.")
            assertThat(originalAbstract).hasSize(2295)
        }
    }

    companion object {
        private const val XML_2539026 = "xml/pubmed_result_25395026.xml"
        private const val XML_23454700 = "xml/pubmed_result_23454700.xml"
        private const val XML_26607712 = "xml/pubmed_result_26607712.xml"
        private const val XML_27214671 = "xml/pubmed_result_27214671.xml"
        private const val XML_27224452 = "xml/pubmed_result_27224452.xml"
        private const val XML_27258721 = "xml/pubmed_result_27258721.xml"
        private const val XML_30124840 = "xml/pubmed_result_30124840.xml"
        private const val XML_29144419 = "xml/pubmed_result_29144419.xml"
        private const val XML_30077140 = "xml/pubmed_result_30077140.xml"
    }
}
