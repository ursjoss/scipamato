package ch.difty.scipamato.publ.web.model

import ch.difty.scipamato.publ.entity.Keyword
import ch.difty.scipamato.publ.persistence.api.KeywordService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test
import java.util.ArrayList

internal class KeywordModelTest : ModelTest() {

    @MockkBean
    private lateinit var serviceMock: KeywordService

    @Test
    fun loading_delegatesToService() {
        val languageCode = "de"
        val keywords: MutableList<Keyword> = ArrayList()
        keywords.add(Keyword(10, 1, "en", "k1", null))
        keywords.add(Keyword(11, 2, "en", "k2", null))

        every { serviceMock.findKeywords(languageCode) } returns keywords

        val model = KeywordModel("de")
        model.load().map { it.name } shouldContainSame listOf("k1", "k2")

        verify { serviceMock.findKeywords(languageCode) }
        confirmVerified(serviceMock)
    }
}
