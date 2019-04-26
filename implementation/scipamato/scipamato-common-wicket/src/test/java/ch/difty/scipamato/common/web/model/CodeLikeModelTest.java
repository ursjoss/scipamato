package ch.difty.scipamato.common.web.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.common.entity.CodeLike;
import ch.difty.scipamato.common.persistence.CodeLikeService;

@ExtendWith(MockitoExtension.class)
public class CodeLikeModelTest {

    private static final String      LANG_CODE = "en";
    private static final CodeClassId CC_ID     = CodeClassId.CC1;

    private CodeLikeModel<CodeLike, CodeLikeService<CodeLike>> model;

    private final List<CodeLike> ccls = new ArrayList<>();

    @Mock
    private CodeLike cclMock;

    @Mock
    private CodeLikeService<CodeLike> serviceMock;

    @BeforeEach
    public void setUp() {
        model = new CodeLikeModel<>(CC_ID, LANG_CODE, serviceMock) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void injectThis() {
                // no-op
            }
        };

        ccls.add(cclMock);
        ccls.add(cclMock);
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
    void modelObject_gotCodeClassesFromService() {
        when(serviceMock.findCodesOfClass(CC_ID, LANG_CODE)).thenReturn(ccls);
        assertThat(model.getObject()).containsExactly(cclMock, cclMock);
        verify(serviceMock).findCodesOfClass(CC_ID, LANG_CODE);
    }

}
