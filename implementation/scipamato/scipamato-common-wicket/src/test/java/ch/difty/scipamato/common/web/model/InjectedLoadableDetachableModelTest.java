package ch.difty.scipamato.common.web.model;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.web.WicketBaseTest;

class InjectedLoadableDetachableModelTest extends WicketBaseTest {

    @Test
    void canInstantiate_includingInjecting() {
        new InjectedLoadableDetachableModel<String>() {

            @Override
            protected List<String> load() {
                return Collections.singletonList("foo");
            }
        };
        // not actually asserting the injection took place. How would I do that?
    }
}
