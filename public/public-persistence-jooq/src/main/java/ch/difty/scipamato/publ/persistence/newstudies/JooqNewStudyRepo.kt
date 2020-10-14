package ch.difty.scipamato.publ.persistence.newstudies

import ch.difty.scipamato.publ.db.tables.NewStudy
import ch.difty.scipamato.publ.db.tables.NewsletterTopic
import ch.difty.scipamato.publ.entity.NewStudyPageLink
import ch.difty.scipamato.publ.entity.NewStudyTopic
import ch.difty.scipamato.publ.entity.Newsletter
import org.jooq.DSLContext
import org.jooq.Field
import org.jooq.Record
import org.jooq.Record13
import org.jooq.Result
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.sql.Date
import java.sql.Timestamp

@Repository
class JooqNewStudyRepo(private val dsl: DSLContext) : NewStudyRepository {

    override fun findNewStudyTopicsForNewsletter(newsletterId: Int, languageCode: String): List<NewStudyTopic> {
        // References to aliased tables
        val newStudyTopicTable = ch.difty.scipamato.publ.db.tables.NewStudyTopic.NEW_STUDY_TOPIC.`as`("nst")
        val newsletterTopicTable = NewsletterTopic.NEWSLETTER_TOPIC.`as`("nt")
        val newStudyTable = NewStudy.NEW_STUDY.`as`("ns")

        // References to aliased table fields (to avoid ambiguous field names
        val newStudyTopicSortField = DSL.coalesce(newStudyTopicTable.SORT, -1).`as`("nstSort")
        val newStudySortField = newStudyTable.SORT.`as`("nsSort")
        val titleOrBlankField = DSL.coalesce(newsletterTopicTable.TITLE, "")

        // Fetch the result set (Grouped NewStudyTopics with associated NewStudies)
        val fetch = dsl
            .select(
                newStudyTopicSortField,
                titleOrBlankField,
                newStudyTable.NEWSLETTER_ID,
                newStudyTable.NEWSLETTER_TOPIC_ID,
                newStudySortField,
                newStudyTable.PAPER_NUMBER,
                newStudyTable.YEAR,
                newStudyTable.AUTHORS,
                newStudyTable.HEADLINE,
                newStudyTable.DESCRIPTION,
                newStudyTable.VERSION,
                newStudyTable.CREATED,
                newStudyTable.LAST_MODIFIED
            )
            .from(newStudyTable)
            .leftOuterJoin(newStudyTopicTable)
            .on(newStudyTable.NEWSLETTER_ID.eq(newStudyTopicTable.NEWSLETTER_ID)
                .and(newStudyTable.NEWSLETTER_TOPIC_ID.eq(newStudyTopicTable.NEWSLETTER_TOPIC_ID)))
            .leftOuterJoin(newsletterTopicTable)
            .on(newStudyTopicTable.NEWSLETTER_TOPIC_ID.eq(newsletterTopicTable.ID))
            .where(newStudyTable.NEWSLETTER_ID.eq(newsletterId))
            .and(DSL.coalesce(newsletterTopicTable.LANG_CODE, languageCode).eq(languageCode))
            .orderBy(
                newStudyTopicSortField,
                titleOrBlankField,
                newStudySortField,
                newStudyTable.DESCRIPTION
            )
            .fetch()
        val resultSet = fetch.intoGroups(arrayOf<Field<*>>(newStudyTopicSortField, titleOrBlankField))
        return processDbRecords(
            map = resultSet,
            titleOrBlankField = titleOrBlankField,
            nstSort = newStudyTopicSortField,
            ns = newStudyTable,
            nsSort = newStudySortField
        )
    }

    /*
     * Walks through the recordset and extracts the NewStudyTopic with all associated NewStudies
     */
    private fun processDbRecords(
        map: Map<Record, Result<Record13<Int, String, Int, Int, Int, Long, Int, String, String, String, Int, Timestamp, Timestamp>>>,
        titleOrBlankField: Field<String>, nstSort: Field<Int>,
        ns: NewStudy, nsSort: Field<Int>,
    ): List<NewStudyTopic> =
        map.map { (r, value) -> newNewStudyTopic(r.get(nstSort), r.get(titleOrBlankField), value, ns, nsSort) }

