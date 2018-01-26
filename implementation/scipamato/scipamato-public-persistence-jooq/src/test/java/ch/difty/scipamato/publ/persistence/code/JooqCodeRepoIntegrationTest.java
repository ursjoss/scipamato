package ch.difty.scipamato.publ.persistence.code;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.publ.entity.Code;
import ch.difty.scipamato.publ.persistence.JooqTransactionalIntegrationTest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JooqCodeRepoIntegrationTest extends JooqTransactionalIntegrationTest {

    @Autowired
    private JooqCodeRepo repo;

    @Test
    public void findingAllCodes1InGerman() {
        List<Code> codesOfClass1 = repo.findCodesOfClass(CodeClassId.CC1, "de");
        assertThat(codesOfClass1).isNotEmpty()
            .hasSize(18);
        codesOfClass1.forEach((c) -> log.debug(c.toString()));
    }

    @Test
    public void findingAllCodes2InEnglish() {
        List<Code> codesOfClass1 = repo.findCodesOfClass(CodeClassId.CC2, "en");
        assertThat(codesOfClass1).isNotEmpty()
            .hasSize(2);
        codesOfClass1.forEach((c) -> log.debug(c.toString()));
    }

    @Test
    public void findingAllCodes3InEnglish() {
        List<Code> codesOfClass1 = repo.findCodesOfClass(CodeClassId.CC3, "fr");
        assertThat(codesOfClass1).isNotEmpty()
            .hasSize(12);
        codesOfClass1.forEach((c) -> log.debug(c.toString()));
    }

}
