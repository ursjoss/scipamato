package ch.difty.sipamato.entity.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of {@link SearchTerm} working with the two audit fields createdDisplayValue and lastModifiedDisplayValue,
 * both of which handle both the user and the date of the create or last change.
 * <p>
 * There are different {@link TokenType}s, each of which is able to lex particular elements of the raw search terms string.
 * <p>
 * Every token type implements a particular {@link MatchType} which will be relevant for building
 * up the search logic for the particular token. The different {@link TokenType}s of one particular
 * {@link MatchType} are required to parse and lex parts of search terms with slightly different syntax,
 * e.g. whether a string part is quoted or not. However, when evaluating the list of tokens, we can
 * safely ignore the different types and simply distinguish the {@link MatchType} to apply the concrete
 * search logic to the database.
 * <p>
 * Each token returned by the class offers the lexed <code>rawData</code>, an sql-ized form of it already
 * containing the wild-card indicator (%) for appropriate.
 * <p>
 * The tokens targeted for the user fields, could be of match type: 
 * <ul>
 * <li> <b>CONTAINS:</b> searchterm contained within field, e.g. <code>createdBy like '%foo%'</code> </code> </li>
 * <li> <b>NONE:</b> dummy category which will be ignored.</li>
 * </ul>
 * <p>
 * The date fields may either contain a complete time stamp in the format <code>yyyy-MM-dd hh:mm:ss</code> or only
 * the date part <code>yyyy-MM-dd</code>. In the latter case, the date will be completed with a time part, depending
 * on the match type with ' 23:59:59' or with ' 00:00:00'.
 * <ul>
 * <li> <b>EQUALS:</b> exact search, e.g. <code>field = 'foo'</code> (dates are completed with ' 00:00:00') </li>
 * <li> <b>GREATER_THAN:</b> date is after the specified date (dates are completed with ' 23:59:59') </li>
 * <li> <b>GREATER_OR_EQUAL:</b> the date is at or after the specified date (dates are completed with ' 00:00:00')</li>
 * <li> <b>LESS_THAN:</b> the date is before the specified date (dates are completed with ' 00:00:00')</li>
 * <li> <b>LESS_OR_EQUAL:</b> the date is at or before the specified date(dates are completed with ' 23:59:59')</li>
 * <li> <b>NONE:</b> dummy category which will be ignored.</li>
 * </ul>
 * <p>
 * The lexing logic was strongly inspired from one of Gio Carlo Cielos blog posts about lexing with capturing-groups (see link below).
 * <p>
 * @author u.joss
 *
 * @see http://giocc.com/writing-a-lexer-in-java-1-7-using-regex-named-capturing-groups.html
 */
public class AuditSearchTerm extends SearchTerm<AuditSearchTerm> {

    private static final long serialVersionUID = 1L;

    private static final String USER_FIELD_TAG = "_BY";

    private static final String RE_DATE = "\\d{4}-\\d{2}-\\d{2}(?: \\d{2}:\\d{2}:\\d{2})?";
    private static final String RE_QUOTE = "\"";

    private final List<Token> tokens;

    AuditSearchTerm(final String fieldName, final String rawSearchTerm) {
        this(null, fieldName, rawSearchTerm);
    }

    AuditSearchTerm(final Long searchConditionId, final String fieldName, final String rawSearchTerm) {
        this(null, searchConditionId, fieldName, rawSearchTerm);
    }

    AuditSearchTerm(final Long id, final Long searchConditionId, final String fieldName, final String rawSearchTerm) {
        super(id, SearchTermType.AUDIT, searchConditionId, fieldName, rawSearchTerm);
        tokens = lex(rawSearchTerm.trim(), isUserTypeSearchTerm(), isDateTypeSearchTerm());

    }

    protected boolean isUserTypeSearchTerm() {
        return getFieldName().toUpperCase().endsWith(USER_FIELD_TAG);
    }

