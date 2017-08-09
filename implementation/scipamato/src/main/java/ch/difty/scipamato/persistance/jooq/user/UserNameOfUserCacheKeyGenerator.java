package ch.difty.scipamato.persistance.jooq.user;

import java.lang.annotation.Annotation;

import javax.cache.annotation.CacheInvocationParameter;
import javax.cache.annotation.CacheKeyGenerator;
import javax.cache.annotation.CacheKeyInvocationContext;
import javax.cache.annotation.GeneratedCacheKey;

import ch.difty.scipamato.entity.User;
import ch.difty.scipamato.lib.AssertAs;
import ch.difty.scipamato.persistance.DefaultGeneratedCacheKey;

/**
 * CacheKeyGenerator extracting the user name from the {@link User} object as key.
 *
 * @author u.joss
 */
public class UserNameOfUserCacheKeyGenerator implements CacheKeyGenerator {

    @Override
    public GeneratedCacheKey generateCacheKey(final CacheKeyInvocationContext<? extends Annotation> ctx) {
        AssertAs.notNull(ctx, "ctx");
        final CacheInvocationParameter[] allParameters = ctx.getAllParameters();
        for (final CacheInvocationParameter parameter : allParameters) {
            if (User.class.equals(parameter.getRawType())) {
                final User user = User.class.cast(parameter.getValue());
                return new DefaultGeneratedCacheKey(new Object[] { user.getUserName() });
            }
        }
        throw new IllegalArgumentException("No user argument found in invocation context");
    }
}