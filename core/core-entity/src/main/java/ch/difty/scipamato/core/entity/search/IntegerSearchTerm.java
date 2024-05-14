package ch.difty.scipamato.core.entity.search;

import java.util.Arrays;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of {@link AbstractSearchTerm} working with Integer fields. The
 * following {@link MatchType}s are implemented:
 *
 * <ul>
 * <li><b>GREATER_THAN:</b> {@code  >2014 }</li>
 * <li><b>GREATER_OR_EQUAL:</b> {@code  >=2014 }</li>
 * <li><b>EXACT:</b> {@code  2014 } or {@code  =2014 }</li>
 * <li><b>LESS_OR_EQUAL:</b> {@code <=2014 }</li>
 * <li><b>LESS_THAN:</b> {@code <2014 }</li>
 * <li><b>RANGE:</b> {@code  2014-2017 }</li>
 * <li><b>MISSING:</b> the field has no value.</li>
 * <li><b>PRESENT:</b> the field has any value.</li>
 * <li><b>INCOMPLETE:</b> the search term ist not complete yet (partial entry).</li>
 * </ul>
 * <p>
 * All rawValues and their individual parts are trimmed, so the following
 * examples are equally valid:
 *
 * <ul>
 * <li>{@code  > 2014 }</li>
 * <li>{@code <= 2014 }</li>
 * <li>{@code  2014  - 2017 }</li>
 * </ul>
 * <p>
 * NOTE: The INCOMPLETE will not find any papers. It's more about gracefully handling
 * unfinished search terms. See https://github.com/ursjoss/scipamato/issues/84
 *
 * @author u.joss
 */
public class IntegerSearchTerm extends AbstractSearchTerm {
    @java.io.Serial
    private static final long serialVersionUID = 1L;

    private static final int COMP_SYMBOL_MAX_LENGTH = 2;

    public enum MatchType {
        EXACT,
        GREATER_THAN,
        GREATER_OR_EQUAL,
        LESS_THAN,
        LESS_OR_EQUAL,
        RANGE,
        MISSING,
        PRESENT,
        INCOMPLETE
    }

    private enum Ops {
        LESS_THAN("<"),
        LESS_THAN_OR_EQUAL("<="),
        EQUAL("="),
        GREATER_THAN_OR_EQUAL(">="),
        GREATER_THAN(">"),
        RANGE("-");

        private final String symbol;

        Ops(final String symbol) {
            this.symbol = symbol;
        }
    }

    private final MatchType type;
    private final int       value;
    private final int       value2;

    IntegerSearchTerm(@NotNull final String fieldName, @NotNull final String rawSearchTerm) {
        this(null, fieldName, rawSearchTerm);
    }

    IntegerSearchTerm(@Nullable final Long searchConditionId, @NotNull final String fieldName,
        @NotNull final String rawSearchTerm) {
        this(null, searchConditionId, fieldName, rawSearchTerm);
    }

    IntegerSearchTerm(@Nullable final Long id, @Nullable final Long searchConditionId, @NotNull final String fieldName,
        @NotNull final String rawSearchTerm) {
        super(id, SearchTermType.INTEGER, searchConditionId, fieldName, rawSearchTerm);
        final String rst = rawSearchTerm.trim();
        if (rst.startsWith(Ops.RANGE.symbol) || rst.endsWith(Ops.RANGE.symbol) || Arrays
            .stream(Ops.values())
            .anyMatch(op -> op.symbol.equals(rst))) {
            this.type = MatchType.INCOMPLETE;
            this.value = 0;
            this.value2 = 0;
        } else if ("=\"\"".equals(rst) || "\"\"".equals(rst)) {
            this.type = MatchType.MISSING;
            this.value = 0;
            this.value2 = 0;
        } else if (">\"\"".equals(rst)) {
            this.type = MatchType.PRESENT;
            this.value = 0;
            this.value2 = 0;
        } else if (rst.startsWith(Ops.GREATER_THAN_OR_EQUAL.symbol)) {
            this.type = MatchType.GREATER_OR_EQUAL;
            this.value = extractInteger(rst, COMP_SYMBOL_MAX_LENGTH);
            this.value2 = this.value;
        } else if (rst.startsWith(Ops.GREATER_THAN.symbol)) {
            this.type = MatchType.GREATER_THAN;
            this.value = extractInteger(rst, 1);
            this.value2 = this.value;
        } else if (rst.startsWith(Ops.LESS_THAN_OR_EQUAL.symbol)) {
            this.type = MatchType.LESS_OR_EQUAL;
            this.value = extractInteger(rst, COMP_SYMBOL_MAX_LENGTH);
            this.value2 = this.value;
        } else if (rst.startsWith(Ops.LESS_THAN.symbol)) {
            this.type = MatchType.LESS_THAN;
            this.value = extractInteger(rst, 1);
            this.value2 = this.value;
        } else if (rst.startsWith(Ops.EQUAL.symbol)) {
            this.type = MatchType.EXACT;
            this.value = extractInteger(rst, 1);
            this.value2 = this.value;
        } else if (rst.contains(Ops.RANGE.symbol)) {
            final String symbol = Ops.RANGE.symbol;
            final int indexOfSymbol = rst.indexOf(symbol);
            this.type = MatchType.RANGE;
            this.value = Integer.parseInt(rst
                .substring(0, indexOfSymbol)
                .trim());
            this.value2 = Integer.parseInt(rst
                .substring(indexOfSymbol + 1)
                .trim());
        } else if (isNumeric(rst.trim())) {
            this.value = Integer.parseInt(rst.trim());
            this.value2 = this.value;
            this.type = MatchType.EXACT;
        } else {
            this.type = MatchType.INCOMPLETE;
            this.value = 1; // arbitrary value to make it different from the first block and mute the intellij warning)
            this.value2 = 1;
        }
    }

    private boolean isNumeric(String rst) {
        try {
            Integer.parseInt(rst.trim());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private int extractInteger(final String rst, final int compSymbolMaxLength) {
        return Integer.parseInt(rst
            .substring(compSymbolMaxLength)
            .trim());
    }

    public int getValue() {
        return value;
    }

    public int getValue2() {
        return value2;
    }

    @NotNull
    public MatchType getType() {
        return type;
    }
}
