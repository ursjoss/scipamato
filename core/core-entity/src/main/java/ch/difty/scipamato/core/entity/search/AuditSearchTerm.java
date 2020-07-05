package ch.difty.scipamato.core.entity.search;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of {@link AbstractSearchTerm} working with the two audit
 * fields createdDisplayValue and lastModifiedDisplayValue, both of which handle
 * both the user and the date of the create or last change.
 * <p>
 * There are different {@link TokenType}s, each of which is able to lex
 * particular elements of the raw search terms string.
 * <p>
 * Every token type implements a particular {@link MatchType} which will be
 * relevant for building up the search logic for the particular token. The
 * different {@link TokenType}s of one particular {@link MatchType} are required
 * to parse and lex parts of search terms with slightly different syntax, e.g.
 * whether a string part is quoted or not. However, when evaluating the list of
 * tokens, we can safely ignore the different types and simply distinguish the
 * {@link MatchType} to apply the concrete search logic to the database.
 * <p>
 * Each token returned by the class offers the lexed {@code rawData}, an
 * sqlized form of it already containing the wild-card indicator (%) for
 * appropriate.
 * <p>
 * The tokens targeted for the user fields, could be of match type:
 * <ul>
 * <li><b>CONTAINS:</b> search term contained within field, e.g.
 * {@code createdBy like '%foo%'} </li>
 * <li><b>NONE:</b> dummy category which will be ignored.</li>
 * </ul>
 * <p>
 * The date fields may either contain a complete time stamp in the format
 * {@code yyyy-MM-dd hh:mm:ss} or only the date part {@code yyyy-MM-dd}. In the
 * latter case, the date will be complemented with a time part, depending on the
 * match type with ' 23:59:59' or with ' 00:00:00'.
 * <ul>
 * <li><b>EQUALS:</b> exact search, e.g. {@code field = 'foo'} (dates are
 * complemented with ' 00:00:00')</li>
 * <li><b>GREATER_THAN:</b> date is after the specified date (dates are
 * complemented with ' 23:59:59')</li>
 * <li><b>GREATER_OR_EQUAL:</b> the date is at or after the specified date
 * (dates are complemented with ' 00:00:00')</li>
 * <li><b>LESS_THAN:</b> the date is before the specified date (dates are
 * complemented with ' 00:00:00')</li>
 * <li><b>LESS_OR_EQUAL:</b> the date is at or before the specified date (dates
 * are complemented with ' 23:59:59')</li>
 * <li><b>RANGE:</b> the date is between two specified dates (inclusive) (dates
 * without time are complemented with ' 00:00:00' (begin date) or ' 23:59:59'
 * (end date))</li>
 * <li><b>NONE:</b> dummy category which will be ignored.</li>
 * </ul>
 * <p>
 * The lexing logic was strongly inspired from one of Gio Carlo Cielos blog
 * posts about lexing with capturing-groups (see link below).
 * <p>
 *
 * @author u.joss
 * @see <a href=
 *     "http://giocc.com/writing-a-lexer-in-java-1-7-using-regex-named-capturing-groups.html">http://giocc.com/writing-a-lexer-in-java-1-7-using-regex-named-capturing-groups.html</a>
 */
@SuppressWarnings({ "SameParameterValue", "SpellCheckingInspection" })
public class AuditSearchTerm extends AbstractSearchTerm {

    private static final long serialVersionUID = 1L;

    private static final int DATE_TIME_LENGTH = 10;

    private static final String USER_FIELD_TAG = "_BY";

    private static final String RE_DATE      = "\\d{4}-\\d{2}-\\d{2}(?: \\d{2}:\\d{2}:\\d{2})?";
    private static final String RE_QUOTE     = "\"";
    private static final String RE_RANGE_DIV = "-";

    private final List<Token> tokens;

    AuditSearchTerm(@NotNull final String fieldName, @NotNull final String rawSearchTerm) {
        this(null, fieldName, rawSearchTerm);
    }

    private AuditSearchTerm(@Nullable final Long searchConditionId, @NotNull final String fieldName,
        @NotNull final String rawSearchTerm) {
        this(null, searchConditionId, fieldName, rawSearchTerm);
    }

    AuditSearchTerm(@Nullable final Long id, @Nullable final Long searchConditionId, @NotNull final String fieldName,
        @NotNull final String rawSearchTerm) {
        super(id, SearchTermType.AUDIT, searchConditionId, fieldName, rawSearchTerm);
        tokens = lex(rawSearchTerm.trim(), isUserTypeSearchTerm(), isDateTypeSearchTerm());

    }

