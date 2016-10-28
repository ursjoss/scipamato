package ch.difty.sipamato.persistance.jooq.codeclass;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
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

    private static final int CODE_CLASS_COUNT = 8;

    @Autowired
    private JooqCodeClassRepo repo;

    @Test
    public void findingAllCodesClassesInGerman() {
        List<CodeClass> cc = repo.find("de");
        assertThat(cc).isNotEmpty().hasSize(CODE_CLASS_COUNT);
        cc.forEach(System.out::println);
    }

    @Test
    public void findingAllCodesClassesInEnglish() {
        List<CodeClass> cc = repo.find("en");
        assertThat(cc).isNotEmpty().hasSize(CODE_CLASS_COUNT);
        cc.forEach(System.out::println);
    }

    @Test
    public void findingAllCodesClassesInFrench() {
        List<CodeClass> cc = repo.find("fr");
        assertThat(cc).isNotEmpty().hasSize(CODE_CLASS_COUNT);
        cc.forEach(System.out::println);
    }

}
