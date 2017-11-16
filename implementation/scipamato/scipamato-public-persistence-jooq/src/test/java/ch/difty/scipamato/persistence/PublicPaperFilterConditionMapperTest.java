package ch.difty.scipamato.persistence;

import static ch.difty.scipamato.db.tables.Paper.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import ch.difty.scipamato.db.tables.Paper;
import ch.difty.scipamato.db.tables.records.PaperRecord;
import ch.difty.scipamato.entity.filter.PublicPaperFilter;

public class PublicPaperFilterConditionMapperTest extends FilterConditionMapperTest<PaperRecord, ch.difty.scipamato.db.tables.Paper, PublicPaperFilter> {

    private final PublicPaperFilterConditionMapper mapper = new PublicPaperFilterConditionMapper();

    private final PublicPaperFilter filter = new PublicPaperFilter();

    @Override
    protected Paper getTable() {
        return PAPER;
    }

    @Override
    protected GenericFilterConditionMapper<PublicPaperFilter> getMapper() {
        return mapper;
    }

    @Override
    protected PublicPaperFilter getFilter() {
        return filter;
    }

    @Test
    public void creatingWhereCondition_withNumber_searchesNumber() {
        Long number = 17l;
        filter.setNumber(number);
        assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase("\"PUBLIC\".\"PAPER\".\"NUMBER\" = 17");
    }

    @Test
    public void creatingWhereCondition_withAuthorMask_searchesFirstAuthorAndAuthors() {
        String pattern = "am";
        filter.setAuthorMask(pattern);
        assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase(makeWhereClause(pattern, "AUTHORS"));
    }

    @Test
    public void creatingWhereCondition_withMethodsMask_searchesExposureAndMethodFields() {
        String pattern = "m";
        filter.setMethodsMask(pattern);
        assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase(makeWhereClause(pattern, "METHODS"));
    }

    @Test
    public void creatingWhereCondition_withPublicationYearFrom_searchesPublicationYear() {
        filter.setPublicationYearFrom(2016);
        assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase("\"PUBLIC\".\"PAPER\".\"PUBLICATION_YEAR\" >= 2016");
    }

    @Test
    public void creatingWhereCondition_withPublicationYearUntil_searchesPublicationYear() {
        filter.setPublicationYearUntil(2016);
        assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase("\"PUBLIC\".\"PAPER\".\"PUBLICATION_YEAR\" <= 2016");
    }
}
