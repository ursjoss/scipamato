package ch.difty.scipamato.core.sync.launcher

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal open class RefDataSyncJobLauncherAdHocTest {

    @Autowired
    private lateinit var launcher: RefDataSyncJobLauncher

    @Test
    fun run() {
        val result = launcher.launch()
        result.messages.forEach { println(it) }
        assertThat(result.isSuccessful).isTrue()
    }
}
