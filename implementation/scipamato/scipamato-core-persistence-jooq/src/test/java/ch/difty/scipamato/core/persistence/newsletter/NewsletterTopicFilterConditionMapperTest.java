package ch.difty.scipamato.core.persistence.newsletter;

import static ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC;

import ch.difty.scipamato.common.persistence.FilterConditionMapperTest;
import ch.difty.scipamato.common.persistence.GenericFilterConditionMapper;
import ch.difty.scipamato.core.db.tables.NewsletterTopic;
import ch.difty.scipamato.core.db.tables.records.NewsletterTopicRecord;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;

public class NewsletterTopicFilterConditionMapperTest
    extends FilterConditionMapperTest<NewsletterTopicRecord, NewsletterTopic, NewsletterTopicFilter> {

    private final NewsletterTopicFilterConditionMapper mapper = new NewsletterTopicFilterConditionMapper();

    private final NewsletterTopicFilter filter = new NewsletterTopicFilter();

    @Override
    protected NewsletterTopic getTable() {
        return NEWSLETTER_TOPIC;
    }

    @Override
    protected GenericFilterConditionMapper<NewsletterTopicFilter> getMapper() {
        return mapper;
    }

    @Override
    protected NewsletterTopicFilter getFilter() {
        return filter;
    }
}