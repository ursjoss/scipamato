package ch.difty.scipamato.core.persistence

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DefaultServiceResultTest {

    private val sr = DefaultServiceResult()

    @Test
    fun defaultServiceResult_hasNoMessages() {
        assertThat(sr.infoMessages).isEmpty()
        assertThat(sr.warnMessages).isEmpty()
        assertThat(sr.errorMessages).isEmpty()
    }

    @Test
    fun addingInfoMessages() {
        sr.addInfoMessage("foo")
        assertThat(sr.infoMessages).containsOnly("foo")
        assertThat(sr.warnMessages).isEmpty()
        assertThat(sr.errorMessages).isEmpty()

        sr.addInfoMessage("bar")
        assertThat(sr.infoMessages).containsOnly("foo", "bar")
        assertThat(sr.warnMessages).isEmpty()
        assertThat(sr.errorMessages).isEmpty()
    }

    @Test
    fun addingWarnMessages() {
        sr.addWarnMessage("foo")
        assertThat(sr.warnMessages).containsOnly("foo")
        assertThat(sr.infoMessages).isEmpty()
        assertThat(sr.errorMessages).isEmpty()

        sr.addWarnMessage("bar")
        assertThat(sr.warnMessages).containsOnly("foo", "bar")
        assertThat(sr.infoMessages).isEmpty()
        assertThat(sr.errorMessages).isEmpty()
    }

    @Test
    fun addingErrorMessages() {
        sr.addErrorMessage("foo")
        assertThat(sr.errorMessages).containsOnly("foo")
        assertThat(sr.infoMessages).isEmpty()
        assertThat(sr.warnMessages).isEmpty()

        sr.addErrorMessage("bar")
        assertThat(sr.errorMessages).containsOnly("foo", "bar")
        assertThat(sr.infoMessages).isEmpty()
        assertThat(sr.warnMessages).isEmpty()
    }

    @Test
    fun ignoringNullMessages() {
        sr.addInfoMessage(null)
        sr.addWarnMessage(null)
        sr.addErrorMessage(null)

        assertThat(sr.infoMessages).isEmpty()
        assertThat(sr.warnMessages).isEmpty()
        assertThat(sr.errorMessages).isEmpty()
    }
}
