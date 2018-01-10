package ch.difty.scipamato.common.web.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.difty.scipamato.common.entity.CodeClassLike;
import ch.difty.scipamato.common.persistence.CodeClassLikeService;

@RunWith(MockitoJUnitRunner.class)
public class CodeClassLikeModelTest {

    private static final String LANG_CODE = "en";

    private CodeClassLikeModel<CodeClassLike, CodeClassLikeService<CodeClassLike>> model;

    private final List<CodeClassLike> ccls = new ArrayList<>();

    @Mock
    private CodeClassLike cclMock;

    @Mock
    private CodeClassLikeService<CodeClassLike> serviceMock;

    @Before
    public void setUp() {
        model = new CodeClassLikeModel<CodeClassLike, CodeClassLikeService<CodeClassLike>>(LANG_CODE, serviceMock) {
            private static final long serialVersionUID = 1L;

            protected void injectThis() {
                // no-op
            }
        };

        ccls.add(cclMock);
        ccls.add(cclMock);

        when(serviceMock.find(LANG_CODE)).thenReturn(ccls);
    }

    @Test
    public void modelObject_gotCodeClasssesFromService() {
        assertThat(model.getObject()).containsExactly(cclMock, cclMock);
        verify(serviceMock).find(LANG_CODE);
    }

}
