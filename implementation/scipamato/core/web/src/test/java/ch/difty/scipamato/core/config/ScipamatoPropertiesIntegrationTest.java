package ch.difty.scipamato.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ch.difty.scipamato.core.ScipamatoCoreApplication;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ScipamatoCoreApplication.class)
class ScipamatoPropertiesIntegrationTest {

    @Autowired
    private ScipamatoProperties sp;

    @Test
    void brand_hasDefaultValue() {
        assertThat(sp.getBrand()).isEqualTo("SciPaMaTo");
    }

    @Test
    void defaultLocalization_hasDefaultEnglish() {
        assertThat(sp.getDefaultLocalization()).isEqualTo("de");
    }

    @Test
    void pubmedBaseUrl_hasDefaultValue() {
        assertThat(sp.getPubmedBaseUrl()).isEqualTo("https://www.ncbi.nlm.nih.gov/pubmed/");
    }

    @Test
    void authorParser_isDefault() {
        assertThat(sp.getAuthorParser()).isEqualTo("PUBMED");
    }

    @Test
    void paperMinimumToBeRecycled_hasDefaultValue() {
        assertThat(sp.getPaperNumberMinimumToBeRecycled()).isEqualTo(8);
    }

    @Test
    void dbSchema_hasValuePublic() {
        assertThat(sp.getDbSchema()).isEqualTo("public");
    }

    @Test
    void gettingMultiSelectBoxActionBoxWithMoreEntriesThan_hasDefaultValue4() {
        assertThat(sp.getMultiSelectBoxActionBoxWithMoreEntriesThan()).isEqualTo(4);
    }

    @Test
    void gettingPubmedApiKey_hasNoDefaultValue() {
        assertThat(sp.getPubmedApiKey()).isNull();
    }
}
