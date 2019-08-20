package ch.difty.scipamato.core.persistence.paper.searchorder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.core.entity.search.SearchTerm;
import ch.difty.scipamato.core.entity.search.SearchTermType;
import ch.difty.scipamato.core.entity.search.StringSearchTerm;
import ch.difty.scipamato.core.entity.search.StringSearchTerm.Token;
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType;

@ExtendWith(MockitoExtension.class)
class StringSearchTermEvaluatorTest extends SearchTermEvaluatorTest<StringSearchTerm> {

    private final StringSearchTermEvaluator e = new StringSearchTermEvaluator();

    private final List<Token> tokens = new ArrayList<>();

    @Mock
    private StringSearchTerm stMock;

    @Override
    protected StringSearchTermEvaluator getEvaluator() {
        return e;
    }

    //region:normalField
    private void expectToken(TokenType type, String term) {
        when(stMock.getFieldName()).thenReturn("field_x");
        tokens.add(new Token(type, term));
        when(stMock.getTokens()).thenReturn(tokens);
    }

    @Test
    void buildingConditionForNotRegex_appliesNotRegex() {
        expectToken(TokenType.NOTREGEX, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(
            //@formatter:off
                "not(coalesce(\n" +
                "  field_x, \n" +
                "  ''\n" +
                ") like_regex 'foo')"
            //@formatter:on
        );
    }

    @Test
    void buildingConditionForRegex_appliesRegex() {
        expectToken(TokenType.REGEX, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(
            //@formatter:off
                "coalesce(\n" +
                "  field_x, \n" +
                "  ''\n" +
                ") like_regex 'foo'"
            //@formatter:on
        );
    }

    @Test
    void buildingConditionForWhitespace_appliesTrueCondition() {
        expectToken(TokenType.WHITESPACE, "   ");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("1 = 1");
    }

    @Test
    void buildingConditionForSome_appliesNotEmpty() {
        expectToken(TokenType.SOME, "whatever");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(
            // @formatter:off
            "(\n" +
            "  field_x is not null\n" +
            "  and char_length(cast(field_x as varchar)) > 0\n" +
            ")"
            // @formatter:on
        );
    }

    @Test
    void buildingConditionForEmpty_appliesEmpty() {
        expectToken(TokenType.EMPTY, "whatever");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(
            // @formatter:off
            "(\n" +
            "  field_x is null\n" +
            "  or char_length(cast(field_x as varchar)) = 0\n" +
            ")"
            // @formatter:on
        );
    }

    @Test
    void buildingConditionForNotOpenLeftRightQuoted_appliesLike() {
        expectToken(TokenType.NOTOPENLEFTRIGHTQUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(
            //@formatter:off
                "lower(cast(coalesce(\n" +
                "  field_x, \n" +
                "  ''\n" +
                ") as varchar)) not like lower('%foo%')"
            //@formatter:on
        );
    }

    @Test
    void buildingConditionForOpenLeftRightQuoted_appliesLike() {
        expectToken(TokenType.OPENLEFTRIGHTQUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("lower(cast(field_x as varchar)) like lower('%foo%')");
    }

    @Test
    void buildingConditionForNotOpenLeftRight_appliesNotLike() {
        expectToken(TokenType.NOTOPENLEFTRIGHT, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(
            //@formatter:off
                "lower(cast(coalesce(\n" +
                "  field_x, \n" +
                "  ''\n" +
                ") as varchar)) not like lower('%foo%')"
            //@formatter:on
        );
    }

    @Test
    void buildingConditionForOpenLeftRight_appliesLike() {
        expectToken(TokenType.OPENLEFTRIGHT, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("lower(cast(field_x as varchar)) like lower('%foo%')");
    }

    @Test
    void buildingConditionForNotOpenRightQuoted_appliesLike() {
        expectToken(TokenType.NOTOPENRIGHTQUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(
            //@formatter:off
                "lower(cast(coalesce(\n" +
                "  field_x, \n" +
                "  ''\n" +
                ") as varchar)) not like lower('foo%')"
            //@formatter:on
        );
    }

    @Test
    void buildingConditionForOpenRightQuoted_appliesLike() {
        expectToken(TokenType.OPENRIGHTQUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("lower(cast(field_x as varchar)) like lower('foo%')");
    }

    @Test
    void buildingConditionForNotOpenRight_appliesNotLike() {
        expectToken(TokenType.NOTOPENRIGHT, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(
            //@formatter:off
                "lower(cast(coalesce(\n" +
                "  field_x, \n" +
                "  ''\n" +
                ") as varchar)) not like lower('foo%')"
            //@formatter:on
        );
    }

    @Test
    void buildingConditionForOpenRight_appliesLike() {
        expectToken(TokenType.OPENRIGHT, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("lower(cast(field_x as varchar)) like lower('foo%')");
    }

    @Test
    void buildingConditionForNotOpenLeftQuoted_appliesLike() {
        expectToken(TokenType.NOTOPENLEFTQUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(
            //@formatter:off
                "lower(cast(coalesce(\n" +
                "  field_x, \n" +
                "  ''\n" +
                ") as varchar)) not like lower('%foo')"
            //@formatter:on
        );
    }

    @Test
    void buildingConditionForOpenLeftQuoted_appliesLike() {
        expectToken(TokenType.OPENLEFTQUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("lower(cast(field_x as varchar)) like lower('%foo')");
    }

    @Test
    void buildingConditionForNotOpenLeft_appliesNotLike() {
        expectToken(TokenType.NOTOPENLEFT, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(
            //@formatter:off
                "lower(cast(coalesce(\n" +
                "  field_x, \n" +
                "  ''\n" +
                ") as varchar)) not like lower('%foo')"
            //@formatter:on
        );
    }

    @Test
    void buildingConditionForOpenLeft_appliesLike() {
        expectToken(TokenType.OPENLEFT, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("lower(cast(field_x as varchar)) like lower('%foo')");
    }

    @Test
    void buildingConditionForNotQuoted_appliesUnequal() {
        expectToken(TokenType.NOTQUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("lower(cast(field_x as varchar)) <> lower('foo')");
    }

    @Test
    void buildingConditionForQuoted_appliesEqual() {
        expectToken(TokenType.QUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("lower(cast(field_x as varchar)) = lower('foo')");
    }

    @Test
    void buildingConditionForNotWord_appliesNotContains() {
        expectToken(TokenType.NOTWORD, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            // @formatter:off
            "not(lower(cast(coalesce(",
            "  field_x, ",
            "  ''",
            ") as varchar)) like lower(('%' || replace(",
            "  replace(",
            "    replace(",
            "      'foo', ",
            "      '!', ",
            "      '!!'",
            "    ), ",
            "    '%', ",
            "    '!%'",
            "  ), ",
            "  '_', ",
            "  '!_'",
            ") || '%')) escape '!')"
            // @formatter:on
        ));
    }

    @Test
    void buildingConditionForWord_appliesContains() {
        expectToken(TokenType.WORD, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            // @formatter:off
            "lower(cast(field_x as varchar)) like lower(('%' || replace(",
            "  replace(",
            "    replace(",
            "      'foo', ",
            "      '!', ",
            "      '!!'",
            "    ), ",
            "    '%', ",
            "    '!%'",
            "  ), ",
            "  '_', ",
            "  '!_'",
            ") || '%')) escape '!'"
            // @formatter:on
        ));
    }

    @Test
    void buildingConditionForRaw_appliesDummyTrue() {
        expectToken(TokenType.RAW, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("1 = 1");
    }

    @Test
    void buildingConditionForUnsupported_throws() {
        expectToken(TokenType.UNSUPPORTED, "foo");
        try {
            e.evaluate(stMock);
            fail("should have thrown exception");
        } catch (Error ex) {
            assertThat(ex)
                .isInstanceOf(AssertionError.class)
                .hasMessage("Evaluation of type UNSUPPORTED is not supported...");
        }
    }
    //endregion

    //region:methodsField
    private void expectMethodToken(TokenType type, String term) {
        when(stMock.getFieldName()).thenReturn("methods");
        tokens.add(new Token(type, term));
        when(stMock.getTokens()).thenReturn(tokens);
    }

    @Test
    void buildingConditionForNotRegex_withMethodsField_appliesNotRegexToAllMethodsFields() {
        expectMethodToken(TokenType.NOTREGEX, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            //@formatter:off
            "(",
                    "  not(lower(cast(coalesce(",
                    "    methods, ",
                    "    ''",
                    "  ) as varchar)) like_regex 'foo')",
                    "  and not(lower(cast(coalesce(",
                    "    method_study_design, ",
                    "    ''",
                    "  ) as varchar)) like_regex 'foo')",
                    "  and not(lower(cast(coalesce(",
                    "    population_place, ",
                    "    ''",
                    "  ) as varchar)) like_regex 'foo')",
                    "  and not(lower(cast(coalesce(",
                    "    method_outcome, ",
                    "    ''",
                    "  ) as varchar)) like_regex 'foo')",
                    "  and not(lower(cast(coalesce(",
                    "    exposure_pollutant, ",
                    "    ''",
                    "  ) as varchar)) like_regex 'foo')",
                    "  and not(lower(cast(coalesce(",
                    "    exposure_assessment, ",
                    "    ''",
                    "  ) as varchar)) like_regex 'foo')",
                    "  and not(lower(cast(coalesce(",
                    "    method_statistics, ",
                    "    ''",
                    "  ) as varchar)) like_regex 'foo')",
                    "  and not(lower(cast(coalesce(",
                    "    method_confounders, ",
                    "    ''",
                    "  ) as varchar)) like_regex 'foo')",
                 ")"
            //@formatter:on
        ));
    }

    @Test
    void buildingConditionForRegex_withMethodsField_appliesRegexToAllMethodsFields() {
        expectMethodToken(TokenType.REGEX, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            //@formatter:off
            "(",
                "  lower(cast(coalesce(",
                "    methods, ",
                "    ''",
                "  ) as varchar)) like_regex 'foo'",
                "  or lower(cast(coalesce(",
                "    method_study_design, ",
                "    ''",
                "  ) as varchar)) like_regex 'foo'",
                "  or lower(cast(coalesce(",
                "    population_place, ",
                "    ''",
                "  ) as varchar)) like_regex 'foo'",
                "  or lower(cast(coalesce(",
                "    method_outcome, ",
                "    ''",
                "  ) as varchar)) like_regex 'foo'",
                "  or lower(cast(coalesce(",
                "    exposure_pollutant, ",
                "    ''",
                "  ) as varchar)) like_regex 'foo'",
                "  or lower(cast(coalesce(",
                "    exposure_assessment, ",
                "    ''",
                "  ) as varchar)) like_regex 'foo'",
                "  or lower(cast(coalesce(",
                "    method_statistics, ",
                "    ''",
                "  ) as varchar)) like_regex 'foo'",
                "  or lower(cast(coalesce(",
                "    method_confounders, ",
                "    ''",
                "  ) as varchar)) like_regex 'foo'",
                ")"
            //@formatter:on
        ));
    }

    @Test
    void buildingConditionForWhitespace_withMethodField_appliesTrueCondition() {
        expectMethodToken(TokenType.WHITESPACE, "   ");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("1 = 1");
    }

    @Test
    void buildingConditionForSome_withMethodField_appliesNotEmpty() {
        expectMethodToken(TokenType.SOME, "whatever");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            // @formatter:off
                "(",
                "  (",
                "    methods is not null",
                "    and char_length(cast(methods as varchar)) > 0",
                "  )",
                "  or (",
                "    method_study_design is not null",
                "    and char_length(cast(method_study_design as varchar)) > 0",
                "  )",
                "  or (",
                "    population_place is not null",
                "    and char_length(cast(population_place as varchar)) > 0",
                "  )",
                "  or (",
                "    method_outcome is not null",
                "    and char_length(cast(method_outcome as varchar)) > 0",
                "  )",
                "  or (",
                "    exposure_pollutant is not null",
                "    and char_length(cast(exposure_pollutant as varchar)) > 0",
                "  )",
                "  or (",
                "    exposure_assessment is not null",
                "    and char_length(cast(exposure_assessment as varchar)) > 0",
                "  )",
                "  or (",
                "    method_statistics is not null",
                "    and char_length(cast(method_statistics as varchar)) > 0",
                "  )",
                "  or (",
                "    method_confounders is not null",
                "    and char_length(cast(method_confounders as varchar)) > 0",
                "  )",
                ")"
                // @formatter:on
        ));
    }

    @Test
    void buildingConditionForEmpty_withMethodField_appliesEmptyToAllMethodsFields() {
        expectMethodToken(TokenType.EMPTY, "whatever");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            // @formatter:off
            "(",
            "  (",
            "    methods is null",
            "    or char_length(cast(methods as varchar)) = 0",
            "  )",
            "  and (",
            "    method_study_design is null",
            "    or char_length(cast(method_study_design as varchar)) = 0",
            "  )",
            "  and (",
            "    population_place is null",
            "    or char_length(cast(population_place as varchar)) = 0",
            "  )",
            "  and (",
            "    method_outcome is null",
            "    or char_length(cast(method_outcome as varchar)) = 0",
            "  )",
            "  and (",
            "    exposure_pollutant is null",
            "    or char_length(cast(exposure_pollutant as varchar)) = 0",
            "  )",
            "  and (",
            "    exposure_assessment is null",
            "    or char_length(cast(exposure_assessment as varchar)) = 0",
            "  )",
            "  and (",
            "    method_statistics is null",
            "    or char_length(cast(method_statistics as varchar)) = 0",
            "  )",
            "  and (",
            "    method_confounders is null",
            "    or char_length(cast(method_confounders as varchar)) = 0",
            "  )",
            ")"
            // @formatter:on
        ));
    }

    @Test
    void buildingConditionForNotOpenLeftRightQuoted_withMethodField_appliesLikeToAllMethodFields() {
        expectMethodToken(TokenType.NOTOPENLEFTRIGHTQUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            // @formatter:off
            "(",
                "  lower(cast(coalesce(",
                "    methods, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo%')",
                "  and lower(cast(coalesce(",
                "    method_study_design, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo%')",
                "  and lower(cast(coalesce(",
                "    population_place, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo%')",
                "  and lower(cast(coalesce(",
                "    method_outcome, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo%')",
                "  and lower(cast(coalesce(",
                "    exposure_pollutant, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo%')",
                "  and lower(cast(coalesce(",
                "    exposure_assessment, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo%')",
                "  and lower(cast(coalesce(",
                "    method_statistics, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo%')",
                "  and lower(cast(coalesce(",
                "    method_confounders, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo%')",
                ")"
            // @formatter:on
        ));
    }

    @Test
    void buildingConditionForOpenLeftRightQuoted_withMethodField_appliesLikeToAllMethodFields() {
        expectMethodToken(TokenType.OPENLEFTRIGHTQUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            // @formatter:off
                "(",
                "  lower(cast(methods as varchar)) like lower('%foo%')",
                "  or lower(cast(method_study_design as varchar)) like lower('%foo%')",
                "  or lower(cast(population_place as varchar)) like lower('%foo%')",
                "  or lower(cast(method_outcome as varchar)) like lower('%foo%')",
                "  or lower(cast(exposure_pollutant as varchar)) like lower('%foo%')",
                "  or lower(cast(exposure_assessment as varchar)) like lower('%foo%')",
                "  or lower(cast(method_statistics as varchar)) like lower('%foo%')",
                "  or lower(cast(method_confounders as varchar)) like lower('%foo%')",
                ")"
            // @formatter:on
        ));
    }

    @Test
    void buildingConditionForNotOpenLeftRight_withMethodField_appliesNotLikeToAllMethodFields() {
        expectMethodToken(TokenType.NOTOPENLEFTRIGHT, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            // @formatter:off
            "(",
                "  lower(cast(coalesce(",
                "    methods, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo%')",
                "  and lower(cast(coalesce(",
                "    method_study_design, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo%')",
                "  and lower(cast(coalesce(",
                "    population_place, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo%')",
                "  and lower(cast(coalesce(",
                "    method_outcome, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo%')",
                "  and lower(cast(coalesce(",
                "    exposure_pollutant, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo%')",
                "  and lower(cast(coalesce(",
                "    exposure_assessment, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo%')",
                "  and lower(cast(coalesce(",
                "    method_statistics, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo%')",
                "  and lower(cast(coalesce(",
                "    method_confounders, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo%')",
                ")"
            // @formatter:on
        ));
    }

    @Test
    void buildingConditionForOpenLeftRight_withMethodField_appliesLikeToAllMethodFields() {
        expectMethodToken(TokenType.OPENLEFTRIGHT, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            // @formatter:off
                "(",
                "  lower(cast(methods as varchar)) like lower('%foo%')",
                "  or lower(cast(method_study_design as varchar)) like lower('%foo%')",
                "  or lower(cast(population_place as varchar)) like lower('%foo%')",
                "  or lower(cast(method_outcome as varchar)) like lower('%foo%')",
                "  or lower(cast(exposure_pollutant as varchar)) like lower('%foo%')",
                "  or lower(cast(exposure_assessment as varchar)) like lower('%foo%')",
                "  or lower(cast(method_statistics as varchar)) like lower('%foo%')",
                "  or lower(cast(method_confounders as varchar)) like lower('%foo%')",
                ")"
            // @formatter:on
        ));
    }

    @Test
    void buildingConditionForNotOpenRightQuoted_withMethodField_appliesLikeToAllMethodFields() {
        expectMethodToken(TokenType.NOTOPENRIGHTQUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            // @formatter:off
            "(",
                "  lower(cast(coalesce(",
                "    methods, ",
                "    ''",
                "  ) as varchar)) not like lower('foo%')",
                "  and lower(cast(coalesce(",
                "    method_study_design, ",
                "    ''",
                "  ) as varchar)) not like lower('foo%')",
                "  and lower(cast(coalesce(",
                "    population_place, ",
                "    ''",
                "  ) as varchar)) not like lower('foo%')",
                "  and lower(cast(coalesce(",
                "    method_outcome, ",
                "    ''",
                "  ) as varchar)) not like lower('foo%')",
                "  and lower(cast(coalesce(",
                "    exposure_pollutant, ",
                "    ''",
                "  ) as varchar)) not like lower('foo%')",
                "  and lower(cast(coalesce(",
                "    exposure_assessment, ",
                "    ''",
                "  ) as varchar)) not like lower('foo%')",
                "  and lower(cast(coalesce(",
                "    method_statistics, ",
                "    ''",
                "  ) as varchar)) not like lower('foo%')",
                "  and lower(cast(coalesce(",
                "    method_confounders, ",
                "    ''",
                "  ) as varchar)) not like lower('foo%')",
                ")"
            // @formatter:on
        ));
    }

    @Test
    void buildingConditionForOpenRightQuoted_withMethodField_appliesLikeToAllMethodFields() {
        expectMethodToken(TokenType.OPENRIGHTQUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            // @formatter:off
                "(",
                "  lower(cast(methods as varchar)) like lower('foo%')",
                "  or lower(cast(method_study_design as varchar)) like lower('foo%')",
                "  or lower(cast(population_place as varchar)) like lower('foo%')",
                "  or lower(cast(method_outcome as varchar)) like lower('foo%')",
                "  or lower(cast(exposure_pollutant as varchar)) like lower('foo%')",
                "  or lower(cast(exposure_assessment as varchar)) like lower('foo%')",
                "  or lower(cast(method_statistics as varchar)) like lower('foo%')",
                "  or lower(cast(method_confounders as varchar)) like lower('foo%')",
                ")"
            // @formatter:on
        ));
    }

    @Test
    void buildingConditionForNotOpenRight_withMethodField_appliesNotLikeToAllMethodFields() {
        expectMethodToken(TokenType.NOTOPENRIGHT, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            // @formatter:off
            "(",
                "  lower(cast(coalesce(",
                "    methods, ",
                "    ''",
                "  ) as varchar)) not like lower('foo%')",
                "  and lower(cast(coalesce(",
                "    method_study_design, ",
                "    ''",
                "  ) as varchar)) not like lower('foo%')",
                "  and lower(cast(coalesce(",
                "    population_place, ",
                "    ''",
                "  ) as varchar)) not like lower('foo%')",
                "  and lower(cast(coalesce(",
                "    method_outcome, ",
                "    ''",
                "  ) as varchar)) not like lower('foo%')",
                "  and lower(cast(coalesce(",
                "    exposure_pollutant, ",
                "    ''",
                "  ) as varchar)) not like lower('foo%')",
                "  and lower(cast(coalesce(",
                "    exposure_assessment, ",
                "    ''",
                "  ) as varchar)) not like lower('foo%')",
                "  and lower(cast(coalesce(",
                "    method_statistics, ",
                "    ''",
                "  ) as varchar)) not like lower('foo%')",
                "  and lower(cast(coalesce(",
                "    method_confounders, ",
                "    ''",
                "  ) as varchar)) not like lower('foo%')",
                ")"
            // @formatter:on
        ));
    }

    @Test
    void buildingConditionForOpenRight_withMethodField_appliesLikeToAllMethodFields() {
        expectMethodToken(TokenType.OPENRIGHT, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            // @formatter:off
                "(",
                "  lower(cast(methods as varchar)) like lower('foo%')",
                "  or lower(cast(method_study_design as varchar)) like lower('foo%')",
                "  or lower(cast(population_place as varchar)) like lower('foo%')",
                "  or lower(cast(method_outcome as varchar)) like lower('foo%')",
                "  or lower(cast(exposure_pollutant as varchar)) like lower('foo%')",
                "  or lower(cast(exposure_assessment as varchar)) like lower('foo%')",
                "  or lower(cast(method_statistics as varchar)) like lower('foo%')",
                "  or lower(cast(method_confounders as varchar)) like lower('foo%')",
                ")"
            // @formatter:on
        ));
    }

    @Test
    void buildingConditionForNotOpenLeftQuoted_withMethodField_appliesLikeToAllMethodFields() {
        expectMethodToken(TokenType.NOTOPENLEFTQUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            // @formatter:off
            "(",
                "  lower(cast(coalesce(",
                "    methods, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo')",
                "  and lower(cast(coalesce(",
                "    method_study_design, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo')",
                "  and lower(cast(coalesce(",
                "    population_place, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo')",
                "  and lower(cast(coalesce(",
                "    method_outcome, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo')",
                "  and lower(cast(coalesce(",
                "    exposure_pollutant, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo')",
                "  and lower(cast(coalesce(",
                "    exposure_assessment, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo')",
                "  and lower(cast(coalesce(",
                "    method_statistics, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo')",
                "  and lower(cast(coalesce(",
                "    method_confounders, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo')",
                ")"
            // @formatter:on
        ));
    }