    protected boolean isDateTypeSearchTerm() {
        return !isUserTypeSearchTerm();
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public enum FieldType {
        USER,
        DATE,
        NONE;
    }

    public enum MatchType {
        EQUALS,
        CONTAINS,
        GREATER_THAN,
        GREATER_OR_EQUAL,
        LESS_THAN,
        LESS_OR_EQUAL,
        NONE;
    }

    public enum TokenType {
        // Token types must not have underscores. Otherwise the named capturing groups in the constructed regexes will break
        WHITESPACE(RE_S + "+", MatchType.NONE, FieldType.NONE, 1),

        GREATEROREQUALQUOTED(">=" + RE_QUOTE + "(" + RE_DATE + ")" + RE_QUOTE, MatchType.GREATER_OR_EQUAL, FieldType.DATE, 3),
        GREATEROREQUAL(">=" + "(" + RE_DATE + ")", MatchType.GREATER_OR_EQUAL, FieldType.DATE, 5),
        GREATERTHANQUOTED(">" + RE_QUOTE + "(" + RE_DATE + ")" + RE_QUOTE, MatchType.GREATER_THAN, FieldType.DATE, 7),
        GREATERTHAN(">" + "(" + RE_DATE + ")", MatchType.GREATER_THAN, FieldType.DATE, 9),
        LESSOREQUALQUOTED("<=" + RE_QUOTE + "(" + RE_DATE + ")" + RE_QUOTE, MatchType.LESS_OR_EQUAL, FieldType.DATE, 11),
        LESSOREQUAL("<=" + "(" + RE_DATE + ")", MatchType.LESS_OR_EQUAL, FieldType.DATE, 13),
        LESSTHANQUOTED("<" + RE_QUOTE + "(" + RE_DATE + ")" + RE_QUOTE, MatchType.LESS_THAN, FieldType.DATE, 15),
        LESSTHAN("<" + "(" + RE_DATE + ")", MatchType.LESS_THAN, FieldType.DATE, 17),
        EXACTQUOTED("=?" + RE_QUOTE + "(" + RE_DATE + ")" + RE_QUOTE, MatchType.EQUALS, FieldType.DATE, 19),
        EXACT("=?" + "(" + RE_DATE + ")", MatchType.EQUALS, FieldType.DATE, 21),

        WORD("\\b(" + RE_WW2 + ")\\b", MatchType.CONTAINS, FieldType.USER, 23),

        RAW("", MatchType.NONE, FieldType.NONE, 24);

        public final String pattern;
        public final MatchType matchType;
        public final FieldType fieldType;
        private final int group;

        private TokenType(final String pattern, final MatchType matchType, final FieldType fieldType, final int group) {
            this.pattern = pattern;
            this.group = group;
            this.fieldType = fieldType;
            this.matchType = matchType;
        }

        public static List<TokenType> byMatchType(final MatchType mt) {
            final List<TokenType> types = new ArrayList<>();
            for (final TokenType tt : values()) {
                if (tt.matchType == mt) {
                    types.add(tt);
                }
            }
            return types;
        }

    }

    public static class Token implements Serializable {
        private static final long serialVersionUID = 1L;

        private final TokenType type;
        private final Map<FieldType, String> rawDataMap = new EnumMap<>(FieldType.class);
        private final Map<FieldType, String> sqlDataMap = new EnumMap<>(FieldType.class);

        public Token(final TokenType type, final String data) {
            this.type = type;
            this.rawDataMap.put(type.fieldType, data);
            this.sqlDataMap.put(type.fieldType, sqlize(completeDateTimeIfNecessary(type, data)));
        }

        private String completeDateTimeIfNecessary(final TokenType type, final String data) {
            if (type.fieldType != FieldType.DATE || data.length() != 10) {
                return data;
            } else {
                if (type.matchType == MatchType.GREATER_THAN || type.matchType == MatchType.LESS_OR_EQUAL)
                    return data + " 23:59:59";
                else
                    return data + " 00:00:00";
            }
        }

        private String sqlize(String data) {
            final StringBuilder sb = new StringBuilder();
            switch (type.matchType) {
            default:
                sb.append(data);
            }
            return sb.toString();
        }

        public TokenType getType() {
            return type;
        }

        public String getUserRawData() {
            return rawDataMap.get(FieldType.USER);
        }

        public String getDateRawData() {
            return rawDataMap.get(FieldType.DATE);
        }

        public String getUserSqlData() {
            return sqlDataMap.get(FieldType.USER);
        }

        public String getDateSqlData() {
            return sqlDataMap.get(FieldType.DATE);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            for (final Entry<FieldType, String> e : sqlDataMap.entrySet()) {
                sb.append("(").append(e.getKey()).append(" ").append(type.name()).append(" ").append(e.getValue()).append(")");
            }
            return sb.toString();
        }
    }

    protected static List<Token> lex(final String input, final boolean isUserType, final boolean isDateType) {
        return tokenize(input, buildPattern(), isUserType, isDateType);
    }

    private static Pattern buildPattern() {
        final StringBuilder tokenPatternBuilder = new StringBuilder();
        for (final TokenType tokenType : TokenType.values())
            if (tokenType != TokenType.RAW)
                tokenPatternBuilder.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
        return Pattern.compile(tokenPatternBuilder.substring(1));
    }

    private static List<Token> tokenize(final String input, final Pattern pattern, final boolean isUserType, final boolean isDateType) {
        final List<Token> tokens = new ArrayList<Token>();
        final Matcher matcher = pattern.matcher(input);
        tokenIteration: while (matcher.find()) {
            for (final TokenType tk : TokenType.values()) {
                if (tk == TokenType.RAW)
                    continue;
                if (matcher.group(TokenType.WHITESPACE.name()) != null)
                    continue;
                else if (matcher.group(tk.name()) != null) {
                    if (isDateRelevant(isDateType, tk) || isUserRelevant(isUserType, tk)) {
                        tokens.add(new Token(tk, matcher.group(tk.group)));
                    }
                    continue tokenIteration;
                }
            }
        }
        return tokens;
    }

    /**
     * Both the field and the token are of type user -> relevant for the user fields created_by, last_modified_by
     */
    private static boolean isUserRelevant(final boolean isUserType, final TokenType tk) {
        return tk.fieldType == FieldType.USER && isUserType;
    }

    /**
     * Both the field and the token are of type date -> relevant for the date fields created, last_modified
     */
    private static boolean isDateRelevant(final boolean isDateType, final TokenType tk) {
        return tk.fieldType == FieldType.DATE && isDateType;
    }

}
