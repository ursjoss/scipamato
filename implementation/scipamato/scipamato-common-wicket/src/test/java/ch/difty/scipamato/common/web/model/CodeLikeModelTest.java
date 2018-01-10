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

import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.common.entity.CodeLike;
import ch.difty.scipamato.common.persistence.CodeLikeService;

@RunWith(MockitoJUnitRunner.class)
public class CodeLikeModelTest {

    private static final String      LANG_CODE = "en";
    private static final CodeClassId CC_ID     = CodeClassId.CC1;

    private CodeLikeModel<CodeLike, CodeLikeService<CodeLike>> model;

    private final List<CodeLike> ccls = new ArrayList<>();

    @Mock
    private CodeLike cclMock;

    @Mock
    private CodeLikeService<CodeLike> serviceMock;

    @Before
    public void setUp() {
        model = new CodeLikeModel<CodeLike, CodeLikeService<CodeLike>>(CC_ID, LANG_CODE, serviceMock) {
            private static final long serialVersionUID = 1L;

            protected void injectThis() {
                // no-op
            }
        };

        ccls.add(cclMock);
        ccls.add(cclMock);

        when(serviceMock.findCodesOfClass(CC_ID, LANG_CODE)).thenReturn(ccls);
    }

    @Test
    public void canGetCodeClass() {
        assertThat(model.getCodeClassId()).isEqualTo(CC_ID);
    }

    @Test
    public void canGetLanguageCode() {
        assertThat(model.getLanguageCode()).isEqualTo(LANG_CODE);
    }

    @Test
    public void modelObject_gotCodeClasssesFromService() {
        assertThat(model.getObject()).containsExactly(cclMock, cclMock);
        verify(serviceMock).findCodesOfClass(CC_ID, LANG_CODE);
    }

}
