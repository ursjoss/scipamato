package ch.difty.scipamato.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.scipamato.core.ScipamatoApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScipamatoApplication.class)
public class ScipamatoCorePropertiesIntegrationTest {

    @Autowired
    private ScipamatoCoreProperties scp;

    @Test
    public void brand_hasDefaultValue() {
        assertThat(scp.getBrand()).isEqualTo("SciPaMaTo - Scientific Paper Management Tool");
    }

    @Test
    public void defaultLocalization_hasDefaultEnglish() {
        assertThat(scp.getDefaultLocalization()).isEqualTo("de");
    }

    @Test
    public void pubmedBaseUrl_hasDefaultValue() {
        assertThat(scp.getPubmedBaseUrl()).isEqualTo("https://www.ncbi.nlm.nih.gov/pubmed/");
    }

    @Test
    public void authorParser_isDefault() {
        assertThat(scp.getAuthorParser()).isEqualTo("DEFAULT");
    }

    @Test
    public void paperMinimumToBeRecycled_hasDefaultValue() {
        assertThat(scp.getPaperNumberMinimumToBeRecycled()).isEqualTo(8);
    }

    @Test
    public void dbSchema_hasValuePublic() {
        assertThat(scp.getDbSchema()).isEqualTo("public");
    }

}
