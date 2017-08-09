package ch.difty.scipamato.persistance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import javax.cache.annotation.CacheInvocationParameter;
import javax.cache.annotation.GeneratedCacheKey;

import org.junit.Test;

import ch.difty.scipamato.lib.NullArgumentException;

public class DefaultGeneratedCacheKeyTest {

    @Test
    public void constructingWithNullObjectArray_throws() {
        Object[] keyValues = null;
        try {
            new DefaultGeneratedCacheKey(keyValues);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("keyValues must not be null.");
        }
    }

    private Object[] newObjectArray(Object... objects) {
        Object[] oa = new Object[objects.length];
        int i = 0;
        for (Object o : objects)
            oa[i++] = o;
        return oa;
    }

    private void assertEquality(GeneratedCacheKey ck1, GeneratedCacheKey ck2) {
        assertThat(ck1.hashCode()).isEqualTo(ck2.hashCode());
        assertThat(ck1.equals(ck2)).isTrue();
    }

    private void assertInequality(GeneratedCacheKey ck1, GeneratedCacheKey ck2) {
        assertThat(ck1.hashCode()).isNotEqualTo(ck2.hashCode());
        assertThat(ck1.equals(ck2)).isFalse();
    }

    @Test
    public void noKeyObjects_equal() {
        GeneratedCacheKey gck1 = new DefaultGeneratedCacheKey(newObjectArray());
        GeneratedCacheKey gck2 = new DefaultGeneratedCacheKey(newObjectArray());
        assertEquality(gck1, gck2);
    }

    @Test
    public void singleKeyObjectsWithSameKey_equal() {
        GeneratedCacheKey gck1 = new DefaultGeneratedCacheKey(newObjectArray("foo"));
        GeneratedCacheKey gck2 = new DefaultGeneratedCacheKey(newObjectArray("foo"));
        assertEquality(gck1, gck2);
    }

    @Test
    public void singleKeyObjectsWithNullKeys_equal() {
        GeneratedCacheKey gck1 = new DefaultGeneratedCacheKey(newObjectArray((Object) null));
        GeneratedCacheKey gck2 = new DefaultGeneratedCacheKey(newObjectArray((Object) null));
        assertEquality(gck1, gck2);
    }

    @Test
    public void singleKeyObjectsWithNullAndNonNullKeys_differ() {
        GeneratedCacheKey gck1 = new DefaultGeneratedCacheKey(newObjectArray((Object) null));
        GeneratedCacheKey gck2 = new DefaultGeneratedCacheKey(newObjectArray("bar"));
        assertInequality(gck1, gck2);
    }

    @Test
    public void singleKeyObjectsWithDifferingKey_differ() {
        GeneratedCacheKey gck1 = new DefaultGeneratedCacheKey(newObjectArray("foo"));
        GeneratedCacheKey gck2 = new DefaultGeneratedCacheKey(newObjectArray("bar"));
        assertInequality(gck1, gck2);
    }

    @Test
    public void singleKeyObjectsWithDifferingObjectSize_differ() {
        GeneratedCacheKey gck1 = new DefaultGeneratedCacheKey(newObjectArray("foo"));
        Object[] kv2 = new Object[2];
        kv2[0] = "foo";
        GeneratedCacheKey gck2 = new DefaultGeneratedCacheKey(kv2);
        assertInequality(gck1, gck2);
    }

    @Test
    public void comparingWithNull_differs() {
        GeneratedCacheKey gck1 = new DefaultGeneratedCacheKey(newObjectArray("foo"));
        assertThat(gck1.equals(null)).isFalse();
    }

    @Test
    public void comparingWithSelf_equals() {
        GeneratedCacheKey gck1 = new DefaultGeneratedCacheKey(newObjectArray("foo"));
        assertThat(gck1.equals(gck1)).isTrue();
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void comparingWithDifferentClass_differs() {
        GeneratedCacheKey gck1 = new DefaultGeneratedCacheKey(newObjectArray("foo"));
        assertThat(gck1.equals("foo")).isFalse();
    }

    @Test
    public void comparingSameValues_equals() {
        GeneratedCacheKey gck1 = new DefaultGeneratedCacheKey(newObjectArray("foo", "bar", "baz"));
        GeneratedCacheKey gck2 = new DefaultGeneratedCacheKey(newObjectArray("foo", "bar", "baz"));
        assertThat(gck1.equals(gck2)).isTrue();
    }

    @Test
    public void comparingDifferingValues_differs() {
        GeneratedCacheKey gck1 = new DefaultGeneratedCacheKey(newObjectArray("foo", "bar", "baz"));
        GeneratedCacheKey gck2 = new DefaultGeneratedCacheKey(newObjectArray("foo", "bam", "baz"));
        assertThat(gck1.equals(gck2)).isFalse();
    }

    @Test
    public void constructingWithNullCacheInvocationParameterArray_throws() {
        CacheInvocationParameter[] cips = null;
        try {
            new DefaultGeneratedCacheKey(cips);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("cips must not be null.");
        }
    }

}
