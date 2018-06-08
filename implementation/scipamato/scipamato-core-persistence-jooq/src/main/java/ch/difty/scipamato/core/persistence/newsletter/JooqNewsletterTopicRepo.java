package ch.difty.scipamato.core.persistence.newsletter;

import static ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC;
import static ch.difty.scipamato.core.db.tables.NewsletterTopicTr.NEWSLETTER_TOPIC_TR;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.common.TranslationUtils;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;

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
            .fetchInto(NewsletterTopic.class);
    }

}
