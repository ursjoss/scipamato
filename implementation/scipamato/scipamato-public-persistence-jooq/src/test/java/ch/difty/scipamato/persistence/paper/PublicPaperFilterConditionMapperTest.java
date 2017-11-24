package ch.difty.scipamato.persistence.paper;

import static ch.difty.scipamato.db.tables.Paper.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.Test;

import ch.difty.scipamato.db.tables.Paper;
import ch.difty.scipamato.db.tables.records.PaperRecord;
import ch.difty.scipamato.entity.Code;
import ch.difty.scipamato.entity.PopulationCode;
import ch.difty.scipamato.entity.StudyDesignCode;
import ch.difty.scipamato.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.persistence.FilterConditionMapperTest;
import ch.difty.scipamato.persistence.GenericFilterConditionMapper;
import ch.difty.scipamato.persistence.paper.PublicPaperFilterConditionMapper;

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

    @Test
    public void creatingWhereCondition_withPopulationCodes_searchesPopulationCodes() {
        filter.setPopulationCodes(Arrays.asList(PopulationCode.CHILDREN));
        assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase("\"PUBLIC\".\"PAPER\".\"CODES_POPULATION\" @> array[1]");
    }

    @Test
    public void creatingWhereCondition_withMethodStudyDesignCodes_searchesStudyDesignCodes() {
        filter.setStudyDesignCodes(Arrays.asList(StudyDesignCode.EPIODEMIOLOGICAL, StudyDesignCode.OVERVIEW_METHODOLOGY));
        assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase("\"PUBLIC\".\"PAPER\".\"CODES_STUDY_DESIGN\" @> array[2, 3]");
    }

    @Test
    public void creatingWhereCondition_withCodesOfCodeClass1_searchesCodeClasses() {
        filter.setCodesOfClass1(Arrays.asList(Code.builder().code("c1").build(), Code.builder().code("c2").build()));
        assertBasicCodeMappingC1C2();
    }

    private void assertBasicCodeMappingC1C2() {
        // Due to bug https://github.com/jOOQ/jOOQ/issues/4754
        // assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase("\"PUBLIC\".\"PAPER\".\"CODES\" @> array['c1', 'c2']");
        // @formatter:off
        assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase(
                "\"public\".\"paper\".\"codes\" @> array[\n" +
                "  cast('c1' as text), \n" +
                "  cast('c2' as text)\n" +
                "]");
        // @formatter:on
    }

    @Test
    public void creatingWhereCondition_withCodesOfCodeClass2_searchesCodeClasses() {
        filter.setCodesOfClass2(Arrays.asList(Code.builder().code("c1").build(), Code.builder().code("c2").build()));
        assertBasicCodeMappingC1C2();
    }

    @Test
    public void creatingWhereCondition_withCodesOfCodeClass3_searchesCodeClasses() {
        filter.setCodesOfClass3(Arrays.asList(Code.builder().code("c1").build(), Code.builder().code("c2").build()));
        assertBasicCodeMappingC1C2();
    }

    @Test
    public void creatingWhereCondition_withCodesOfCodeClass4_searchesCodeClasses() {
        filter.setCodesOfClass4(Arrays.asList(Code.builder().code("c1").build(), Code.builder().code("c2").build()));
        assertBasicCodeMappingC1C2();
    }

    @Test
    public void creatingWhereCondition_withCodesOfCodeClass5_searchesCodeClasses() {
        filter.setCodesOfClass5(Arrays.asList(Code.builder().code("c1").build(), Code.builder().code("c2").build()));
        assertBasicCodeMappingC1C2();
    }

    @Test
    public void creatingWhereCondition_withCodesOfCodeClass6_searchesCodeClasses() {
        filter.setCodesOfClass6(Arrays.asList(Code.builder().code("c1").build(), Code.builder().code("c2").build()));
        assertBasicCodeMappingC1C2();
    }

    @Test
    public void creatingWhereCondition_withCodesOfCodeClass7_searchesCodeClasses() {
        filter.setCodesOfClass7(Arrays.asList(Code.builder().code("c1").build(), Code.builder().code("c2").build()));
        assertBasicCodeMappingC1C2();
    }

    @Test
    public void creatingWhereCondition_withCodesOfCodeClass8_searchesCodeClasses() {
        filter.setCodesOfClass8(Arrays.asList(Code.builder().code("c1").build(), Code.builder().code("c2").build()));
        assertBasicCodeMappingC1C2();
    }

    @Test
    public void creatingWhereCondition_withCodesOfAllCodeClasses_searchesCodeClasses() {
        filter.setCodesOfClass1(Arrays.asList(Code.builder().code("1A").build()));
        filter.setCodesOfClass2(Arrays.asList(Code.builder().code("2B").build()));
        filter.setCodesOfClass3(Arrays.asList(Code.builder().code("3C").build()));
        filter.setCodesOfClass4(Arrays.asList(Code.builder().code("4D").build()));
        filter.setCodesOfClass5(Arrays.asList(Code.builder().code("5E").build()));
        filter.setCodesOfClass6(Arrays.asList(Code.builder().code("6F").build()));
        filter.setCodesOfClass7(Arrays.asList(Code.builder().code("7G").build()));
        filter.setCodesOfClass8(Arrays.asList(Code.builder().code("8H").build()));
        // Due to bug https://github.com/jOOQ/jOOQ/issues/4754
        // assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase("\"PUBLIC\".\"PAPER\".\"CODES\" @> array['1A', '2B', '3C', '4D', '5E', '6F', '7G', '8H']");
        // @formatter:off
        assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase(
                "\"public\".\"paper\".\"codes\" @> array[\n" +
                "  cast('1A' as text), \n" +
                "  cast('2B' as text), \n" +
                "  cast('3C' as text), \n" +
                "  cast('4D' as text), \n" +
                "  cast('5E' as text), \n" +
                "  cast('6F' as text), \n" +
                "  cast('7G' as text), \n" +
                "  cast('8H' as text)\n" +
                "]");
        // @formatter:on
    }

}
