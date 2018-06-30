package ch.difty.scipamato.core.sync.launcher;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Ignore
public class RefDataSyncJobLauncherAdHocTest {

    @Autowired
    private RefDataSyncJobLauncher launcher;

    @Test
    public void run() {
        SyncJobResult result = launcher.launch();
        result
            .getMessages()
            .forEach(System.out::println);
        assertThat(result.isSuccessful()).isTrue();
    }
}
