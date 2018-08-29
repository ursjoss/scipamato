package ch.difty.scipamato.publ.persistence.newstudies;

import static ch.difty.scipamato.publ.db.tables.NewStudy.NEW_STUDY;
import static ch.difty.scipamato.publ.db.tables.NewStudyPageLink.NEW_STUDY_PAGE_LINK;
import static ch.difty.scipamato.publ.db.tables.NewStudyTopic.NEW_STUDY_TOPIC;
import static ch.difty.scipamato.publ.db.tables.Newsletter.NEWSLETTER;
import static ch.difty.scipamato.publ.db.tables.NewsletterTopic.NEWSLETTER_TOPIC;
import static java.util.stream.Collectors.toList;

import java.sql.Timestamp;
import java.util.*;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.publ.entity.NewStudy;
import ch.difty.scipamato.publ.entity.NewStudyTopic;

@Repository
public class JooqNewStudyRepo implements NewStudyRepository {

    public static final String LANGUAGE_CODE = "languageCode";

    private final DSLContext dsl;

    public JooqNewStudyRepo(final DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public List<NewStudyTopic> findNewStudyTopicsForNewsletter(final int newsletterId, String languageCode) {
        AssertAs.notNull(languageCode, LANGUAGE_CODE);

        // References to aliased tables
        final ch.difty.scipamato.publ.db.tables.NewStudyTopic newStudyTopicTable = NEW_STUDY_TOPIC.as("nst");
        final ch.difty.scipamato.publ.db.tables.NewsletterTopic newsletterTopicTable = NEWSLETTER_TOPIC.as("nt");
        final ch.difty.scipamato.publ.db.tables.NewStudy newStudyTable = NEW_STUDY.as("ns");

        // References to aliased table fields (to avoid ambiguous field names
        final Field<Integer> newStudyTopicSortField = DSL
            .coalesce(newStudyTopicTable.SORT, -1)
            .as("nstSort");
        final Field<Integer> newStudySortField = newStudyTable.SORT.as("nsSort");
        final Field<String> titleOrBlankField = DSL.coalesce(newsletterTopicTable.TITLE, "");

        // Fetch the result set (Grouped NewStudyTopics with associated NewStudies)
        final Result<Record13<Integer, String, Integer, Integer, Integer, Long, Integer, String, String, String, Integer, Timestamp, Timestamp>> fetch = dsl
            .select(newStudyTopicSortField, titleOrBlankField, newStudyTable.NEWSLETTER_ID,
                newStudyTable.NEWSLETTER_TOPIC_ID, newStudySortField, newStudyTable.PAPER_NUMBER, newStudyTable.YEAR,
                newStudyTable.AUTHORS, newStudyTable.HEADLINE, newStudyTable.DESCRIPTION, newStudyTable.VERSION,
                newStudyTable.CREATED, newStudyTable.LAST_MODIFIED)
            .from(newStudyTable)
            .leftOuterJoin(newStudyTopicTable)
            .on(newStudyTable.NEWSLETTER_ID
                .eq(newStudyTopicTable.NEWSLETTER_ID)
                .and(newStudyTable.NEWSLETTER_TOPIC_ID.eq(newStudyTopicTable.NEWSLETTER_TOPIC_ID)))
            .leftOuterJoin(newsletterTopicTable)
            .on(newStudyTopicTable.NEWSLETTER_TOPIC_ID.eq(newsletterTopicTable.ID))
            .where(newStudyTable.NEWSLETTER_ID.eq(newsletterId))
            .and(DSL
                .coalesce(newsletterTopicTable.LANG_CODE, languageCode)
                .eq(languageCode))
            .orderBy(newStudyTopicSortField, titleOrBlankField, newStudySortField, newStudyTable.DESCRIPTION)
            .fetch();
        final Map<Record, Result<Record13<Integer, String, Integer, Integer, Integer, Long, Integer, String, String, String, Integer, Timestamp, Timestamp>>> resultSet = fetch.intoGroups(
            new Field[] { newStudyTopicSortField, titleOrBlankField });

        return processDbRecords(resultSet, titleOrBlankField, newStudyTopicSortField, newStudyTable, newStudySortField);
    }

    /*
     * Walks through the recordset and extracts the NewStudyTopic with all associated NewStudies
     */
    private List<NewStudyTopic> processDbRecords(
        final Map<Record, Result<Record13<Integer, String, Integer, Integer, Integer, Long, Integer, String, String, String, Integer, Timestamp, Timestamp>>> map,
        final Field<String> titleOrBlankField, final Field<Integer> nstSort,
        final ch.difty.scipamato.publ.db.tables.NewStudy ns, final Field<Integer> nsSort) {

        final List<NewStudyTopic> topics = new ArrayList<>();
        for (final Map.Entry<Record, Result<Record13<Integer, String, Integer, Integer, Integer, Long, Integer, String, String, String, Integer, Timestamp, Timestamp>>> entry : map.entrySet()) {
            final Record r = entry.getKey();
            topics.add(newNewStudyTopic(r.get(nstSort), r.get(titleOrBlankField), entry.getValue(), ns, nsSort));
        }
        return topics;
    }

    /**
     * Creates a new newStudyTopic including the associated NewStudies as list.
     *
     * @param newStudyTopicSortValue
     *     the sort value of the NewStudyTopic to be created
     * @param newStudyTopicTitleValue
     *     the title value of the NewStudyTopic to be created
     * @param newStudyRecords
     *     the database resultset of the newStudyRecord including NewStudy fields
     * @param newStudyTable
     *     database table representation of the NewStudy table. Required to access the fields.
     * @param aliasedNewsStudySortField
     *     the aliased NewStudy.Sort field
     * @return single NewStudyTopic
     */
    private NewStudyTopic newNewStudyTopic(final int newStudyTopicSortValue, final String newStudyTopicTitleValue,
        final Result<Record13<Integer, String, Integer, Integer, Integer, Long, Integer, String, String, String, Integer, Timestamp, Timestamp>> newStudyRecords,
        final ch.difty.scipamato.publ.db.tables.NewStudy newStudyTable,
        final Field<Integer> aliasedNewsStudySortField) {
        final List<NewStudy> map = newStudyRecords.map(r -> {
            final Integer sort = r.getValue(aliasedNewsStudySortField);
            return sort != null ?
                new NewStudy(sort, r.getValue(newStudyTable.PAPER_NUMBER), r.getValue(newStudyTable.YEAR),
                    r.getValue(newStudyTable.AUTHORS), r.getValue(newStudyTable.HEADLINE),
                    r.getValue(newStudyTable.DESCRIPTION)) :
                null;
        });
        return new NewStudyTopic(newStudyTopicSortValue, newStudyTopicTitleValue, map
            .stream()
            .filter(Objects::nonNull)
            .collect(toList()));
    }

    @Override
    public Optional<Integer> findMostRecentNewsletterId() {
        return Optional.ofNullable(dsl
            .select(NEWSLETTER.ID)
            .from(NEWSLETTER)
            .orderBy(NEWSLETTER.ISSUE_DATE.desc())
            .limit(1)
            .fetchOneInto(Integer.class));
    }

    @Override
    public Optional<Integer> findIdOfNewsletterWithIssue(final String issue) {
        AssertAs.notNull(issue, "issue");
        return Optional.ofNullable(dsl
            .select(NEWSLETTER.ID)
            .from(NEWSLETTER)
            .where(NEWSLETTER.ISSUE.eq(issue))
            .fetchOneInto(Integer.class));
    }

    @Override
    public List<ch.difty.scipamato.publ.entity.Newsletter> findArchivedNewsletters(final String languageCode) {
        AssertAs.notNull(languageCode, LANGUAGE_CODE);
        return dsl
            .select(NEWSLETTER.ID, NEWSLETTER.ISSUE, NEWSLETTER.ISSUE_DATE)
            .from(NEWSLETTER)
            .orderBy(NEWSLETTER.ISSUE_DATE.desc())
            .limit(14)
            .fetchInto(ch.difty.scipamato.publ.entity.Newsletter.class);
    }

    @Override
    public List<ch.difty.scipamato.publ.entity.NewStudyPageLink> findNewStudyPageLinks(final String languageCode) {
        AssertAs.notNull(languageCode, LANGUAGE_CODE);
        return dsl
            .select(NEW_STUDY_PAGE_LINK.LANG_CODE, NEW_STUDY_PAGE_LINK.SORT, NEW_STUDY_PAGE_LINK.TITLE,
                NEW_STUDY_PAGE_LINK.URL)
            .from(NEW_STUDY_PAGE_LINK)
            .where(NEW_STUDY_PAGE_LINK.LANG_CODE.eq(languageCode))
            .orderBy(NEW_STUDY_PAGE_LINK.SORT, NEW_STUDY_PAGE_LINK.TITLE)
            .fetchInto(ch.difty.scipamato.publ.entity.NewStudyPageLink.class);
    }
}
