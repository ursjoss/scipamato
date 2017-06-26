package ch.difty.scipamato.entity.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of {@link SearchTerm} working with String fields. The search term (applying to one particular field)
 * is lexed and transferred into one or more {@link Token}s.
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
 * Each token returned by the class offers the lexed {@code rawData}, an sql-ized form of it already
 * containing the wild-card indicator (%) as well as an indication of whether the search applies positively
 * (e.g. {@code like 'foo%'}) or negatively ({@code not like 'foo%'}).
 * <p>
 * The following {@link MatchType}s are implemented (using pseudo-code examples):
 * <ul>
 * <li> <b>EQUALS:</b> exact search, e.g. {@code field = 'foo'} or {@code field != 'foo'} </li>
 * <li> <b>CONTAINS:</b> search term contained within field, e.g. {@code field like '%foo%'} or {@code field not like '%foo%'} </li>
 * <li> <b>LIKE:</b> positive match with wildcards, e.g. {@code field like '%foo%'} or {@code field not like '%foo%'}</li>
 * <li> <b>REGEX:</b> full regex search, e.g. {@code field regexp_like 's/fo{1,2}/'} or {@code field ! regexp_like 's/fo{1,2}/'}</li>
 * <li> <b>LENGTH:</b> field has content, e.g. {@code length(field) > 0} or field is empty {@code length(field) = 0}</li>
 * <li> <b>NONE:</b> dummy category which will be ignored.</li>
 * </ul>
 * <p>
 * The lexing logic was strongly inspired from one of Gio Carlo Cielos blog posts about lexing with capturing-groups (see link below).
 * <p>
 * @author u.joss
 *
 * @see <a href="http://giocc.com/writing-a-lexer-in-java-1-7-using-regex-named-capturing-groups.html">http://giocc.com/writing-a-lexer-in-java-1-7-using-regex-named-capturing-groups.html</a>
 */
public class StringSearchTerm extends SearchTerm {
    private static final long serialVersionUID = 1L;

    private static final String RE_NOT = "-";
    private static final String RE_QUOTE = "\"";
    private static final String RE_AST = "\\*";
    private static final String RE_FOO = "[^\"* ]+";
    private static final String RE_FOO2 = "[^\"*]+";
    private static final String RE_WC = "%";

    private final List<Token> tokens;

    StringSearchTerm(final String fieldName, final String rawSearchTerm) {
        this(null, fieldName, rawSearchTerm);
    }

    StringSearchTerm(final Long searchConditionId, final String fieldName, final String rawSearchTerm) {
        this(null, searchConditionId, fieldName, rawSearchTerm);
    }

