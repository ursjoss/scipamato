package ch.difty.scipamato.common.web.model;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.web.WicketBaseTest;

public class InjectedLoadableDetachableModelTest extends WicketBaseTest {

    @Test
    public void canInstantiate_includingInjecting() {
        new InjectedLoadableDetachableModel<String>() {

            @Override
            protected List<String> load() {
                return Arrays.asList("foo");
            }
        };
        // not actually asserting the injection took place. How would I do that?
    }
}