    private boolean isUserTypeSearchTerm() {
        return getFieldName()
            .toUpperCase()
            .endsWith(USER_FIELD_TAG);
    }

    private boolean isDateTypeSearchTerm() {
        return !isUserTypeSearchTerm();
    }

    @NotNull
    public List<Token> getTokens() {
        return tokens;
    }

    public enum FieldType {
        USER,
        DATE,
        NONE
    }

    public enum MatchType {
        EQUALS,
        CONTAINS,
        GREATER_THAN,
        GREATER_OR_EQUAL,
        RANGE,
        LESS_THAN,
        LESS_OR_EQUAL,
        NONE
    }

    public enum TokenType {
        // Token types must not have underscores. Otherwise the named capturing groups
        // in the constructed regex terms will break
        WHITESPACE(RE_S + "+", MatchType.NONE, FieldType.NONE, 1),

        RANGEQUOTED(
            "=?" + RE_QUOTE + "(" + RE_DATE + ")" + RE_QUOTE + RE_RANGE_DIV + RE_QUOTE + "(" + RE_DATE + ")" + RE_QUOTE,
            MatchType.RANGE, FieldType.DATE, 3),
        RANGE("=?" + "(" + RE_DATE + ")" + RE_RANGE_DIV + "(" + RE_DATE + ")", MatchType.RANGE, FieldType.DATE, 6),
        GREATEROREQUALQUOTED(">=" + RE_QUOTE + "(" + RE_DATE + ")" + RE_QUOTE, MatchType.GREATER_OR_EQUAL,
            FieldType.DATE, 9),
        GREATEROREQUAL(">=" + "(" + RE_DATE + ")", MatchType.GREATER_OR_EQUAL, FieldType.DATE, 11),
        GREATERTHANQUOTED(">" + RE_QUOTE + "(" + RE_DATE + ")" + RE_QUOTE, MatchType.GREATER_THAN, FieldType.DATE, 13),
        GREATERTHAN(">" + "(" + RE_DATE + ")", MatchType.GREATER_THAN, FieldType.DATE, 15),
        LESSOREQUALQUOTED("<=" + RE_QUOTE + "(" + RE_DATE + ")" + RE_QUOTE, MatchType.LESS_OR_EQUAL, FieldType.DATE,
            17),
        LESSOREQUAL("<=" + "(" + RE_DATE + ")", MatchType.LESS_OR_EQUAL, FieldType.DATE, 19),
        LESSTHANQUOTED("<" + RE_QUOTE + "(" + RE_DATE + ")" + RE_QUOTE, MatchType.LESS_THAN, FieldType.DATE, 21),
        LESSTHAN("<" + "(" + RE_DATE + ")", MatchType.LESS_THAN, FieldType.DATE, 23),
        EXACTQUOTED("=?" + RE_QUOTE + "(" + RE_DATE + ")" + RE_QUOTE, MatchType.EQUALS, FieldType.DATE, 25),
        EXACT("=?" + "(" + RE_DATE + ")", MatchType.EQUALS, FieldType.DATE, 27),

        WORD("\\b(" + RE_WW2 + ")\\b", MatchType.CONTAINS, FieldType.USER, 29),

        RAW("", MatchType.NONE, FieldType.NONE, 30);

        // cache values
        private static final TokenType[] TOKEN_TYPES = values();

        final         String    pattern;
        public final  MatchType matchType;
        final         FieldType fieldType;
        private final int       group;

        TokenType(@NotNull final String pattern, @NotNull final MatchType matchType, @NotNull final FieldType fieldType,
            final int group) {
            this.pattern = pattern;
            this.group = group;
            this.fieldType = fieldType;
            this.matchType = matchType;
        }

        @NotNull
        public static List<TokenType> byMatchType(@NotNull final MatchType mt) {
            final List<TokenType> types = new ArrayList<>();
            for (final TokenType tt : TOKEN_TYPES)
                if (tt.matchType == mt)
                    types.add(tt);
            return types;
        }
    }

    public static class Token implements Serializable {
        private static final long serialVersionUID = 1L;

        private final TokenType              type;
        private final Map<FieldType, String> rawDataMap = new EnumMap<>(FieldType.class);
        private final Map<FieldType, String> sqlDataMap = new EnumMap<>(FieldType.class);

        public Token(@NotNull final TokenType type, @NotNull final String data) {
            this.type = type;
            this.rawDataMap.put(type.fieldType, data);
            this.sqlDataMap.put(type.fieldType, completeDateTimeIfNecessary(type, data));
        }

