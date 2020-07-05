package ch.difty.scipamato.core.persistence

import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test

internal class DefaultServiceResultTest {

    private val sr = DefaultServiceResult()

    @Test
    fun defaultServiceResult_hasNoMessages() {
        sr.infoMessages.shouldBeEmpty()
        sr.warnMessages.shouldBeEmpty()
        sr.errorMessages.shouldBeEmpty()
    }

    @Test
    fun addingInfoMessages() {
        sr.addInfoMessage("foo")
        sr.infoMessages shouldContainSame listOf("foo")
        sr.warnMessages.shouldBeEmpty()
        sr.errorMessages.shouldBeEmpty()

        sr.addInfoMessage("bar")
        sr.infoMessages shouldContainSame listOf("foo", "bar")
        sr.warnMessages.shouldBeEmpty()
        sr.errorMessages.shouldBeEmpty()
    }

    @Test
    fun addingWarnMessages() {
        sr.addWarnMessage("foo")
        sr.warnMessages shouldContainSame listOf("foo")
        sr.infoMessages.shouldBeEmpty()
        sr.errorMessages.shouldBeEmpty()

        sr.addWarnMessage("bar")
        sr.warnMessages shouldContainSame listOf("foo", "bar")
        sr.infoMessages.shouldBeEmpty()
        sr.errorMessages.shouldBeEmpty()
    }

    @Test
    fun addingErrorMessages() {
        sr.addErrorMessage("foo")
        sr.errorMessages shouldContainSame listOf("foo")
        sr.infoMessages.shouldBeEmpty()
        sr.warnMessages.shouldBeEmpty()

        sr.addErrorMessage("bar")
        sr.errorMessages shouldContainSame listOf("foo", "bar")
        sr.infoMessages.shouldBeEmpty()
        sr.warnMessages.shouldBeEmpty()
    }

    @Test
    fun ignoringNullMessages() {
        sr.addInfoMessage(null)
        sr.addWarnMessage(null)
        sr.addErrorMessage(null)

        sr.infoMessages.shouldBeEmpty()
        sr.warnMessages.shouldBeEmpty()
        sr.errorMessages.shouldBeEmpty()
    }
}
