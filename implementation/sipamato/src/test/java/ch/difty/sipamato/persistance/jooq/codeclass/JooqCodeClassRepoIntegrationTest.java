package ch.difty.sipamato.persistance.jooq.codeclass;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.sipamato.SipamatoApplication;
import ch.difty.sipamato.entity.CodeClass;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SipamatoApplication.class)
@ActiveProfiles({ "DB_JOOQ" })
public class JooqCodeClassRepoIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JooqCodeClassRepoIntegrationTest.class);

    private static final int CODE_CLASS_COUNT = 8;

    @Autowired
    private JooqCodeClassRepo repo;

    @Test
    public void findingAllCodesClassesInGerman() {
        List<CodeClass> ccs = repo.find("de");
        assertThat(ccs).isNotEmpty().hasSize(CODE_CLASS_COUNT);
        ccs.forEach((cc) -> LOGGER.debug(cc.toString()));
    }

    @Test
    public void findingAllCodesClassesInEnglish() {
        List<CodeClass> ccs = repo.find("en");
        assertThat(ccs).isNotEmpty().hasSize(CODE_CLASS_COUNT);
        ccs.forEach((cc) -> LOGGER.debug(cc.toString()));
    }

    @Test
    public void findingAllCodesClassesInFrench() {
        List<CodeClass> ccs = repo.find("fr");
        assertThat(ccs).isNotEmpty().hasSize(CODE_CLASS_COUNT);
        ccs.forEach((cc) -> LOGGER.debug(cc.toString()));
    }

}
