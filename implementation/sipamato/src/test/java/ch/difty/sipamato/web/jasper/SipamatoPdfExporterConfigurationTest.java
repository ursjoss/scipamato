package ch.difty.sipamato.web.jasper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.User;

public class SipamatoPdfExporterConfigurationTest {

    private static final String TITLE = "title";
    private SipamatoPdfExporterConfiguration config;
    private final List<Code> codes = new ArrayList<>();

    @Test
    public void minimalInstantiation_buildingTitleFromHeaderPartAndId_intoMetadataTitle() {
        config = new SipamatoPdfExporterConfiguration.Builder("headerpart:", 5l).build();
        assertingMinimalConfigWithTitle("headerpart: 5");
    }

    @Test
    public void minimalInstantiation_buildingTitleFromHeaderPartAndNullId_intoMetadataTitle() {
        config = new SipamatoPdfExporterConfiguration.Builder("headerpart:", null).build();
        assertingMinimalConfigWithTitle("headerpart:");
    }

    @Test
    public void minimalInstantiation_buildingTitleFromNullHeaderPartAndNullId_intoMetadataTitle() {
        config = new SipamatoPdfExporterConfiguration.Builder(null, null).build();
        assertingMinimalConfigWithTitle(null);
    }

    @Test
    public void minimalInstantiation_buildingTitleFromNullHeaderPartAndId_intoMetadataTitle() {
        config = new SipamatoPdfExporterConfiguration.Builder(null, 3l).build();
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
    public void minimalInstantiation_buildingTitleAsIsIntoMetadataTitle() {
        config = new SipamatoPdfExporterConfiguration.Builder(TITLE).build();
        assertingMinimalConfigWithTitle(TITLE);
    }

    @Test
    public void withAuthor_asString_setsMetadataAuthor() {
        config = new SipamatoPdfExporterConfiguration.Builder(TITLE).withAuthor("author").build();
        assertThat(config.getMetadataCreator()).isNull();
        assertThat(config.getMetadataAuthor()).isEqualTo("author");
        assertThat(config.getMetadataTitle()).isEqualTo(TITLE);
        assertThat(config.getMetadataSubject()).isNull();
        assertThat(config.getMetadataKeywords()).isNull();
        assertThat(config.isCompressed()).isFalse();
    }

    @Test
    public void withAuthor_asUser_setsMetadataAuthorFromFirstAndLastName() {
        final User user = new User(3, "username", "first", "last", "email", "pw");
        config = new SipamatoPdfExporterConfiguration.Builder(TITLE).withAuthor(user).build();
        assertThat(config.getMetadataCreator()).isNull();
        assertThat(config.getMetadataAuthor()).isEqualTo("first last");
        assertThat(config.getMetadataTitle()).isEqualTo(TITLE);
        assertThat(config.getMetadataSubject()).isNull();
        assertThat(config.getMetadataKeywords()).isNull();
        assertThat(config.isCompressed()).isFalse();
    }

    @Test
    public void withPaperTitle_concatsTitleAndPaperTitleIntoMetadataTitle() {
        config = new SipamatoPdfExporterConfiguration.Builder(TITLE).withPaperTitle("papertitle").build();
        assertThat(config.getMetadataCreator()).isNull();
        assertThat(config.getMetadataAuthor()).isNull();
        assertThat(config.getMetadataTitle()).isEqualTo(TITLE + ": papertitle");
        assertThat(config.getMetadataSubject()).isNull();
        assertThat(config.getMetadataKeywords()).isNull();
        assertThat(config.isCompressed()).isFalse();
    }

    @Test
    public void withSubject_setsMetadataSubject() {
        config = new SipamatoPdfExporterConfiguration.Builder(TITLE).withSubject("subject").build();
        assertThat(config.getMetadataCreator()).isNull();
        assertThat(config.getMetadataAuthor()).isNull();
        assertThat(config.getMetadataTitle()).isEqualTo(TITLE);
        assertThat(config.getMetadataSubject()).isEqualTo("subject");
        assertThat(config.getMetadataKeywords()).isNull();
        assertThat(config.isCompressed()).isFalse();
    }

    @Test
    public void withCreator_setsMetadataCreator() {
        config = new SipamatoPdfExporterConfiguration.Builder(TITLE).withCreator("creator").build();
        assertThat(config.getMetadataCreator()).isEqualTo("creator");
        assertThat(config.getMetadataAuthor()).isNull();
        assertThat(config.getMetadataTitle()).isEqualTo(TITLE);
        assertThat(config.getMetadataSubject()).isNull();
        assertThat(config.getMetadataKeywords()).isNull();
        assertThat(config.isCompressed()).isFalse();
    }

    @Test
    public void withSingleCodeSpaceless_setsMetadataKeywords() {
        codes.add(new Code("1A", "code1", null, false, 1, "c1", "", 1));
        config = new SipamatoPdfExporterConfiguration.Builder(TITLE).withCodes(codes).build();
        assertThat(config.getMetadataCreator()).isNull();
        assertThat(config.getMetadataAuthor()).isNull();
        assertThat(config.getMetadataTitle()).isEqualTo(TITLE);
        assertThat(config.getMetadataSubject()).isNull();
        assertThat(config.getMetadataKeywords()).isEqualTo("code1");
        assertThat(config.isCompressed()).isFalse();
    }

    @Test
    public void withDoubleCodesBothSpaceless_setsMetadataKeywordsCommaSeparated() {
        codes.add(new Code("1A", "code1", null, false, 1, "c1", "", 1));
        codes.add(new Code("2B", "code2", null, false, 2, "c2", "", 1));
        config = new SipamatoPdfExporterConfiguration.Builder(TITLE).withCodes(codes).build();
        assertThat(config.getMetadataKeywords()).isEqualTo("code1,code2");
    }

    @Test
    public void withTripleCodesOneWithSpaces_setsMetadataKeywordsCommaSeparatedAndPartiallyQuoted() {
        codes.add(new Code("1A", "code1", null, false, 1, "c1", "", 1));
        codes.add(new Code("2B", "code2 with spaces", null, false, 2, "c2", "", 1));
        codes.add(new Code("3C", "code3", null, false, 3, "c3", "", 1));
        config = new SipamatoPdfExporterConfiguration.Builder(TITLE).withCodes(codes).build();
        assertThat(config.getMetadataKeywords()).isEqualTo("code1,\"code2 with spaces\",code3");
    }

    @Test
    public void withCompression() {
        config = new SipamatoPdfExporterConfiguration.Builder(TITLE).withCompression().build();
        assertThat(config.getMetadataCreator()).isNull();
        assertThat(config.getMetadataAuthor()).isNull();
        assertThat(config.getMetadataTitle()).isEqualTo(TITLE);
        assertThat(config.getMetadataSubject()).isNull();
        assertThat(config.getMetadataKeywords()).isNull();
        assertThat(config.isCompressed()).isTrue();
    }

    @Test
    public void withAllAttributes() {
        codes.add(new Code("1A", "c1", null, false, 1, "c1", "", 1));
        codes.add(new Code("2B", "c2 with spaces", null, false, 2, "c2", "", 1));
        config = new SipamatoPdfExporterConfiguration.Builder("hp:", 10l).withAuthor("a").withPaperTitle("pt").withSubject("s").withCreator("c").withCodes(codes).withCompression().build();
        assertThat(config.getMetadataCreator()).isEqualTo("c");
        assertThat(config.getMetadataAuthor()).isEqualTo("a");
        assertThat(config.getMetadataTitle()).isEqualTo("hp: 10: pt");
        assertThat(config.getMetadataSubject()).isEqualTo("s");
        assertThat(config.getMetadataKeywords()).isEqualTo("c1,\"c2 with spaces\"");
        assertThat(config.isCompressed()).isTrue();
    }

}
