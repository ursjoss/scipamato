package ch.difty.scipamato.persistance.jooq.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

import javax.cache.annotation.CacheInvocationParameter;
import javax.cache.annotation.CacheKeyInvocationContext;
import javax.cache.annotation.GeneratedCacheKey;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.scipamato.auth.Role;
import ch.difty.scipamato.entity.User;
import ch.difty.scipamato.lib.NullArgumentException;
import ch.difty.scipamato.persistance.DefaultGeneratedCacheKey;

@RunWith(MockitoJUnitRunner.class)
public class UserNameOfUserCacheKeyGeneratorTest {

    private static final String USER_NAME = "un";
    private static final String NOF_ERROR_MSG = "No user argument found in invocation context";

    private final User user = new User(1, USER_NAME, "fn", "ln", "em", "pw", true, Arrays.asList(Role.USER, Role.ADMIN));

    private final UserNameOfUserCacheKeyGenerator generator = new UserNameOfUserCacheKeyGenerator();

    @Mock
    private CacheKeyInvocationContext<? extends Annotation> ctxMock;

    private CacheInvocationParameter cip = new CacheInvocationParameter() {

        @Override
        public Object getValue() {
            return user;
        }

        @Override
        public Class<?> getRawType() {
            return User.class;
        }

        @Override
        public int getParameterPosition() {
            return 0;
        }

        @Override
        public Set<Annotation> getAnnotations() {
            return null;
        }
    };

    @Test
    public void instantiatingWithNullContext_throws() {
        try {
            generator.generateCacheKey(null);
            fail("should havethrown Exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("ctx must not be null.");
        }
    }

    @Test
    public void generatingCacheKey_withNoCacheInvocationParameter_throws() {
        CacheInvocationParameter[] cips = new CacheInvocationParameter[0];
        when(ctxMock.getAllParameters()).thenReturn(cips);
        try {
            generator.generateCacheKey(ctxMock);
            fail("should havethrown Exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class).hasMessage(NOF_ERROR_MSG);
        }
    }

    @Test
    public void generatingCacheKey_withCacheInvocationParameterForNonUserClass_throws() {
        CacheInvocationParameter[] cips = new CacheInvocationParameter[1];
        cips[0] = new CacheInvocationParameter() {

            @Override
            public Object getValue() {
                return "foo";
            }

            @Override
            public Class<?> getRawType() {
                return String.class;
            }

            @Override
            public int getParameterPosition() {
                return 0;
            }

            @Override
            public Set<Annotation> getAnnotations() {
                return null;
            }
        };
        when(ctxMock.getAllParameters()).thenReturn(cips);
        try {
            generator.generateCacheKey(ctxMock);
            fail("should havethrown Exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class).hasMessage(NOF_ERROR_MSG);
        }
    }

    @Test
    public void generatingCacheKey_withValidCacheInvocationParameter_() {
        CacheInvocationParameter[] cips = new CacheInvocationParameter[1];
        cips[0] = cip;
        when(ctxMock.getAllParameters()).thenReturn(cips);
        GeneratedCacheKey gck = generator.generateCacheKey(ctxMock);
        assertThat(gck).isNotNull();

        Object[] names = new Object[1];
        names[0] = USER_NAME;
        GeneratedCacheKey dgck = new DefaultGeneratedCacheKey(names);
        assertThat(gck.equals(dgck)).isTrue();
    }
}
