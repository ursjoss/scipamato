package ch.difty.scipamato.core.persistence.newsletter;

import static ch.difty.scipamato.core.db.tables.Newsletter.NEWSLETTER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.common.persistence.FilterConditionMapperTest;
import ch.difty.scipamato.common.persistence.GenericFilterConditionMapper;
import ch.difty.scipamato.core.db.tables.Newsletter;
import ch.difty.scipamato.core.db.tables.records.NewsletterRecord;
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter;
import ch.difty.scipamato.common.entity.newsletter.PublicationStatus;

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
}