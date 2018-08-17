package ch.difty.scipamato.core.persistence.newsletter;

import static ch.difty.scipamato.core.db.tables.Language.LANGUAGE;
import static ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC;
import static ch.difty.scipamato.core.db.tables.NewsletterTopicTr.NEWSLETTER_TOPIC_TR;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.common.TranslationUtils;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.Sort;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicTranslation;

@Repository
@Slf4j
public class JooqNewsletterTopicRepo implements NewsletterTopicRepository {

    private final DSLContext dslContext;

    public JooqNewsletterTopicRepo(@Qualifier("dslContext") final DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public List<NewsletterTopic> findAll(final String languageCode) {
        final String lang = TranslationUtils.trimLanguageCode(languageCode);
        // skipping the audit fields
        return dslContext
            .select(NEWSLETTER_TOPIC.ID.as("NT_ID"), DSL
                .coalesce(NEWSLETTER_TOPIC_TR.TITLE, TranslationUtils.NOT_TRANSL)
                .as("NT_TITLE"))
            .from(NEWSLETTER_TOPIC)
            .leftOuterJoin(NEWSLETTER_TOPIC_TR)
            .on(NEWSLETTER_TOPIC.ID
                .equal(NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID)
                .and(NEWSLETTER_TOPIC_TR.LANG_CODE.equal(lang)))
            .orderBy(NEWSLETTER_TOPIC_TR.TITLE)
            .fetchInto(NewsletterTopic.class);
    }

    @Override
    public List<NewsletterTopicDefinition> findPageOfNewsletterTopicDefinitions(final NewsletterTopicFilter filter,
        final PaginationContext pc) {
        final SelectOnConditionStep<Record> selectStep = dslContext
            .select(NEWSLETTER_TOPIC.fields())
            .select(LANGUAGE.CODE)
            .select(NEWSLETTER_TOPIC_TR.fields())
            .from(NEWSLETTER_TOPIC)
            .crossJoin(LANGUAGE)
            .leftOuterJoin(NEWSLETTER_TOPIC_TR)
            .on(NEWSLETTER_TOPIC.ID.eq(NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID))
            .and(LANGUAGE.CODE.eq(NEWSLETTER_TOPIC_TR.LANG_CODE));
        final SelectConditionStep<Record> selectConditionStep = applyWhereCondition(filter, selectStep);
        // the subsequent grouping requires ordering by id then language_code
        final Map<Integer, Result<Record>> rawRecords = selectConditionStep
            .orderBy(NEWSLETTER_TOPIC.ID, LANGUAGE.CODE, NEWSLETTER_TOPIC_TR.TITLE)
            .fetchGroups(NEWSLETTER_TOPIC.ID);
        final List<NewsletterTopicDefinition> results = mapRawRecordsIntoNewsletterTopicDefinitions(rawRecords);
        considerSorting(pc, results);
        // need to page after sorting due to grouping, not profiting from DB filtering :-(
        return results
            .stream()
            .skip((long) pc.getOffset())
            .limit((long) pc.getPageSize())
            .collect(toList());
    }

    private <R extends Record> SelectConditionStep<R> applyWhereCondition(final NewsletterTopicFilter filter,
        final SelectJoinStep<R> selectStep) {
        if (filter != null && filter.getTitleMask() != null) {
            final String titleMask = filter.getTitleMask();
            if ("n.a.".equals(titleMask))
                return selectStep.where(NEWSLETTER_TOPIC.ID.in(DSL
                    .selectDistinct(NEWSLETTER_TOPIC.ID)
                    .from(NEWSLETTER_TOPIC)
                    .crossJoin(LANGUAGE)
                    .leftOuterJoin(NEWSLETTER_TOPIC_TR)
                    .on(NEWSLETTER_TOPIC.ID.eq(NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID))
                    .and(LANGUAGE.CODE.eq(NEWSLETTER_TOPIC_TR.LANG_CODE))
                    .where(NEWSLETTER_TOPIC_TR.TITLE.isNull())));
            else
                return selectStep.where(NEWSLETTER_TOPIC.ID.in(DSL
                    .selectDistinct(NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID)
                    .from(NEWSLETTER_TOPIC_TR)
                    .where(NEWSLETTER_TOPIC_TR.TITLE.likeIgnoreCase('%' + titleMask + '%'))));
        } else {
            return selectStep.where(DSL.trueCondition());
        }
    }

    private List<NewsletterTopicDefinition> mapRawRecordsIntoNewsletterTopicDefinitions(
        final Map<Integer, Result<Record>> rawRecords) {
        final List<NewsletterTopicDefinition> definitions = new ArrayList<>();
        final String mainLanguage = getMainLanguage();
        for (final Map.Entry<Integer, Result<Record>> entry : rawRecords.entrySet()) {
            final List<NewsletterTopicTranslation> translations = entry
                .getValue()
                .stream()
                .map(r -> new NewsletterTopicTranslation(r.getValue(NEWSLETTER_TOPIC_TR.ID), r.getValue(LANGUAGE.CODE),
                    r.getValue(NEWSLETTER_TOPIC_TR.TITLE), r.getValue(NEWSLETTER_TOPIC_TR.VERSION)))
                .collect(toList());
            Record r = entry
                .getValue()
                .stream()
                .findFirst()
                .orElseThrow();
            definitions.add(
                new NewsletterTopicDefinition(entry.getKey(), mainLanguage, r.getValue(NEWSLETTER_TOPIC.VERSION),
                    translations.toArray(new NewsletterTopicTranslation[translations.size()])));
        }
        return definitions;
    }

    @Override
    public String getMainLanguage() {
        return dslContext
            .select(LANGUAGE.CODE)
            .from(LANGUAGE)
            .where(LANGUAGE.MAIN_LANGUAGE.eq(true))
            .fetchOneInto(String.class);
    }

    @Override
    public NewsletterTopicDefinition newUnpersistedNewsletterTopicDefinition() {
        final List<NewsletterTopicTranslation> translations = dslContext
            .select(LANGUAGE.CODE)
            .from(LANGUAGE)
            .fetchInto(String.class)
            .stream()
            .map(lc -> new NewsletterTopicTranslation(null, lc, null, 0))
            .collect(toList());
        return new NewsletterTopicDefinition(null, getMainLanguage(), 0,
            translations.toArray(new NewsletterTopicTranslation[translations.size()]));
    }

    /**
     * Currently only accepting title as sort. It's a bit hacky...
     */
    private void considerSorting(final PaginationContext pc, final List<NewsletterTopicDefinition> results) {
        for (final Sort.SortProperty sortProperty : pc.getSort()) {
            final String propName = sortProperty.getName();
            if (propName.equals(NEWSLETTER_TOPIC_TR.TITLE.getName())) {
                if (sortProperty.getDirection() == Sort.Direction.DESC)
                    results.sort(Comparator
                        .comparing(NewsletterTopicDefinition::getTranslationsAsString)
                        .reversed());
                else
                    results.sort(Comparator.comparing(NewsletterTopicDefinition::getTranslationsAsString));
            }
        }
    }

    @Override
    public int countByFilter(final NewsletterTopicFilter filter) {
        final SelectJoinStep<Record1<Integer>> selectStep = dslContext
            .selectCount()
            .from(NEWSLETTER_TOPIC);
        return applyWhereCondition(filter, selectStep).fetchOneInto(Integer.class);
    }

}
