package ch.difty.scipamato.core.persistence.codeclass;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.persistence.JooqTransactionalIntegrationTest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JooqCodeClassRepoIntegrationTest extends JooqTransactionalIntegrationTest {

    private static final int CODE_CLASS_COUNT = 8;

    @Autowired
    private JooqCodeClassRepo repo;

    @Test
    public void findingAllCodesClassesInGerman() {
        List<CodeClass> ccs = repo.find("de");
        assertThat(ccs).isNotEmpty()
            .hasSize(CODE_CLASS_COUNT);
        ccs.forEach((cc) -> log.debug(cc.toString()));
    }

    @Test
    public void findingAllCodesClassesInEnglish() {
        List<CodeClass> ccs = repo.find("en");
        assertThat(ccs).isNotEmpty()
            .hasSize(CODE_CLASS_COUNT);
        ccs.forEach((cc) -> log.debug(cc.toString()));
    }

    @Test
    public void findingAllCodesClassesInFrench() {
        List<CodeClass> ccs = repo.find("fr");
        assertThat(ccs).isNotEmpty()
            .hasSize(CODE_CLASS_COUNT);
        ccs.forEach((cc) -> log.debug(cc.toString()));
    }

}
