package ch.difty.scipamato.persistence.codeclass;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.entity.CodeClass;
import ch.difty.scipamato.persistence.JooqTransactionalIntegrationTest;

public class JooqCodeClassRepoIntegrationTest extends JooqTransactionalIntegrationTest {

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
