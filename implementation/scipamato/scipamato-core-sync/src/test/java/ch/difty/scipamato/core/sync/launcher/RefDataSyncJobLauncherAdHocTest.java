package ch.difty.scipamato.core.sync.launcher;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Disabled
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
