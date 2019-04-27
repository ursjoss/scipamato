package ch.difty.scipamato.core.web.paper.jasper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.entity.Code;
import ch.difty.scipamato.core.entity.User;

class ScipamatoPdfExporterConfigurationTest {

    private static final String                            TITLE = "title";
    private              ScipamatoPdfExporterConfiguration config;
    private final        List<Code>                        codes = new ArrayList<>();

    @Test
    void minimalInstantiation_buildingTitleFromHeaderPartAndNumber_intoMetadataTitle() {
        config = new ScipamatoPdfExporterConfiguration.Builder("headerpart:", 5L).build();
        assertingMinimalConfigWithTitle("headerpart: 5");
    }

    @Test
    void minimalInstantiation_buildingTitleFromHeaderPartAndNullNumber_intoMetadataTitle() {
        config = new ScipamatoPdfExporterConfiguration.Builder("headerpart:", null).build();
        assertingMinimalConfigWithTitle("headerpart:");
    }

    @Test
    void minimalInstantiation_buildingTitleFromNullHeaderPartAndNullNumber_intoMetadataTitle() {
        config = new ScipamatoPdfExporterConfiguration.Builder(null, null).build();
        assertingMinimalConfigWithTitle(null);
    }

    @Test
    void minimalInstantiation_buildingTitleFromNullHeaderPartAndNumber_intoMetadataTitle() {
        config = new ScipamatoPdfExporterConfiguration.Builder(null, 3L).build();
        assertingMinimalConfigWithTitle("3");
    }

    private void assertingMinimalConfigWithTitle(String title) {
        assertThat(config.getMetadataCreator()).isNull();
        assertThat(config.getMetadataAuthor()).isNull();
        assertThat(config.getMetadataTitle()).isEqualTo(title);
        assertThat(config.getMetadataSubject()).isNull();
        assertThat(config.getMetadataKeywords()).isNull();
        assertThat(config.isCompressed()).isFalse();
    }

    @Test
    void minimalInstantiation_buildingTitleAsIsIntoMetadataTitle() {
        config = new ScipamatoPdfExporterConfiguration.Builder(TITLE).build();
        assertingMinimalConfigWithTitle(TITLE);
    }

    @Test
    void withAuthor_asString_setsMetadataAuthor() {
        config = new ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withAuthor("author")
            .build();
        assertThat(config.getMetadataCreator()).isNull();
        assertThat(config.getMetadataAuthor()).isEqualTo("author");
        assertThat(config.getMetadataTitle()).isEqualTo(TITLE);
        assertThat(config.getMetadataSubject()).isNull();
        assertThat(config.getMetadataKeywords()).isNull();
        assertThat(config.isCompressed()).isFalse();
    }

    @Test
    void withAuthor_asUser_setsMetadataAuthorFromFirstAndLastName() {
        final User user = new User(3, "username", "first", "last", "email", "pw");
        config = new ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withAuthor(user)
            .build();
        assertThat(config.getMetadataCreator()).isNull();
        assertThat(config.getMetadataAuthor()).isEqualTo("first last");
        assertThat(config.getMetadataTitle()).isEqualTo(TITLE);
        assertThat(config.getMetadataSubject()).isNull();
        assertThat(config.getMetadataKeywords()).isNull();
        assertThat(config.isCompressed()).isFalse();
    }

    @Test
    void withAuthor_asNullUser_doesNotSetMetadataAuthor() {
        config = new ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withAuthor((User) null)
            .build();
        assertThat(config.getMetadataAuthor()).isNull();
    }

    @Test
    void withPaperTitle_concatenatesTitleAndPaperTitleIntoMetadataTitle() {
        config = new ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withPaperTitle("papertitle")
            .build();
        assertThat(config.getMetadataCreator()).isNull();
        assertThat(config.getMetadataAuthor()).isNull();
        assertThat(config.getMetadataTitle()).isEqualTo(TITLE + " - papertitle");
        assertThat(config.getMetadataSubject()).isNull();
        assertThat(config.getMetadataKeywords()).isNull();
        assertThat(config.isCompressed()).isFalse();
    }

    @Test
    void withPaperTitle_withNullTitle_onlyAppliesPaperTitle() {
        config = new ScipamatoPdfExporterConfiguration.Builder(null)
            .withPaperTitle("papertitle")
            .build();
        assertThat(config.getMetadataTitle()).isEqualTo("papertitle");
    }

    @Test
    void withNullPaperTitle_withTitleAndAuthor_appliesTitleAndAuthor() {
        config = new ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withPaperTitle(null)
            .withPaperAuthor("author")
            .build();
        assertThat(config.getMetadataTitle()).isEqualTo("title - author et al.");
    }

