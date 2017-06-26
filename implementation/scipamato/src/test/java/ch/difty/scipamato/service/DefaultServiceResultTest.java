package ch.difty.scipamato.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class DefaultServiceResultTest {

    private final ServiceResult sr = new DefaultServiceResult();

    @Test
    public void defaultServiceResult_hasNoMessages() {
        assertThat(sr.getInfoMessages()).isEmpty();
        assertThat(sr.getWarnMessages()).isEmpty();
        assertThat(sr.getErrorMessages()).isEmpty();
    }

    @Test
    public void addingInfoMessages() {
        sr.addInfoMessage("foo");
        assertThat(sr.getInfoMessages()).containsOnly("foo");
        assertThat(sr.getWarnMessages()).isEmpty();
        assertThat(sr.getErrorMessages()).isEmpty();

        sr.addInfoMessage("bar");
        assertThat(sr.getInfoMessages()).containsOnly("foo", "bar");
        assertThat(sr.getWarnMessages()).isEmpty();
        assertThat(sr.getErrorMessages()).isEmpty();
    }

    @Test
    public void addingWarnMessages() {
        sr.addWarnMessage("foo");
        assertThat(sr.getWarnMessages()).containsOnly("foo");
        assertThat(sr.getInfoMessages()).isEmpty();
        assertThat(sr.getErrorMessages()).isEmpty();

        sr.addWarnMessage("bar");
        assertThat(sr.getWarnMessages()).containsOnly("foo", "bar");
        assertThat(sr.getInfoMessages()).isEmpty();
        assertThat(sr.getErrorMessages()).isEmpty();
    }

    @Test
    public void addingErrorMessages() {
        sr.addErrorMessage("foo");
        assertThat(sr.getErrorMessages()).containsOnly("foo");
        assertThat(sr.getInfoMessages()).isEmpty();
        assertThat(sr.getWarnMessages()).isEmpty();

        sr.addErrorMessage("bar");
        assertThat(sr.getErrorMessages()).containsOnly("foo", "bar");
        assertThat(sr.getInfoMessages()).isEmpty();
        assertThat(sr.getWarnMessages()).isEmpty();
    }

    @Test
    public void ignoringNullMessages() {
        sr.addInfoMessage(null);
        sr.addWarnMessage(null);
        sr.addErrorMessage(null);

        assertThat(sr.getInfoMessages()).isEmpty();
        assertThat(sr.getWarnMessages()).isEmpty();
        assertThat(sr.getErrorMessages()).isEmpty();
    }
}
