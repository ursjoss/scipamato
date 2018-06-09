package ch.difty.scipamato.publ.persistence.paper;

import static ch.difty.scipamato.publ.db.tables.Paper.PAPER;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import ch.difty.scipamato.common.persistence.AbstractFilterConditionMapper;
import ch.difty.scipamato.common.persistence.FilterConditionMapper;
import ch.difty.scipamato.publ.db.tables.records.PaperRecord;
import ch.difty.scipamato.publ.entity.Code;
import ch.difty.scipamato.publ.entity.PopulationCode;
import ch.difty.scipamato.publ.entity.StudyDesignCode;
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter;

@FilterConditionMapper
public class PublicPaperFilterConditionMapper extends AbstractFilterConditionMapper<PublicPaperFilter> {

    private static final String  RE_QUOTE           = "\"";
    private static final Pattern QUOTED             = Pattern.compile(RE_QUOTE + "([^" + RE_QUOTE + "]+)" + RE_QUOTE);
    private static final int     QUOTED_GROUP_INDEX = 1;

    @Override
    protected void map(final PublicPaperFilter filter, final List<Condition> conditions) {
        if (filter.getNumber() != null) {
            conditions.add(PAPER.NUMBER.eq(filter.getNumber()));
        }

        if (filter.getAuthorMask() != null) {
            addTokenizedConditions(conditions, filter.getAuthorMask(), PAPER.AUTHORS);
        }

        if (filter.getMethodsMask() != null) {
            addTokenizedConditions(conditions, filter.getMethodsMask(), PAPER.METHODS);
        }

        if (filter.getPublicationYearFrom() != null) {
            if (hasNoOrIdenticalPubYearUntil(filter)) {
                conditions.add(PAPER.PUBLICATION_YEAR.eq(filter.getPublicationYearFrom()));
            } else {
                conditions.add(
                    PAPER.PUBLICATION_YEAR.between(filter.getPublicationYearFrom(), filter.getPublicationYearUntil()));
            }
        } else if (filter.getPublicationYearUntil() != null) {
            conditions.add(PAPER.PUBLICATION_YEAR.le(filter.getPublicationYearUntil()));
        }

        if (filter.getPopulationCodes() != null) {
            final Short[] ids = filter
                .getPopulationCodes()
                .stream()
                .map(PopulationCode::getId)
                .toArray(Short[]::new);
            conditions.add(PAPER.CODES_POPULATION.contains(ids));
        }

        if (filter.getStudyDesignCodes() != null) {
            final Short[] ids = filter
                .getStudyDesignCodes()
                .stream()
                .map(StudyDesignCode::getId)
                .toArray(Short[]::new);
            conditions.add(PAPER.CODES_STUDY_DESIGN.contains(ids));
        }

        if (filter.getCodesOfClass1() != null || filter.getCodesOfClass2() != null || filter.getCodesOfClass3() != null
            || filter.getCodesOfClass4() != null || filter.getCodesOfClass5() != null
            || filter.getCodesOfClass6() != null || filter.getCodesOfClass7() != null
            || filter.getCodesOfClass8() != null) {
            final List<String> allCodes = collectAllCodesFrom(filter);
            if (!allCodes.isEmpty())
                conditions.add(codeCondition(allCodes));
        }

    }

    private boolean hasNoOrIdenticalPubYearUntil(final PublicPaperFilter filter) {
        return filter.getPublicationYearUntil() == null || filter
                                                               .getPublicationYearFrom()
                                                               .intValue() == filter
                                                               .getPublicationYearUntil()
                                                               .intValue();
    }

    /*
     * Currently does not allow to mix quoted and unquoted terms. If this will
     * become necessary we might have to implement a proper tokenization of the
     * search term, as was done in core with the SearchTerm hierarchy.
     */
    private void addTokenizedConditions(final List<Condition> conditions, final String mask,
        final TableField<PaperRecord, String> field) {
        final Matcher m = QUOTED.matcher(mask);
        boolean done = tokenizeQuoted(field, m, conditions);
        if (!done)
            tokenizeUnquoted(mask, field, conditions);
    }

    private boolean tokenizeQuoted(final TableField<PaperRecord, String> field, final Matcher m,
        final List<Condition> conditions) {
        String term = null;
        while (m.find()) {
            term = m.group(QUOTED_GROUP_INDEX);
            if (term != null)
                conditions.add(field.likeIgnoreCase("%" + term + "%"));
        }
        return term != null;
    }

    private void tokenizeUnquoted(final String mask, final TableField<PaperRecord, String> field,
        final List<Condition> conditions) {
        if (!mask.contains(" ")) {
            conditions.add(field.likeIgnoreCase("%" + mask + "%"));
        } else {
            for (final String t : mask.split(" ")) {
                final String token = t.trim();
                if (!token.isEmpty())
                    conditions.add(field.likeIgnoreCase("%" + token + "%"));
            }
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
            return codes
                .stream()
                .filter(Objects::nonNull)
                .map(Code::getCode)
                .collect(Collectors.toList());
        }
    }

    /**
     * Due to bug https://github.com/jOOQ/jOOQ/issues/4754, the straightforward way
     * of mapping the codes to the array of type text does not work:
     *
     * <pre>
     * return PAPER.CODES.contains(codeCollection.toArray(new String[codeCollection.size()]));
     * </pre>
     * <p>
     * While I originally casted to PostgresDataType.TEXT, I now need to cast to SQLDataType.CLOB
     * due to https://github.com/jOOQ/jOOQ/issues/7375
     */
    private Condition codeCondition(final List<String> codeCollection) {
        final List<Field<String>> convCodes = codeCollection
            .stream()
            .filter(Objects::nonNull)
            .map(str -> DSL
                .val(str)
                .cast(SQLDataType.CLOB))
            .collect(Collectors.toList());
        return PAPER.CODES.contains(DSL.array(convCodes));
    }

}