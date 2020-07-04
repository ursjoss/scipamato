package ch.difty.scipamato.core.web.resources.jasper

import ch.difty.scipamato.core.web.WicketTest
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
internal abstract class JasperReportResourceReferenceTest<E : JasperReportResourceReference> : WicketTest() {

    /**
     * Implement to provide the resource reference under test
     */
    protected abstract val resourceReference: E

    /**
     * @return the base name (without extension) of the report as defined within the
     * jrxml file.
     */
    protected abstract val reportBaseName: String

    /**
     * @return the full package path and class name of the resource reference
     */
    protected abstract val resourceReferencePath: String?

    @Test
    fun testDefaultAttributes() {
        val ref = resourceReference
        ref.scope shouldBeEqualTo resourceReference.javaClass
        ref.name shouldBeEqualTo reportBaseName + DOTTAG
        ref.extension shouldBeEqualTo TAG
        ref.dependencies.shouldBeEmpty()
        ref.locale.shouldBeNull()
    }

    @Test
    fun testKey() {
        val ref = resourceReference
        ref.key.scope shouldBeEqualTo resourceReferencePath
        ref.key.name shouldBeEqualTo reportBaseName + DOTTAG
        ref.key.locale.shouldBeNull()
        ref.key.style.shouldBeNull()
        ref.key.variation.shouldBeNull()
    }

    @Test
    fun gettingReport() {
        val ref = resourceReference
        ref.report.name shouldBeEqualTo reportBaseName
    }

    /**
     * In production the reports should be cached for performance reasons (it does
     * not need to be recompiled every time). For development purposes the caching
     * can be omitted. This test indicates that the cache flag should be reset to
     * true...
     */
    @Test
    fun doCacheReport() {
        val ref = resourceReference
        ref.isCacheReport.shouldBeTrue()
    }

    companion object {
        private const val TAG = "jrxml"
        private const val DOTTAG = ".$TAG"
    }
}
