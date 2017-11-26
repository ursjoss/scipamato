package ch.difty.scipamato.persistence.paper;

import static ch.difty.scipamato.db.tables.Paper.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.jooq.util.postgres.PostgresDataType;

import ch.difty.scipamato.entity.Code;
import ch.difty.scipamato.entity.PopulationCode;
import ch.difty.scipamato.entity.StudyDesignCode;
import ch.difty.scipamato.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.persistence.AbstractFilterConditionMapper;
import ch.difty.scipamato.persistence.FilterConditionMapper;

@FilterConditionMapper
public class PublicPaperFilterConditionMapper extends AbstractFilterConditionMapper<PublicPaperFilter> {

    @Override
    protected void map(final PublicPaperFilter filter, final List<Condition> conditions) {
        if (filter.getNumber() != null) {
            conditions.add(PAPER.NUMBER.eq(filter.getNumber()));
        }

        if (filter.getAuthorMask() != null) {
            final String likeExpression = "%" + filter.getAuthorMask() + "%";
            conditions.add(PAPER.AUTHORS.likeIgnoreCase(likeExpression));
        }

        if (filter.getMethodsMask() != null) {
            final String likeExpression = "%" + filter.getMethodsMask() + "%";
            conditions.add(PAPER.METHODS.likeIgnoreCase(likeExpression));
        }

        if (filter.getPublicationYearFrom() != null) {
            conditions.add(PAPER.PUBLICATION_YEAR.ge(filter.getPublicationYearFrom()));
        }

        if (filter.getPublicationYearUntil() != null) {
            conditions.add(PAPER.PUBLICATION_YEAR.le(filter.getPublicationYearUntil()));
        }

        if (filter.getPopulationCodes() != null) {
            final Short[] ids = filter.getPopulationCodes().stream().map(PopulationCode::getId).toArray(Short[]::new);
            conditions.add(PAPER.CODES_POPULATION.contains(ids));
        }

        if (filter.getStudyDesignCodes() != null) {
            final Short[] ids = filter.getStudyDesignCodes().stream().map(StudyDesignCode::getId).toArray(Short[]::new);
            conditions.add(PAPER.CODES_STUDY_DESIGN.contains(ids));
        }

        if (filter.getCodesOfClass1() != null || filter.getCodesOfClass2() != null || filter.getCodesOfClass3() != null || filter.getCodesOfClass4() != null || filter.getCodesOfClass5() != null
                || filter.getCodesOfClass6() != null || filter.getCodesOfClass7() != null || filter.getCodesOfClass8() != null) {
            final List<String> allCodes = collectAllCodesFrom(filter);
            if (!allCodes.isEmpty())
                conditions.add(codeCondition(allCodes));
        }

    }

    private List<String> collectAllCodesFrom(final PublicPaperFilter filter) {
        final List<String> codes = new ArrayList<>();
        codes.addAll(getCodesOf(filter.getCodesOfClass1()));
        codes.addAll(getCodesOf(filter.getCodesOfClass2()));
        codes.addAll(getCodesOf(filter.getCodesOfClass3()));
        codes.addAll(getCodesOf(filter.getCodesOfClass4()));
        codes.addAll(getCodesOf(filter.getCodesOfClass5()));
        codes.addAll(getCodesOf(filter.getCodesOfClass6()));
        codes.addAll(getCodesOf(filter.getCodesOfClass7()));
        codes.addAll(getCodesOf(filter.getCodesOfClass8()));
        return codes;
    }

    private Collection<? extends String> getCodesOf(List<Code> codes) {
        if (codes == null)
            return Collections.emptyList();
        else {
            return codes.stream().filter(Objects::nonNull).map(Code::getCode).collect(Collectors.toList());
        }
    }

    /**
     * Due to bug https://github.com/jOOQ/jOOQ/issues/4754, the straightforward way of mapping the codes to the
     * array of type text does not work:
     *
     * <pre>
     * return PAPER.CODES.contains(codeCollection.toArray(new String[codeCollection.size()]));
     * </pre>
     */
    private Condition codeCondition(final List<String> codeCollection) {
        final List<Field<String>> convCodes = codeCollection.stream().filter(Objects::nonNull).map(str -> DSL.val(str).cast(PostgresDataType.TEXT)).collect(Collectors.toList());
        return PAPER.CODES.contains(DSL.array(convCodes));
    }

}