    /**
     * Creates a new newStudyTopic including the associated NewStudies as list.
     *
     * @param newStudyTopicSortValue
     * the sort value of the NewStudyTopic to be created
     * @param newStudyTopicTitleValue
     * the title value of the NewStudyTopic to be created
     * @param newStudyRecords
     * the database resultset of the newStudyRecord including NewStudy fields
     * @param newStudyTable
     * database table representation of the NewStudy table. Required to access the fields.
     * @param aliasedNewsStudySortField
     * the aliased NewStudy.Sort field, the value must not be null
     * @return single NewStudyTopic
     */
    private fun newNewStudyTopic(
        newStudyTopicSortValue: Int, newStudyTopicTitleValue: String,
        newStudyRecords: Result<Record13<Int, String, Int, Int, Int, Long, Int, String, String, String, Int, Timestamp, Timestamp>>,
        newStudyTable: NewStudy,
        aliasedNewsStudySortField: Field<Int>,
    ): NewStudyTopic {
        val map = newStudyRecords.map { r: Record13<Int, String, Int, Int, Int, Long, Int, String, String, String, Int, Timestamp, Timestamp> ->
            val sort = r.getValue(aliasedNewsStudySortField)
            ch.difty.scipamato.publ.entity.NewStudy(sort, r.getValue(newStudyTable.PAPER_NUMBER), r.getValue(newStudyTable.YEAR),
                r.getValue(newStudyTable.AUTHORS), r.getValue(newStudyTable.HEADLINE),
                r.getValue(newStudyTable.DESCRIPTION))
        }
        return NewStudyTopic(newStudyTopicSortValue, newStudyTopicTitleValue, map.filterNotNull())
    }

    override fun findMostRecentNewsletterId(): Int? = dsl
        .select(ch.difty.scipamato.publ.db.tables.Newsletter.NEWSLETTER.ID)
        .from(ch.difty.scipamato.publ.db.tables.Newsletter.NEWSLETTER)
        .orderBy(ch.difty.scipamato.publ.db.tables.Newsletter.NEWSLETTER.ISSUE_DATE.desc())
        .limit(1)
        .fetchOneInto(Int::class.java)

    override fun findIdOfNewsletterWithIssue(issue: String): Int? = dsl
        .select(ch.difty.scipamato.publ.db.tables.Newsletter.NEWSLETTER.ID)
        .from(ch.difty.scipamato.publ.db.tables.Newsletter.NEWSLETTER)
        .where(ch.difty.scipamato.publ.db.tables.Newsletter.NEWSLETTER.ISSUE.eq(issue))
        .fetchOneInto(Int::class.java)

    override fun findArchivedNewsletters(
        newsletterCount: Int,
        languageCode: String,
    ): List<Newsletter> = dsl
        .select(
            ch.difty.scipamato.publ.db.tables.Newsletter.NEWSLETTER.ID,
            ch.difty.scipamato.publ.db.tables.Newsletter.NEWSLETTER.ISSUE,
            ch.difty.scipamato.publ.db.tables.Newsletter.NEWSLETTER.ISSUE_DATE
        )
        .from(ch.difty.scipamato.publ.db.tables.Newsletter.NEWSLETTER)
        .orderBy(ch.difty.scipamato.publ.db.tables.Newsletter.NEWSLETTER.ISSUE_DATE.desc())
        .limit(newsletterCount)
        .map { r -> Newsletter(r[0] as Int, r[1] as String?, (r[2] as Date?)?.toLocalDate()) }

    override fun findNewStudyPageLinks(languageCode: String): List<NewStudyPageLink> = dsl
        .select(
            ch.difty.scipamato.publ.db.tables.NewStudyPageLink.NEW_STUDY_PAGE_LINK.LANG_CODE,
            ch.difty.scipamato.publ.db.tables.NewStudyPageLink.NEW_STUDY_PAGE_LINK.SORT,
            ch.difty.scipamato.publ.db.tables.NewStudyPageLink.NEW_STUDY_PAGE_LINK.TITLE,
            ch.difty.scipamato.publ.db.tables.NewStudyPageLink.NEW_STUDY_PAGE_LINK.URL
        ).from(ch.difty.scipamato.publ.db.tables.NewStudyPageLink.NEW_STUDY_PAGE_LINK)
        .where(ch.difty.scipamato.publ.db.tables.NewStudyPageLink.NEW_STUDY_PAGE_LINK.LANG_CODE.eq(languageCode))
        .orderBy(
            ch.difty.scipamato.publ.db.tables.NewStudyPageLink.NEW_STUDY_PAGE_LINK.SORT,
            ch.difty.scipamato.publ.db.tables.NewStudyPageLink.NEW_STUDY_PAGE_LINK.TITLE
        ).map { r -> NewStudyPageLink(r[0] as String?, r[1] as Int?, r[2] as String?, r[3] as String?) }
}
