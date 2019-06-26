package ch.difty.scipamato.common;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;
import org.springframework.core.GenericTypeResolver;

/**
 * Derive a test class from this abstract class if the tested has a private
 * constructor.
 *
 * @param <C>
 *     the class to be tested
 * @author u.joss
 * @see <a href=
 *     "http://stackoverflow.com/questions/4520216/how-to-add-test-coverage-to-a-private-constructor">http://stackoverflow.com/questions/4520216/how-to-add-test-coverage-to-a-private-constructor</a>
 * @see <a href=
 *     "http://stackoverflow.com/questions/3437897/how-to-get-a-class-instance-of-generics-type-t">http://stackoverflow.com/questions/3437897/how-to-get-a-class-instance-of-generics-type-t</a>
 */
public interface FinalClassTest<C> {

    @Test
    default void testConstructorIsPrivate()
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        @SuppressWarnings("unchecked") Class<C> clazz = (Class<C>) GenericTypeResolver.resolveTypeArgument(getClass(),
            FinalClassTest.class);
        Constructor<C> constructor = clazz.getDeclaredConstructor();
        assertThat(constructor).isNotNull();
        assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();
        constructor.setAccessible(true);
        constructor.newInstance();
    }

}
