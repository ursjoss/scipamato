package ch.difty.scipamato.publ.persistence.paper.authors;

import static ch.difty.scipamato.common.TestUtilsKt.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.publ.config.ApplicationPublicProperties;

@ExtendWith(MockitoExtension.class)
class EtAlAuthorsAbbreviatorTest {

    private final int maxLength = 16;

    private EtAlAuthorsAbbreviator abbr16;

    @Mock
    private ApplicationPublicProperties propertiesMock;

    private static final String ET_AL = " et al.";

    @BeforeEach
    void setUp() {
        when(propertiesMock.getAuthorsAbbreviatedMaxLength()).thenReturn(maxLength);
        abbr16 = new EtAlAuthorsAbbreviator(propertiesMock);
    }

    private void assertAbbreviation(final String abbr, final String expected) {
        assertThat(abbr.length()).isLessThanOrEqualTo(maxLength);
        assertThat(abbr).isEqualTo(expected);
    }

    @Test
    void constructingAbbreviatorWithNullProperties_throw() {
        assertDegenerateSupplierParameter(() -> new EtAlAuthorsAbbreviator(null), "properties");
    }

    @Test
    void abbreviating_withNullAuthors_returnsBlank() {
        assertThat(abbr16.abbreviate(null)).isBlank();
    }

    @Test
    void abbreviating_withAuthorsLengthBelowThreshold_returnsFullAuthorsString() {
        String authors = "12345678901.";
        assertThat(authors.length()).isLessThan(maxLength);

        assertAbbreviation(abbr16.abbreviate(authors), authors);
    }

    @Test
    void abbreviating_withAuthorsLengthAtThreshold_returnsFullAuthorsString() {
        String authors = "123456789012345.";
        assertThat(authors.length()).isEqualTo(maxLength);

        assertAbbreviation(abbr16.abbreviate(authors), authors);
    }

    @Test
    void abbreviating_withSingleAuthorLongerThanThreshold_returnsAbbreviatedAuthorWithEllipsis() {
        String authors = "12345678901234567890.";
        assertThat(authors.length()).isGreaterThan(maxLength);

        assertAbbreviation(abbr16.abbreviate(authors), "1234567890123...");
    }

    @Test
    void abbreviating_withMultipleAuthors_returnsFirstAuthorEtAl() {
        String authors = "FooBar F, BarBaz B.";
        assertThat(authors.length()).isGreaterThan(maxLength);

        assertAbbreviation(abbr16.abbreviate(authors), "FooBar F et al.");
    }

    @Test
    void abbreviating_withMultipleAuthors_withCommaAtCutoff_returnsTwoAuthorsEtAl() {
        String authors = "F F, A A, Bar B, Baz B.";
        int cutoffIndex = maxLength - ET_AL.length() - 1;
        assertThat(authors.charAt(cutoffIndex)).isEqualTo(',');

        assertAbbreviation(abbr16.abbreviate(authors), "F F, A A et al.");
    }

    @Test
    void abbreviating_withMultipleAuthors_withCommaJustBeyondCutoff_returnsTwoAuthorsEtAl() {
        String authors = "F F, AB A, Bar B, Baz B.";
        int justAfterCutoffIndex = maxLength - ET_AL.length();
        assertThat(authors.charAt(justAfterCutoffIndex)).isEqualTo(',');

        assertAbbreviation(abbr16.abbreviate(authors), "F F, AB A et al.");
    }

    @Test
    void abbreviating_withMultipleAuthors_withPeriodJustBeyondCutoff_returnsTwoAuthorsEtAl() {
        String authors = "F F, AB A.";
        int justAfterCutoffIndex = maxLength - ET_AL.length();
        assertThat(authors.charAt(justAfterCutoffIndex)).isEqualTo('.');

        assertAbbreviation(abbr16.abbreviate(authors), "F F, AB A.");
    }

    @Test
    void abbreviating_withMaxLengthZero_doesNotAbbreviate() {
        Mockito.reset(propertiesMock);
        when(propertiesMock.getAuthorsAbbreviatedMaxLength()).thenReturn(0);
        EtAlAuthorsAbbreviator abbr0 = new EtAlAuthorsAbbreviator(propertiesMock);
        String authors = "Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, .";

        assertThat(abbr0.abbreviate(authors)).isEqualTo(authors);
    }
}
