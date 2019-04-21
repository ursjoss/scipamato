package ch.difty.scipamato.core.entity.keyword;

import static ch.difty.scipamato.core.entity.keyword.Keyword.KeywordFields.NAME;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.entity.Jsr303ValidatedEntityTest;

public class KeywordTest extends Jsr303ValidatedEntityTest<Keyword> {

    public KeywordTest() {
        super(Keyword.class);
    }

    @Override
    protected Keyword newValidEntity() {
        return new Keyword(10, "kw", "so");
    }

    @Override
    protected String getToString() {
        return "Keyword(name=kw, searchOverride=so)";
    }

    @Override
    protected String getDisplayValue() {
        return newValidEntity().getName();
    }

    @Test
    public void get() {
        Keyword nt = newValidEntity();
        assertThat(nt.getId()).isEqualTo(10);
        assertThat(nt.getName()).isEqualTo("kw");
        assertThat(nt.getSearchOverride()).isEqualTo("so");
    }

    @Test
    public void validatingKeyword_withCompleteInformation_succeeds() {
        verifySuccessfulValidation(newValidEntity());
    }

    @Test
    public void validatingNewsletter_withNullIssue_fails() {
        Keyword nt = newValidEntity();
        nt.setName(null);
        validateAndAssertFailure(nt, NAME, null, "{javax.validation.constraints.NotNull.message}");
    }

}