    @Test
    void withPaperTitle_concatenatesTitleAndPaperAuthorAndPaperTitleIntoMetadataTitle() {
        config = new ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withPaperTitle("papertitle")
            .withPaperAuthor("paperAuthor")
            .build();
        assertThat(config.getMetadataCreator()).isNull();
        assertThat(config.getMetadataAuthor()).isNull();
        assertThat(config.getMetadataTitle()).isEqualTo(TITLE + " - paperAuthor et al.: papertitle");
        assertThat(config.getMetadataSubject()).isNull();
        assertThat(config.getMetadataKeywords()).isNull();
        assertThat(config.isCompressed()).isFalse();
    }

    @Test
    void withSubject_setsMetadataSubject() {
        config = new ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withSubject("subject")
            .build();
        assertThat(config.getMetadataCreator()).isNull();
        assertThat(config.getMetadataAuthor()).isNull();
        assertThat(config.getMetadataTitle()).isEqualTo(TITLE);
        assertThat(config.getMetadataSubject()).isEqualTo("subject");
        assertThat(config.getMetadataKeywords()).isNull();
        assertThat(config.isCompressed()).isFalse();
    }

    @Test
    void withCreator_setsMetadataCreator() {
        config = new ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withCreator("creator")
            .build();
        assertThat(config.getMetadataCreator()).isEqualTo("creator");
        assertThat(config.getMetadataAuthor()).isNull();
        assertThat(config.getMetadataTitle()).isEqualTo(TITLE);
        assertThat(config.getMetadataSubject()).isNull();
        assertThat(config.getMetadataKeywords()).isNull();
        assertThat(config.isCompressed()).isFalse();
    }

    @Test
    void withSingleCodeSpaceLess_setsMetadataKeywords() {
        codes.add(new Code("1A", "code1", null, false, 1, "c1", "", 1));
        config = new ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withCodes(codes)
            .build();
        assertThat(config.getMetadataCreator()).isNull();
        assertThat(config.getMetadataAuthor()).isNull();
        assertThat(config.getMetadataTitle()).isEqualTo(TITLE);
        assertThat(config.getMetadataSubject()).isNull();
        assertThat(config.getMetadataKeywords()).isEqualTo("code1");
        assertThat(config.isCompressed()).isFalse();
    }

    @Test
    void withDoubleCodesBothSpaceLess_setsMetadataKeywordsCommaSeparated() {
        codes.add(new Code("1A", "code1", null, false, 1, "c1", "", 1));
        codes.add(new Code("2B", "code2", null, false, 2, "c2", "", 1));
        config = new ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withCodes(codes)
            .build();
        assertThat(config.getMetadataKeywords()).isEqualTo("code1,code2");
    }

    @Test
    void withTripleCodesOneWithSpaces_setsMetadataKeywordsCommaSeparatedAndPartiallyQuoted() {
        codes.add(new Code("1A", "code1", null, false, 1, "c1", "", 1));
        codes.add(new Code("2B", "code2 with spaces", null, false, 2, "c2", "", 1));
        codes.add(new Code("3C", "code3", null, false, 3, "c3", "", 1));
        config = new ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withCodes(codes)
            .build();
        assertThat(config.getMetadataKeywords()).isEqualTo("code1,\"code2 with spaces\",code3");
    }

    @Test
    void withCompression() {
        config = new ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withCompression()
            .build();
        assertThat(config.getMetadataCreator()).isNull();
        assertThat(config.getMetadataAuthor()).isNull();
        assertThat(config.getMetadataTitle()).isEqualTo(TITLE);
        assertThat(config.getMetadataSubject()).isNull();
        assertThat(config.getMetadataKeywords()).isNull();
        assertThat(config.isCompressed()).isTrue();
    }

    @Test
    void withAllAttributes() {
        codes.add(new Code("1A", "c1", null, false, 1, "c1", "", 1));
        codes.add(new Code("2B", "c2 with spaces", null, false, 2, "c2", "", 1));
        config = new ScipamatoPdfExporterConfiguration.Builder("hp:", 10L)
            .withAuthor("a")
            .withPaperTitle("pt")
            .withPaperAuthor("pa")
            .withSubject("s")
            .withCreator("c")
            .withCodes(codes)
            .withCompression()
            .build();
        assertThat(config.getMetadataCreator()).isEqualTo("c");
        assertThat(config.getMetadataAuthor()).isEqualTo("a");
        assertThat(config.getMetadataTitle()).isEqualTo("hp: 10 - pa et al.: pt");
        assertThat(config.getMetadataSubject()).isEqualTo("s");
        assertThat(config.getMetadataKeywords()).isEqualTo("c1,\"c2 with spaces\"");
        assertThat(config.isCompressed()).isTrue();
    }

    @Test
    void withNullCodes_returnsNullMetadataKeywords() {
        config = new ScipamatoPdfExporterConfiguration.Builder(TITLE)
            .withCodes(null)
            .build();
        assertThat(config.getMetadataCreator()).isNull();
        assertThat(config.getMetadataAuthor()).isNull();
        assertThat(config.getMetadataTitle()).isEqualTo(TITLE);
        assertThat(config.getMetadataSubject()).isNull();
        assertThat(config.getMetadataKeywords()).isNull();
        assertThat(config.isCompressed()).isFalse();
    }

}
