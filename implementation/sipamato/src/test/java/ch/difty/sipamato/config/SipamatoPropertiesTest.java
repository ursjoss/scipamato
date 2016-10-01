package ch.difty.sipamato.config;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.sipamato.SipamatoApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SipamatoApplication.class)
public class SipamatoPropertiesTest {

    @Autowired
    public SipamatoProperties appProperties;

    @Test
    public void gettingDefaultStrategy() {
        assertThat(appProperties.getAuthorParserStrategy()).isEqualTo(AuthorParserStrategy.DEFAULT);
    }

    @Test
    public void gettingPaperSaveMode() {
        assertThat(appProperties.getPaperSaveMode()).isEqualTo(SaveMode.AUTO);
    }
}
