package ch.difty.scipamato.common.web.event

import com.nhaarman.mockitokotlin2.mock
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.apache.wicket.ajax.AjaxRequestTarget
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verifyNoMoreInteractions

internal class WicketEventTest {

    private val targetMock = mock<AjaxRequestTarget>()

    private val e: WicketEvent = object : WicketEvent(targetMock) {}

    @AfterEach
    fun tearDown() {
        verifyNoMoreInteractions(targetMock)
    }

    @Test
    fun test() {
        assertThat(e.target).isEqualTo(targetMock)
    }

    @Test
    fun equals() {
        EqualsVerifier.forClass(WicketEvent::class.java)
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }

    @Test
    fun testingToString() {
        assertThat(e.toString()).startsWith("WicketEvent(target=Mock for AjaxRequestTarget")
    }
}
