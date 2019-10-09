package ch.difty.scipamato.publ.persistence

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.common.logger
import ch.difty.scipamato.publ.persistence.code.JooqCodeRepo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.testcontainers.junit.jupiter.Testcontainers

private val log = logger()

@Suppress("MagicNumber")
@JooqTest
@Testcontainers
internal open class JooqCodeRepoIntegrationTest {

    @Autowired
    private lateinit var repo: JooqCodeRepo

    @Test
    fun findingAllCodes1InGerman() {
        val codesOfClass1 = repo.findCodesOfClass(CodeClassId.CC1, "de")
        assertThat(codesOfClass1).hasSize(18)
        codesOfClass1.forEach { c -> log.debug(c.toString()) }
    }

    @Test
    fun findingAllCodes2InEnglish() {
        val codesOfClass1 = repo.findCodesOfClass(CodeClassId.CC2, "en")
        assertThat(codesOfClass1).hasSize(2)
        codesOfClass1.forEach { c -> log.debug(c.toString()) }
    }

    @Test
    fun findingAllCodes3InEnglish() {
        val codesOfClass1 = repo.findCodesOfClass(CodeClassId.CC3, "fr")
        assertThat(codesOfClass1).hasSize(12)
        codesOfClass1.forEach { c -> log.debug(c.toString()) }
    }
}
