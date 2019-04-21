package ch.difty.scipamato.publ.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class PublicEntityTest<T extends PublicEntity> {

    private static final LocalDateTime CREATED_DATE = LocalDateTime.parse("2017-01-01T22:15:13.111");
    private static final LocalDateTime LASTMOD_DATE = LocalDateTime.parse("2017-01-10T22:15:13.111");

    private T entity;

    T getEntity() {
        return entity;
    }

    @BeforeEach
    public final void setUp() {
        entity = newEntity();
        entity.setCreated(CREATED_DATE);
        entity.setLastModified(LASTMOD_DATE);
        entity.setVersion(10);
    }

    protected abstract T newEntity();

    @Test
    public void setGet() {
        assertSpecificGetters();

        assertThat(entity.getCreated()).isEqualTo(CREATED_DATE);
        assertThat(entity.getLastModified()).isEqualTo(LASTMOD_DATE);
        assertThat(entity.getVersion()).isEqualTo(10);
    }

    protected abstract void assertSpecificGetters();

    @Test
    public void testingToString() {
        assertThat(entity.toString()).isEqualTo(getToString());
    }

    protected abstract String getToString();

    @Test
    public void equals() {
        verifyEquals();
    }

    protected abstract void verifyEquals();

}
