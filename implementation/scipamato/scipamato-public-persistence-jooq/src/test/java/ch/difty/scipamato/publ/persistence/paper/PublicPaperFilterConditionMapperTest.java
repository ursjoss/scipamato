package ch.difty.scipamato.publ.persistence.paper;

import static ch.difty.scipamato.publ.db.tables.Paper.PAPER;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import ch.difty.scipamato.common.persistence.FilterConditionMapperTest;
import ch.difty.scipamato.common.persistence.GenericFilterConditionMapper;
import ch.difty.scipamato.publ.db.tables.Paper;
import ch.difty.scipamato.publ.db.tables.records.PaperRecord;
import ch.difty.scipamato.publ.entity.Code;
import ch.difty.scipamato.publ.entity.PopulationCode;
import ch.difty.scipamato.publ.entity.StudyDesignCode;
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter;

public class PublicPaperFilterConditionMapperTest
    extends FilterConditionMapperTest<PaperRecord, ch.difty.scipamato.publ.db.tables.Paper, PublicPaperFilter> {

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
        Long number = 17L;
        filter.setNumber(number);
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase("\"PUBLIC\".\"PAPER\".\"NUMBER\" = 17");
    }

    @Test
    public void creatingWhereCondition_withAuthorMask_searchesAuthors() {
        String pattern = "am";
        filter.setAuthorMask(pattern);
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase(makeWhereClause(pattern, "AUTHORS"));
    }

    @Test
    public void creatingWhereCondition_withAuthorMaskHoldingMultipleAuthors_searchesForPapersWithBothAuthorsInAnyOrder() {
        String pattern = "foo  bar";
        filter.setAuthorMask(pattern);
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase(
            // @formatter:off
                "(\n" +
                "  lower(\"public\".\"paper\".\"authors\") like lower('%foo%')\n" +
                "  and lower(\"public\".\"paper\".\"authors\") like lower('%bar%')\n" +
                ")"
        // @formatter:on
        );
    }

    @Test
    public void creatingWhereCondition_withMethodsMask_searchesMethodFields() {
        String pattern = "m";
        filter.setMethodsMask(pattern);
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase(makeWhereClause(pattern, "METHODS"));
    }

    @Test
    public void creatingWhereCondition_withMethodsMaskHoldingMultipleKeywords__searchesMethodFieldsWithAllKeywordsInAnyOrder() {
        String pattern = "m1 m2 m3";
        filter.setMethodsMask(pattern);
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase(
            // @formatter:off
                "(\n" +
                "  lower(\"public\".\"paper\".\"methods\") like lower('%m1%')\n" +
                "  and lower(\"public\".\"paper\".\"methods\") like lower('%m2%')\n" +
                "  and lower(\"public\".\"paper\".\"methods\") like lower('%m3%')\n" +
                ")"
        // @formatter:on
        );
    }

    @Test
    public void creatingWhereCondition_withPublicationYearFrom_anBlankYearUntil_searchesExactPublicationYear() {
        filter.setPublicationYearFrom(2016);
        assertThat(filter.getPublicationYearUntil()).isNull();
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase("\"PUBLIC\".\"PAPER\".\"PUBLICATION_YEAR\" = 2016");
    }

    @Test
    public void creatingWhereCondition_withPublicationYearFrom_andPublicationYearUntil_searchesRange() {
        filter.setPublicationYearFrom(2016);
        filter.setPublicationYearUntil(2017);
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase("\"PUBLIC\".\"PAPER\".\"PUBLICATION_YEAR\" between 2016 and 2017");
    }

    @Test
    public void creatingWhereCondition_withIdenticalPublicationYearFromAndTo_searchesExactPublicationYear() {
        filter.setPublicationYearFrom(2016);
        filter.setPublicationYearUntil(2016);
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase("\"PUBLIC\".\"PAPER\".\"PUBLICATION_YEAR\" = 2016");
    }

    @Test
    public void creatingWhereCondition_withPublicationYearUntil_searchesUpToPublicationYear() {
        assertThat(filter.getPublicationYearFrom()).isNull();
        filter.setPublicationYearUntil(2016);
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase("\"PUBLIC\".\"PAPER\".\"PUBLICATION_YEAR\" <= 2016");
    }

    @Test
    public void creatingWhereCondition_withPopulationCodes_searchesPopulationCodes() {
        filter.setPopulationCodes(Collections.singletonList(PopulationCode.CHILDREN));
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase("\"PUBLIC\".\"PAPER\".\"CODES_POPULATION\" @> array[1]");
    }

    @Test
    public void creatingWhereCondition_withMethodStudyDesignCodes_searchesStudyDesignCodes() {
        filter.setStudyDesignCodes(
            Arrays.asList(StudyDesignCode.EPIDEMIOLOGICAL, StudyDesignCode.OVERVIEW_METHODOLOGY));
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase("\"PUBLIC\".\"PAPER\".\"CODES_STUDY_DESIGN\" @> array[2, 3]");
    }

    @Test
    public void creatingWhereCondition_withCodesOfCodeClass1_searchesCodeClasses() {
        filter.setCodesOfClass1(Arrays.asList(Code
            .builder()
            .code("c1")
            .build(), Code
            .builder()
            .code("c2")
            .build()));
        assertBasicCodeMappingC1C2();
    }

    private void assertBasicCodeMappingC1C2() {
        // Due to bug https://github.com/jOOQ/jOOQ/issues/4754
        // assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase("\"PUBLIC\".\"PAPER\".\"CODES\"
        // @> array['c1', 'c2']");
        // @formatter:off
        assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase(
                "\"public\".\"paper\".\"codes\" @> array[\n" +
                "  cast('c1' as clob), \n" +
                "  cast('c2' as clob)\n" +
                "]");
        // @formatter:on
    }

    @Test
    public void creatingWhereCondition_withCodesOfCodeClass2_searchesCodeClasses() {
        filter.setCodesOfClass2(Arrays.asList(Code
            .builder()
            .code("c1")
            .build(), Code
            .builder()
            .code("c2")
            .build()));
        assertBasicCodeMappingC1C2();
    }

    @Test
    public void creatingWhereCondition_withCodesOfCodeClass3_searchesCodeClasses() {
        filter.setCodesOfClass3(Arrays.asList(Code
            .builder()
            .code("c1")
            .build(), Code
            .builder()
            .code("c2")
            .build()));
        assertBasicCodeMappingC1C2();
    }

    @Test
    public void creatingWhereCondition_withCodesOfCodeClass4_searchesCodeClasses() {
        filter.setCodesOfClass4(Arrays.asList(Code
            .builder()
            .code("c1")
            .build(), Code
            .builder()
            .code("c2")
            .build()));
        assertBasicCodeMappingC1C2();
    }

    @Test
    public void creatingWhereCondition_withCodesOfCodeClass5_searchesCodeClasses() {
        filter.setCodesOfClass5(Arrays.asList(Code
            .builder()
            .code("c1")
            .build(), Code
            .builder()
            .code("c2")
            .build()));
        assertBasicCodeMappingC1C2();
    }

    @Test
    public void creatingWhereCondition_withCodesOfCodeClass6_searchesCodeClasses() {
        filter.setCodesOfClass6(Arrays.asList(Code
            .builder()
            .code("c1")
            .build(), Code
            .builder()
            .code("c2")
            .build()));
        assertBasicCodeMappingC1C2();
    }

    @Test
    public void creatingWhereCondition_withCodesOfCodeClass7_searchesCodeClasses() {
        filter.setCodesOfClass7(Arrays.asList(Code
            .builder()
            .code("c1")
            .build(), Code
            .builder()
            .code("c2")
            .build()));
        assertBasicCodeMappingC1C2();
    }

    @Test
    public void creatingWhereCondition_withCodesOfCodeClass8_searchesCodeClasses() {
        filter.setCodesOfClass8(Arrays.asList(Code
            .builder()
            .code("c1")
            .build(), Code
            .builder()
            .code("c2")
            .build()));
        assertBasicCodeMappingC1C2();
    }

    @Test
    public void creatingWhereCondition_withCodesOfAllCodeClasses_searchesCodeClasses() {
        filter.setCodesOfClass1(Collections.singletonList(Code
            .builder()
            .code("1A")
            .build()));
        filter.setCodesOfClass2(Collections.singletonList(Code
            .builder()
            .code("2B")
            .build()));
        filter.setCodesOfClass3(Collections.singletonList(Code
            .builder()
            .code("3C")
            .build()));
        filter.setCodesOfClass4(Collections.singletonList(Code
            .builder()
            .code("4D")
            .build()));
        filter.setCodesOfClass5(Collections.singletonList(Code
            .builder()
            .code("5E")
            .build()));
        filter.setCodesOfClass6(Collections.singletonList(Code
            .builder()
            .code("6F")
            .build()));
        filter.setCodesOfClass7(Collections.singletonList(Code
            .builder()
            .code("7G")
            .build()));
        filter.setCodesOfClass8(Collections.singletonList(Code
            .builder()
            .code("8H")
            .build()));
        // Due to bug https://github.com/jOOQ/jOOQ/issues/4754
        // assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase("\"PUBLIC\".\"PAPER\".\"CODES\"
        // @> array['1A', '2B', '3C', '4D', '5E', '6F', '7G', '8H']");
        // @formatter:off
        assertThat(mapper.map(filter).toString()).isEqualToIgnoringCase(
                "\"public\".\"paper\".\"codes\" @> array[\n" +
                "  cast('1A' as clob), \n" +
                "  cast('2B' as clob), \n" +
                "  cast('3C' as clob), \n" +
                "  cast('4D' as clob), \n" +
                "  cast('5E' as clob), \n" +
                "  cast('6F' as clob), \n" +
                "  cast('7G' as clob), \n" +
                "  cast('8H' as clob)\n" +
                "]");
        // @formatter:on
    }

    @Test
    public void creatingWhereCondition_withSetButThenClearedCodes_doesNotFilterByCodes() {
        filter.setCodesOfClass1(new ArrayList<>(Collections.singletonList(Code
            .builder()
            .code("1A")
            .build())));
        filter
            .getCodesOfClass1()
            .clear();
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase("1 = 1");
    }

    @Test
    public void creatingWhereCondition_withAuthorMaskHoldingMultipleQuotedAuthors_searchesForPapersWithBothAuthorsInAnyOrder() {
        String pattern = "\"Last F\" \"Other S\"";
        filter.setAuthorMask(pattern);
        assertThat(mapper
            .map(filter)
            .toString()).isEqualToIgnoringCase(
            // @formatter:off
                "(\n" +
                "  lower(\"public\".\"paper\".\"authors\") like lower('%Last F%')\n" +
                "  and lower(\"public\".\"paper\".\"authors\") like lower('%Other S%')\n" +
                ")"
        // @formatter:on
        );
    }

}
