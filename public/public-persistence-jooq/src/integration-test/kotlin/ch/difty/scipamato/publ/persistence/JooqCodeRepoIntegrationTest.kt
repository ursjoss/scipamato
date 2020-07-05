package ch.difty.scipamato.publ.persistence

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.common.logger
import ch.difty.scipamato.publ.persistence.code.JooqCodeRepo
import org.amshove.kluent.shouldHaveSize
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
        codesOfClass1 shouldHaveSize 18
        codesOfClass1.forEach { c -> log.debug(c.toString()) }
    }

    @Test
    fun findingAllCodes2InEnglish() {
        val codesOfClass1 = repo.findCodesOfClass(CodeClassId.CC2, "en")
        codesOfClass1 shouldHaveSize 2
        codesOfClass1.forEach { c -> log.debug(c.toString()) }
    }

    @Test
    fun findingAllCodes3InEnglish() {
        val codesOfClass1 = repo.findCodesOfClass(CodeClassId.CC3, "fr")
        codesOfClass1 shouldHaveSize 12
        codesOfClass1.forEach { c -> log.debug(c.toString()) }
    }
}
