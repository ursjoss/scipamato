package ch.difty.scipamato.core.sync.launcher;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RefDataSyncJobLauncherAdHocTest {

    @Autowired
    private RefDataSyncJobLauncher launcher;

    @Test
    void run() {
        SyncJobResult result = launcher.launch();
        result
            .getMessages()
            .forEach(System.out::println);
        assertThat(result.isSuccessful()).isTrue();
    }
}
