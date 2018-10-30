package ch.difty.scipamato.core.persistence.paper;

import static ch.difty.scipamato.core.db.tables.Paper.PAPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.common.persistence.FilterConditionMapperTest;
import ch.difty.scipamato.common.persistence.GenericFilterConditionMapper;
import ch.difty.scipamato.core.db.tables.Paper;
import ch.difty.scipamato.core.db.tables.records.PaperRecord;
import ch.difty.scipamato.core.entity.search.PaperFilter;

public class PaperFilterConditionMapperTest
    extends FilterConditionMapperTest<PaperRecord, ch.difty.scipamato.core.db.tables.Paper, PaperFilter> {

    private final PaperFilterConditionMapper mapper = new PaperFilterConditionMapper();

    private final PaperFilter filter = new PaperFilter();

    @Override
    protected Paper getTable() {
        return PAPER;
    }

    @Override
    protected GenericFilterConditionMapper<PaperFilter> getMapper() {
        return mapper;
    }

    @Override
    protected PaperFilter getFilter() {
        return filter;
    }

    @Test
    public void creatingWhereCondition_withNumber_searchesNumber() {
        Long number = 17L;
        filter.setNumber(number);
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase("\"PUBLIC\".\"PAPER\".\"NUMBER\" = 17");
    }

    @Test
    public void creatingWhereCondition_withAuthorMask_searchesFirstAuthorAndAuthors() {
        String pattern = "am";
        filter.setAuthorMask(pattern);
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase(makeWhereClause(pattern, "FIRST_AUTHOR", "AUTHORS"));
    }

    @Test
    public void creatingWhereCondition_withMethodsMask_searchesExposureAndMethodFields() {
        String pattern = "m";
        filter.setMethodsMask(pattern);
        // @formatter:off
        assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase(makeWhereClause(pattern
                , "EXPOSURE_POLLUTANT"
                , "EXPOSURE_ASSESSMENT"
                , "METHODS"
                , "METHOD_STUDY_DESIGN"
                , "METHOD_OUTCOME"
                , "METHOD_STATISTICS"
                , "METHOD_CONFOUNDERS"
        ));
        // @formatter:on
    }

    @Test
    public void creatingWhereCondition_withSearchMask_searchesRemainingTextFields() {
        String pattern = "foo";
        filter.setSearchMask(pattern);
        // @formatter:off
        assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase(makeWhereClause(pattern
                , "DOI"
                , "LOCATION"
                , "TITLE"
                , "GOALS"
                , "POPULATION"
                , "POPULATION_PLACE"
                , "POPULATION_PARTICIPANTS"
                , "POPULATION_DURATION"
                , "RESULT"
                , "RESULT_EXPOSURE_RANGE"
                , "RESULT_EFFECT_ESTIMATE"
                , "RESULT_MEASURED_OUTCOME"
                , "CONCLUSION"
                , "COMMENT"
                , "INTERN"
                , "ORIGINAL_ABSTRACT"
        ));
        // @formatter:on
    }

    @Test
    public void creatingWhereCondition_withPublicationYearFrom_searchesPublicationYear() {
        filter.setPublicationYearFrom(2016);
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase("\"PUBLIC\".\"PAPER\".\"PUBLICATION_YEAR\" >= 2016");
    }

    @Test
    public void creatingWhereCondition_withPublicationYearUntil_searchesPublicationYear() {
        filter.setPublicationYearUntil(2016);
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase("\"PUBLIC\".\"PAPER\".\"PUBLICATION_YEAR\" <= 2016");
    }

    @Test
    public void creatingWhereCondition_withNewsletterId() {
        filter.setNewsletterId(10);
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase(
            //@formatter:off
                "exists (\n" +
                "  select 1 \"one\"\n" +
                "  from \"public\".\"paper_newsletter\"\n" +
                "  where (\n" +
                "    \"public\".\"paper_newsletter\".\"paper_id\" = \"public\".\"paper\".\"id\"\n" +
                "    and \"public\".\"paper_newsletter\".\"newsletter_id\" = 10\n" +
                "  )\n" +
                ")"
            //@formatter:on
        );
    }

}
