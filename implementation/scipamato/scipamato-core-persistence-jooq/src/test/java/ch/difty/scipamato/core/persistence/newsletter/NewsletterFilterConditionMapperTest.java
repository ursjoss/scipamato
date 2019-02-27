package ch.difty.scipamato.core.persistence.newsletter;

import static ch.difty.scipamato.core.db.tables.Newsletter.NEWSLETTER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.common.persistence.FilterConditionMapperTest;
import ch.difty.scipamato.common.persistence.GenericFilterConditionMapper;
import ch.difty.scipamato.core.db.tables.Newsletter;
import ch.difty.scipamato.core.db.tables.records.NewsletterRecord;
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;

public class NewsletterFilterConditionMapperTest
    extends FilterConditionMapperTest<NewsletterRecord, Newsletter, NewsletterFilter> {

    private final NewsletterFilterConditionMapper mapper = new NewsletterFilterConditionMapper();

    private final NewsletterFilter filter = new NewsletterFilter();

    @Override
    protected Newsletter getTable() {
        return NEWSLETTER;
    }

    @Override
    protected GenericFilterConditionMapper<NewsletterFilter> getMapper() {
        return mapper;
    }

    @Override
    protected NewsletterFilter getFilter() {
        return filter;
    }

    @Test
    public void creatingWhereCondition_withIssueMask_searchesFirstIssue() {
        String pattern = "im";
        filter.setIssueMask(pattern);
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase(makeWhereClause(pattern, "ISSUE"));
    }

    @Test
    public void creatingWhereCondition_withPublicationStatus() {
        filter.setPublicationStatus(PublicationStatus.CANCELLED);
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase("\"public\".\"newsletter\".\"publication_status\" = -1");
    }

    @Test
    public void creatingWhereCondition_withNewsletterTopic() {
        filter.setNewsletterTopic(new NewsletterTopic(5, "foo"));
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase(
            //@formatter:off
                  "\"public\".\"newsletter\".\"id\" in (\n"
                + "  select \"public\".\"newsletter_newsletter_topic\".\"newsletter_id\"\n"
                + "  from \"public\".\"newsletter_newsletter_topic\"\n"
                + "  where (\n"
                + "    \"public\".\"newsletter_newsletter_topic\".\"newsletter_id\" = \"public\".\"newsletter\".\"id\"\n"
                + "    and \"public\".\"newsletter_newsletter_topic\".\"newsletter_topic_id\" = 5\n"
                + "  )\n" + ")");
            //@formatter:on
    }
}