    @Test
    void buildingConditionForOpenLeftQuoted_withMethodField_appliesLikeToAllMethodFields() {
        expectMethodToken(TokenType.OPENLEFTQUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            // @formatter:off
            "(",
                "  lower(cast(methods as varchar)) like lower('%foo')",
                "  or lower(cast(method_study_design as varchar)) like lower('%foo')",
                "  or lower(cast(population_place as varchar)) like lower('%foo')",
                "  or lower(cast(method_outcome as varchar)) like lower('%foo')",
                "  or lower(cast(exposure_pollutant as varchar)) like lower('%foo')",
                "  or lower(cast(exposure_assessment as varchar)) like lower('%foo')",
                "  or lower(cast(method_statistics as varchar)) like lower('%foo')",
                "  or lower(cast(method_confounders as varchar)) like lower('%foo')",
                ")"
            // @formatter:on
        ));
    }

    @Test
    void buildingConditionForNotOpenLeft_withMethodField_appliesNotLikeToAllMethodFields() {
        expectMethodToken(TokenType.NOTOPENLEFT, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            // @formatter:off
            "(",
                "  lower(cast(coalesce(",
                "    methods, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo')",
                "  and lower(cast(coalesce(",
                "    method_study_design, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo')",
                "  and lower(cast(coalesce(",
                "    population_place, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo')",
                "  and lower(cast(coalesce(",
                "    method_outcome, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo')",
                "  and lower(cast(coalesce(",
                "    exposure_pollutant, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo')",
                "  and lower(cast(coalesce(",
                "    exposure_assessment, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo')",
                "  and lower(cast(coalesce(",
                "    method_statistics, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo')",
                "  and lower(cast(coalesce(",
                "    method_confounders, ",
                "    ''",
                "  ) as varchar)) not like lower('%foo')",
                ")"
            // @formatter:on
        ));
    }

    @Test
    void buildingConditionForOpenLeft_withMethodField_appliesLikeToAllMethodFields() {
        expectMethodToken(TokenType.OPENLEFT, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            // @formatter:off
            "(",
                "  lower(cast(methods as varchar)) like lower('%foo')",
                "  or lower(cast(method_study_design as varchar)) like lower('%foo')",
                "  or lower(cast(population_place as varchar)) like lower('%foo')",
                "  or lower(cast(method_outcome as varchar)) like lower('%foo')",
                "  or lower(cast(exposure_pollutant as varchar)) like lower('%foo')",
                "  or lower(cast(exposure_assessment as varchar)) like lower('%foo')",
                "  or lower(cast(method_statistics as varchar)) like lower('%foo')",
                "  or lower(cast(method_confounders as varchar)) like lower('%foo')",
                ")"
            // @formatter:on
        ));
    }

    @Test
    void buildingConditionForNotQuoted_withMethodField_appliesUnequalToAllMethodFields() {
        expectMethodToken(TokenType.NOTQUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            // @formatter:off
                "(",
                "  lower(cast(methods as varchar)) <> lower('foo')",
                "  and lower(cast(method_study_design as varchar)) <> lower('foo')",
                "  and lower(cast(population_place as varchar)) <> lower('foo')",
                "  and lower(cast(method_outcome as varchar)) <> lower('foo')",
                "  and lower(cast(exposure_pollutant as varchar)) <> lower('foo')",
                "  and lower(cast(exposure_assessment as varchar)) <> lower('foo')",
                "  and lower(cast(method_statistics as varchar)) <> lower('foo')",
                "  and lower(cast(method_confounders as varchar)) <> lower('foo')",
                ")"
             // @formatter:on
        ));
    }

    @Test
    void buildingConditionForQuoted_withMethodField_appliesEqualToAllMethodFields() {
        expectMethodToken(TokenType.QUOTED, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            // @formatter:off
                "(",
                "  lower(cast(methods as varchar)) = lower('foo')",
                "  or lower(cast(method_study_design as varchar)) = lower('foo')",
                "  or lower(cast(population_place as varchar)) = lower('foo')",
                "  or lower(cast(method_outcome as varchar)) = lower('foo')",
                "  or lower(cast(exposure_pollutant as varchar)) = lower('foo')",
                "  or lower(cast(exposure_assessment as varchar)) = lower('foo')",
                "  or lower(cast(method_statistics as varchar)) = lower('foo')",
                "  or lower(cast(method_confounders as varchar)) = lower('foo')",
                ")"
             // @formatter:on
        ));
    }

    @Test
    void buildingConditionForNotWord_withMethodField_appliesNotContainsToAllMethodFields() {
        expectMethodToken(TokenType.NOTWORD, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            // @formatter:off
                "(",
                "  not(lower(cast(coalesce(",
                "    methods, ",
                "    ''",
                "  ) as varchar)) like lower(('%' || replace(",
                "    replace(",
                "      replace(",
                "        'foo', ",
                "        '!', ",
                "        '!!'",
                "      ), ",
                "      '%', ",
                "      '!%'",
                "    ), ",
                "    '_', ",
                "    '!_'",
                "  ) || '%')) escape '!')",
                "  and not(lower(cast(coalesce(",
                "    method_study_design, ",
                "    ''",
                "  ) as varchar)) like lower(('%' || replace(",
                "    replace(",
                "      replace(",
                "        'foo', ",
                "        '!', ",
                "        '!!'",
                "      ), ",
                "      '%', ",
                "      '!%'",
                "    ), ",
                "    '_', ",
                "    '!_'",
                "  ) || '%')) escape '!')",
                "  and not(lower(cast(coalesce(",
                "    population_place, ",
                "    ''",
                "  ) as varchar)) like lower(('%' || replace(",
                "    replace(",
                "      replace(",
                "        'foo', ",
                "        '!', ",
                "        '!!'",
                "      ), ",
                "      '%', ",
                "      '!%'",
                "    ), ",
                "    '_', ",
                "    '!_'",
                "  ) || '%')) escape '!')",
                "  and not(lower(cast(coalesce(",
                "    method_outcome, ",
                "    ''",
                "  ) as varchar)) like lower(('%' || replace(",
                "    replace(",
                "      replace(",
                "        'foo', ",
                "        '!', ",
                "        '!!'",
                "      ), ",
                "      '%', ",
                "      '!%'",
                "    ), ",
                "    '_', ",
                "    '!_'",
                "  ) || '%')) escape '!')",
                "  and not(lower(cast(coalesce(",
                "    exposure_pollutant, ",
                "    ''",
                "  ) as varchar)) like lower(('%' || replace(",
                "    replace(",
                "      replace(",
                "        'foo', ",
                "        '!', ",
                "        '!!'",
                "      ), ",
                "      '%', ",
                "      '!%'",
                "    ), ",
                "    '_', ",
                "    '!_'",
                "  ) || '%')) escape '!')",
                "  and not(lower(cast(coalesce(",
                "    exposure_assessment, ",
                "    ''",
                "  ) as varchar)) like lower(('%' || replace(",
                "    replace(",
                "      replace(",
                "        'foo', ",
                "        '!', ",
                "        '!!'",
                "      ), ",
                "      '%', ",
                "      '!%'",
                "    ), ",
                "    '_', ",
                "    '!_'",
                "  ) || '%')) escape '!')",
                "  and not(lower(cast(coalesce(",
                "    method_statistics, ",
                "    ''",
                "  ) as varchar)) like lower(('%' || replace(",
                "    replace(",
                "      replace(",
                "        'foo', ",
                "        '!', ",
                "        '!!'",
                "      ), ",
                "      '%', ",
                "      '!%'",
                "    ), ",
                "    '_', ",
                "    '!_'",
                "  ) || '%')) escape '!')",
                "  and not(lower(cast(coalesce(",
                "    method_confounders, ",
                "    ''",
                "  ) as varchar)) like lower(('%' || replace(",
                "    replace(",
                "      replace(",
                "        'foo', ",
                "        '!', ",
                "        '!!'",
                "      ), ",
                "      '%', ",
                "      '!%'",
                "    ), ",
                "    '_', ",
                "    '!_'",
                "  ) || '%')) escape '!')",
                ")"
            // @formatter:on
        ));
    }

    @Test
    void buildingConditionForWord_withMethodField_appliesContainsToAllMethodFields() {
        expectMethodToken(TokenType.WORD, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo(concat(
            // @formatter:off
            "(",
            "  lower(cast(methods as varchar)) like lower(('%' || replace(",
            "    replace(",
            "      replace(",
            "        'foo', ",
            "        '!', ",
            "        '!!'",
            "      ), ",
            "      '%', ",
            "      '!%'",
            "    ), ",
            "    '_', ",
            "    '!_'",
            "  ) || '%')) escape '!'",
            "  or lower(cast(method_study_design as varchar)) like lower(('%' || replace(",
            "    replace(",
            "      replace(",
            "        'foo', ",
            "        '!', ",
            "        '!!'",
            "      ), ",
            "      '%', ",
            "      '!%'",
            "    ), ",
            "    '_', ",
            "    '!_'",
            "  ) || '%')) escape '!'",
            "  or lower(cast(population_place as varchar)) like lower(('%' || replace(",
            "    replace(",
            "      replace(",
            "        'foo', ",
            "        '!', ",
            "        '!!'",
            "      ), ",
            "      '%', ",
            "      '!%'",
            "    ), ",
            "    '_', ",
            "    '!_'",
            "  ) || '%')) escape '!'",
            "  or lower(cast(method_outcome as varchar)) like lower(('%' || replace(",
            "    replace(",
            "      replace(",
            "        'foo', ",
            "        '!', ",
            "        '!!'",
            "      ), ",
            "      '%', ",
            "      '!%'",
            "    ), ",
            "    '_', ",
            "    '!_'",
            "  ) || '%')) escape '!'",
            "  or lower(cast(exposure_pollutant as varchar)) like lower(('%' || replace(",
            "    replace(",
            "      replace(",
            "        'foo', ",
            "        '!', ",
            "        '!!'",
            "      ), ",
            "      '%', ",
            "      '!%'",
            "    ), ",
            "    '_', ",
            "    '!_'",
            "  ) || '%')) escape '!'",
            "  or lower(cast(exposure_assessment as varchar)) like lower(('%' || replace(",
            "    replace(",
            "      replace(",
            "        'foo', ",
            "        '!', ",
            "        '!!'",
            "      ), ",
            "      '%', ",
            "      '!%'",
            "    ), ",
            "    '_', ",
            "    '!_'",
            "  ) || '%')) escape '!'",
            "  or lower(cast(method_statistics as varchar)) like lower(('%' || replace(",
            "    replace(",
            "      replace(",
            "        'foo', ",
            "        '!', ",
            "        '!!'",
            "      ), ",
            "      '%', ",
            "      '!%'",
            "    ), ",
            "    '_', ",
            "    '!_'",
            "  ) || '%')) escape '!'",
            "  or lower(cast(method_confounders as varchar)) like lower(('%' || replace(",
            "    replace(",
            "      replace(",
            "        'foo', ",
            "        '!', ",
            "        '!!'",
            "      ), ",
            "      '%', ",
            "      '!%'",
            "    ), ",
            "    '_', ",
            "    '!_'",
            "  ) || '%')) escape '!'",
            ")"
            // @formatter:on
        ));
    }

    @Test
    void buildingConditionForRaw_withMethodField_appliesDummyTrue() {
        expectMethodToken(TokenType.RAW, "foo");
        assertThat(e
            .evaluate(stMock)
            .toString()).isEqualTo("1 = 1");
    }

    @Test
    void buildingConditionForUnsupported_withMethodField_throws() {
        expectMethodToken(TokenType.UNSUPPORTED, "foo");
        try {
            e.evaluate(stMock);
            fail("should have thrown exception");
        } catch (Error ex) {
            assertThat(ex)
                .isInstanceOf(AssertionError.class)
                .hasMessage("Evaluation of type UNSUPPORTED is not supported...");
        }
    }

    @Test
    void buildingConditionForCombinedSearchTerm_withMethodsField_() {
        StringSearchTerm sst = (StringSearchTerm) SearchTerm.newSearchTerm(1L, SearchTermType.STRING.getId(), 1L,
            "methods", "foo -bar");
        assertThat(e
            .evaluate(sst)
            .toString()).isEqualTo(concat(
            //@formatter:off
            "(",
            "  (",
            "    lower(cast(methods as varchar)) like lower(('%' || replace(",
            "      replace(",
            "        replace(",
            "          'foo', ",
            "          '!', ",
            "          '!!'",
            "        ), ",
            "        '%', ",
            "        '!%'",
            "      ), ",
            "      '_', ",
            "      '!_'",
            "    ) || '%')) escape '!'",
            "    or lower(cast(method_study_design as varchar)) like lower(('%' || replace(",
            "      replace(",
            "        replace(",
            "          'foo', ",
            "          '!', ",
            "          '!!'",
            "        ), ",
            "        '%', ",
            "        '!%'",
            "      ), ",
            "      '_', ",
            "      '!_'",
            "    ) || '%')) escape '!'",
            "    or lower(cast(population_place as varchar)) like lower(('%' || replace(",
            "      replace(",
            "        replace(",
            "          'foo', ",
            "          '!', ",
            "          '!!'",
            "        ), ",
            "        '%', ",
            "        '!%'",
            "      ), ",
            "      '_', ",
            "      '!_'",
            "    ) || '%')) escape '!'",
            "    or lower(cast(method_outcome as varchar)) like lower(('%' || replace(",
            "      replace(",
            "        replace(",
            "          'foo', ",
            "          '!', ",
            "          '!!'",
            "        ), ",
            "        '%', ",
            "        '!%'",
            "      ), ",
            "      '_', ",
            "      '!_'",
            "    ) || '%')) escape '!'",
            "    or lower(cast(exposure_pollutant as varchar)) like lower(('%' || replace(",
            "      replace(",
            "        replace(",
            "          'foo', ",
            "          '!', ",
            "          '!!'",
            "        ), ",
            "        '%', ",
            "        '!%'",
            "      ), ",
            "      '_', ",
            "      '!_'",
            "    ) || '%')) escape '!'",
            "    or lower(cast(exposure_assessment as varchar)) like lower(('%' || replace(",
            "      replace(",
            "        replace(",
            "          'foo', ",
            "          '!', ",
            "          '!!'",
            "        ), ",
            "        '%', ",
            "        '!%'",
            "      ), ",
            "      '_', ",
            "      '!_'",
            "    ) || '%')) escape '!'",
            "    or lower(cast(method_statistics as varchar)) like lower(('%' || replace(",
            "      replace(",
            "        replace(",
            "          'foo', ",
            "          '!', ",
            "          '!!'",
            "        ), ",
            "        '%', ",
            "        '!%'",
            "      ), ",
            "      '_', ",
            "      '!_'",
            "    ) || '%')) escape '!'",
            "    or lower(cast(method_confounders as varchar)) like lower(('%' || replace(",
            "      replace(",
            "        replace(",
            "          'foo', ",
            "          '!', ",
            "          '!!'",
            "        ), ",
            "        '%', ",
            "        '!%'",
            "      ), ",
            "      '_', ",
            "      '!_'",
            "    ) || '%')) escape '!'",
            "  )",
            "  and not(lower(cast(coalesce(",
            "    methods, ",
            "    ''",
            "  ) as varchar)) like lower(('%' || replace(",
            "    replace(",
            "      replace(",
            "        'bar', ",
            "        '!', ",
            "        '!!'",
            "      ), ",
            "      '%', ",
            "      '!%'",
            "    ), ",
            "    '_', ",
            "    '!_'",
            "  ) || '%')) escape '!')",
            "  and not(lower(cast(coalesce(",
            "    method_study_design, ",
            "    ''",
            "  ) as varchar)) like lower(('%' || replace(",
            "    replace(",
            "      replace(",
            "        'bar', ",
            "        '!', ",
            "        '!!'",
            "      ), ",
            "      '%', ",
            "      '!%'",
            "    ), ",
            "    '_', ",
            "    '!_'",
            "  ) || '%')) escape '!')",
            "  and not(lower(cast(coalesce(",
            "    population_place, ",
            "    ''",
            "  ) as varchar)) like lower(('%' || replace(",
            "    replace(",
            "      replace(",
            "        'bar', ",
            "        '!', ",
            "        '!!'",
            "      ), ",
            "      '%', ",
            "      '!%'",
            "    ), ",
            "    '_', ",
            "    '!_'",
            "  ) || '%')) escape '!')",
            "  and not(lower(cast(coalesce(",
            "    method_outcome, ",
            "    ''",
            "  ) as varchar)) like lower(('%' || replace(",
            "    replace(",
            "      replace(",
            "        'bar', ",
            "        '!', ",
            "        '!!'",
            "      ), ",
            "      '%', ",
            "      '!%'",
            "    ), ",
            "    '_', ",
            "    '!_'",
            "  ) || '%')) escape '!')",
            "  and not(lower(cast(coalesce(",
            "    exposure_pollutant, ",
            "    ''",
            "  ) as varchar)) like lower(('%' || replace(",
            "    replace(",
            "      replace(",
            "        'bar', ",
            "        '!', ",
            "        '!!'",
            "      ), ",
            "      '%', ",
            "      '!%'",
            "    ), ",
            "    '_', ",
            "    '!_'",
            "  ) || '%')) escape '!')",
            "  and not(lower(cast(coalesce(",
            "    exposure_assessment, ",
            "    ''",
            "  ) as varchar)) like lower(('%' || replace(",
            "    replace(",
            "      replace(",
            "        'bar', ",
            "        '!', ",
            "        '!!'",
            "      ), ",
            "      '%', ",
            "      '!%'",
            "    ), ",
            "    '_', ",
            "    '!_'",
            "  ) || '%')) escape '!')",
            "  and not(lower(cast(coalesce(",
            "    method_statistics, ",
            "    ''",
            "  ) as varchar)) like lower(('%' || replace(",
            "    replace(",
            "      replace(",
            "        'bar', ",
            "        '!', ",
            "        '!!'",
            "      ), ",
            "      '%', ",
            "      '!%'",
            "    ), ",
            "    '_', ",
            "    '!_'",
            "  ) || '%')) escape '!')",
            "  and not(lower(cast(coalesce(",
            "    method_confounders, ",
            "    ''",
            "  ) as varchar)) like lower(('%' || replace(",
            "    replace(",
            "      replace(",
            "        'bar', ",
            "        '!', ",
            "        '!!'",
            "      ), ",
            "      '%', ",
            "      '!%'",
            "    ), ",
            "    '_', ",
            "    '!_'",
            "  ) || '%')) escape '!')",
            ")"
            //@formatter:on
        ));
    }

    //endregion
}
