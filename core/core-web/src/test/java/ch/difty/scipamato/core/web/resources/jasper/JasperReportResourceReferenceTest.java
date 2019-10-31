package ch.difty.scipamato.core.web.resources.jasper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.web.WicketTest;

abstract class JasperReportResourceReferenceTest<E extends JasperReportResourceReference> extends WicketTest {

    private static final String TAG    = "jrxml";
    private static final String DOTTAG = "." + TAG;

    /**
     * Implement to provide the resource reference under test
     */
    protected abstract E getResourceReference();

    /**
     * @return the base name (without extension) of the report as defined within the
     *     jrxml file.
     */
    protected abstract String getReportBaseName();

    /**
     * @return the full package path and class name of the resource reference
     */
    protected abstract String getResourceReferencePath();

    @Test
    void testDefaultAttributes() {
        E ref = getResourceReference();
        assertThat(ref.getScope()).isEqualTo(getResourceReference().getClass());
        assertThat(ref.getName()).isEqualTo(getReportBaseName() + DOTTAG);
        assertThat(ref.getExtension()).isEqualTo(TAG);
        assertThat(ref.getDependencies()).isEmpty();
        assertThat(ref.getLocale()).isNull();
    }

    @Test
    void testKey() {
        E ref = getResourceReference();
        assertThat(ref
            .getKey()
            .getScope()).isEqualTo(getResourceReferencePath());
        assertThat(ref
            .getKey()
            .getName()).isEqualTo(getReportBaseName() + DOTTAG);
        assertThat(ref
            .getKey()
            .getLocale()).isNull();
        assertThat(ref
            .getKey()
            .getStyle()).isNull();
        assertThat(ref
            .getKey()
            .getVariation()).isNull();
    }

    @Test
    void gettingReport() {
        E ref = getResourceReference();
        assertThat(ref
            .getReport()
            .getName()).isEqualTo(getReportBaseName());
    }

    /**
     * In production the reports should be cached for performance reasons (it does
     * not need to be recompiled every time). For development purposes the caching
     * can be omitted. This test indicates that the cache flag should be reset to
     * true...
     */
    @Test
    void doCacheReport() {
        E ref = getResourceReference();
        assertThat(ref.isCacheReport()).isTrue();
    }
}
