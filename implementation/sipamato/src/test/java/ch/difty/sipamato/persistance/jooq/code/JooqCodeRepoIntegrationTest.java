package ch.difty.sipamato.persistance.jooq.code;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.sipamato.SipamatoApplication;
import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.CodeClassId;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SipamatoApplication.class)
@ActiveProfiles({ "DB_JOOQ" })
public class JooqCodeRepoIntegrationTest {

    @Autowired
    private JooqCodeRepo repo;

    @Test
    public void findingAllCodes1InGerman() {
        List<Code> codesOfClass1 = repo.findCodesOfClass(CodeClassId.CC1, "de");
        assertThat(codesOfClass1).isNotEmpty().hasSize(21);
        codesOfClass1.forEach(System.out::println);
    }

    @Test
    public void findingAllCodes2InEnglish() {
        List<Code> codesOfClass1 = repo.findCodesOfClass(CodeClassId.CC2, "en");
        assertThat(codesOfClass1).isNotEmpty().hasSize(2);
        codesOfClass1.forEach(System.out::println);
    }

    @Test
    public void findingAllCodes3InEnglish() {
        List<Code> codesOfClass1 = repo.findCodesOfClass(CodeClassId.CC3, "fr");
        assertThat(codesOfClass1).isNotEmpty().hasSize(14);
        codesOfClass1.forEach(System.out::println);
    }

}