    StringSearchTerm(final Long id, final Long searchConditionId, final String fieldName, final String rawSearchTerm) {
        super(id, SearchTermType.STRING, searchConditionId, fieldName, rawSearchTerm);
        tokens = lex(rawSearchTerm.trim());
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public enum MatchType {
        EQUALS,
        CONTAINS,
        LIKE,
        REGEX,
        LENGTH,
        NONE;
    }

    public enum TokenType {
        // Token types must not have underscores. Otherwise the named capturing groups in the constructed regexes will break
        NOTREGEX("-s/(.+)/", MatchType.REGEX, 2, false, false, true),
        REGEX("s/(.+)/", MatchType.REGEX, 4, false, false, false),
        WHITESPACE(RE_S + "+", MatchType.NONE, 5, false, false, false),
        SOME(">\"\"", MatchType.LENGTH, 6, false, false, false),
        EMPTY("=\"\"", MatchType.LENGTH, 7, false, false, true),
        NOTOPENLEFTRIGHTQUOTED(RE_NOT + RE_QUOTE + RE_AST + "(" + RE_FOO2 + ")" + RE_AST + RE_QUOTE, MatchType.LIKE, 9, true, true, true),
        OPENLEFTRIGHTQUOTED(RE_QUOTE + RE_AST + "(" + RE_FOO2 + ")" + RE_AST + RE_QUOTE, MatchType.LIKE, 11, true, true, false),
        NOTOPENLEFTRIGHT(RE_NOT + RE_AST + "\\b(" + RE_FOO + ")\\b" + RE_AST, MatchType.LIKE, 13, true, true, true),
        OPENLEFTRIGHT(RE_AST + "\\b(" + RE_FOO + ")\\b" + RE_AST, MatchType.LIKE, 15, true, true, false),
        NOTOPENRIGHTQUOTED(RE_NOT + RE_QUOTE + "(" + RE_FOO2 + ")" + RE_AST + RE_QUOTE, MatchType.LIKE, 17, false, true, true),
        OPENRIGHTQUOTED(RE_QUOTE + "(" + RE_FOO2 + ")" + RE_AST + RE_QUOTE, MatchType.LIKE, 19, false, true, false),
        NOTOPENRIGHT(RE_NOT + "\\b(" + RE_FOO + ")\\b" + RE_AST, MatchType.LIKE, 21, false, true, true),
        OPENRIGHT("\\b(" + RE_FOO + ")\\b" + RE_AST, MatchType.LIKE, 23, false, true, false),
        NOTOPENLEFTQUOTED(RE_NOT + RE_QUOTE + RE_AST + "(" + RE_FOO2 + ")" + RE_QUOTE, MatchType.LIKE, 25, true, false, true),
        OPENLEFTQUOTED(RE_QUOTE + RE_AST + "(" + RE_FOO2 + ")" + RE_QUOTE, MatchType.LIKE, 27, true, false, false),
        NOTOPENLEFT(RE_NOT + RE_AST + "\\b(" + RE_FOO + ")\\b", MatchType.LIKE, 29, true, false, true),
        OPENLEFT(RE_AST + "\\b(" + RE_FOO + ")\\b", MatchType.LIKE, 31, true, false, false),
        NOTQUOTED(RE_NOT + RE_QUOTE + "([^" + RE_QUOTE + "]+)" + RE_QUOTE, MatchType.EQUALS, 33, false, false, true),
        QUOTED(RE_QUOTE + "([^" + RE_QUOTE + "]+)" + RE_QUOTE, MatchType.EQUALS, 35, false, false, false),
        NOTWORD(RE_NOT + "\\b(" + RE_WW2 + "\\b)", MatchType.CONTAINS, 37, false, false, true),
        WORD("\\b(" + RE_WW2 + ")\\b", MatchType.CONTAINS, 39, false, false, false),
        RAW("", MatchType.NONE, 41, false, false, false);

        public final String pattern;
        public final MatchType matchType;
        private final int group;
        private final boolean wcLeft;
        private final boolean wcRight;
        public final boolean negate;

        private TokenType(final String pattern, final MatchType matchType, final int group, final boolean wcLeft, final boolean wcRight, final boolean negate) {
            this.pattern = pattern;
            this.group = group;
            this.matchType = matchType;
            this.wcLeft = wcLeft;
            this.wcRight = wcRight;
            this.negate = negate;
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

    /**
     * The token holds the raw and the sql-ized form of the search string.
     * It also indicates whether the search should be inverted ({@code not})
     */
    public static class Token implements Serializable {
        private static final long serialVersionUID = 1L;

        public final TokenType type;
        public final boolean negate;
        public final String rawData;
        public final String sqlData;

        public Token(final TokenType type, final String data) {
            this.type = type;
            this.negate = type.negate;
            this.rawData = data;
            this.sqlData = sqlize(data);
        }

        private String sqlize(String data) {
            final StringBuilder sb = new StringBuilder();
            switch (type.matchType) {
            case REGEX:
                sb.append(data);
                break;
            case LIKE:
                if (type.wcLeft)
                    sb.append(RE_WC);
                sb.append(data);
                if (type.wcRight)
                    sb.append(RE_WC);
                break;
            default:
                sb.append(data);
            }
            return sb.toString();
        }

        public TokenType getType() {
            return type;
        }

        @Override
        public String toString() {
            return String.format("(%s %s)", type.name(), sqlData);
        }
    }

    private static List<Token> lex(final String input) {
        return tokenize(input, buildPattern());
    }

    private static Pattern buildPattern() {
        final StringBuilder tokenPatternBuilder = new StringBuilder();
        for (final TokenType tokenType : TokenType.values())
            if (tokenType != TokenType.RAW)
                tokenPatternBuilder.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
        return Pattern.compile(tokenPatternBuilder.substring(1));
    }

    private static List<Token> tokenize(final String input, final Pattern pattern) {
        final List<Token> tokens = new ArrayList<>();
        final Matcher matcher = pattern.matcher(input);
        tokenIteration: while (matcher.find()) {
            for (final TokenType tk : TokenType.values()) {
                if (tk == TokenType.RAW)
                    continue;
                if (matcher.group(TokenType.WHITESPACE.name()) != null)
                    continue;
                else if (matcher.group(tk.name()) != null) {
                    tokens.add(new Token(tk, matcher.group(tk.group)));
                    continue tokenIteration;
                }
            }
        }
        if (tokens.isEmpty()) {
            tokens.add(new Token(TokenType.RAW, input));
        }
        return tokens;
    }

}