        @NotNull
        private String completeDateTimeIfNecessary(@NotNull final TokenType type, @NotNull final String data) {
            if (type.fieldType != FieldType.DATE || data.length() != DATE_TIME_LENGTH) {
                return data;
            } else {
                if (type.matchType == MatchType.GREATER_THAN || type.matchType == MatchType.LESS_OR_EQUAL)
                    return data + " 23:59:59";
                else
                    return data + " 00:00:00";
            }
        }

        @NotNull
        public TokenType getType() {
            return type;
        }

        @Nullable
        String getUserRawData() {
            return rawDataMap.get(FieldType.USER);
        }

        @Nullable
        String getDateRawData() {
            return rawDataMap.get(FieldType.DATE);
        }

        @Nullable
        public String getUserSqlData() {
            return sqlDataMap.get(FieldType.USER);
        }

        @Nullable
        public String getDateSqlData() {
            return sqlDataMap.get(FieldType.DATE);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            for (final Entry<FieldType, String> e : sqlDataMap.entrySet()) {
                sb
                    .append("(")
                    .append(e.getKey())
                    .append(" ")
                    .append(type.name())
                    .append(" ")
                    .append(e.getValue())
                    .append(")");
            }
            return sb.toString();
        }
    }

    @NotNull
    private static List<Token> lex(@NotNull final String input, final boolean isUserType, final boolean isDateType) {
        return tokenize(input, buildPattern(), isUserType, isDateType);
    }

    private static Pattern buildPattern() {
        final StringBuilder tokenPatternBuilder = new StringBuilder();
        for (final TokenType tokenType : TokenType.TOKEN_TYPES)
            if (tokenType != TokenType.RAW)
                tokenPatternBuilder.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
        return Pattern.compile(tokenPatternBuilder.substring(1));
    }

    private static List<Token> tokenize(final String input, final Pattern pattern, final boolean isUserType,
        final boolean isDateType) {
        return processTokens(pattern.matcher(input), isUserType, isDateType);
    }

    private static List<Token> processTokens(final Matcher matcher, final boolean isUserType,
        final boolean isDateType) {
        final List<Token> tokens = new ArrayList<>();
        while (matcher.find())
            getNextToken(matcher, isUserType, isDateType).ifPresent(tokens::add);
        return tokens;
    }

    private static Optional<Token> getNextToken(final Matcher matcher, final boolean isUserType,
        final boolean isDateType) {
        for (final TokenType tk : TokenType.TOKEN_TYPES) {
            if (tk == TokenType.RAW || matcher.group(TokenType.WHITESPACE.name()) != null)
                continue;
            if (matcher.group(tk.name()) != null) {
                if (isAppropriate(tk, isUserType, isDateType))
                    return Optional.of(new Token(tk, gatherData(matcher, tk)));
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private static String gatherData(final Matcher matcher, final TokenType tk) {
        if (tk == TokenType.RANGEQUOTED || tk == TokenType.RANGE)
            return buildRange(matcher, tk);
        else
            return matcher.group(tk.group);
    }

    private static String buildRange(final Matcher matcher, final TokenType tk) {
        return completeDateTimeIfNecessary(matcher.group(tk.group), RangeElement.BEGIN) + "-"
               + completeDateTimeIfNecessary(matcher.group(tk.group + 1), RangeElement.END);
    }

    private static String completeDateTimeIfNecessary(final String data, final RangeElement rangeElement) {
        if (data.length() != DATE_TIME_LENGTH) {
            return data;
        } else {
            switch (rangeElement) {
            case BEGIN:
                return data + " 00:00:00";
            case END:
            default:
                return data + " 23:59:59";
            }
        }
    }

    private static boolean isAppropriate(final TokenType tk, final boolean isUserType, final boolean isDateType) {
        return isDateRelevant(isDateType, tk) || isUserRelevant(isUserType, tk);
    }

    /**
     * Both the field and the token are of type user -> relevant for the user fields
     * created_by, last_modified_by
     */
    private static boolean isUserRelevant(final boolean isUserType, final TokenType tk) {
        return tk.fieldType == FieldType.USER && isUserType;
    }

    /**
     * Both the field and the token are of type date -> relevant for the date fields
     * created, last_modified
     */
    private static boolean isDateRelevant(final boolean isDateType, final TokenType tk) {
        return tk.fieldType == FieldType.DATE && isDateType;
    }

    private enum RangeElement {
        BEGIN,
        END
    }
}
