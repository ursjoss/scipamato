package ch.difty.scipamato.common.persistence.code;

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

@ExtendWith(MockitoExtension.class)
public class JooqCodeLikeServiceTest {
    private static final String      LANG_CODE = "en";
    private static final CodeClassId CC_ID     = CodeClassId.CC1;

    private JooqCodeLikeService<CodeLike, CodeLikeRepository<CodeLike>> service;

    private final List<CodeLike> codeClasses = new ArrayList<>();

    @Mock
    private CodeLike cclMock;

    @Mock
    private CodeLikeRepository<CodeLike> repoMock;

    @BeforeEach
    public void setUp() {
        service = new JooqCodeLikeService<>(repoMock) {
        };

        codeClasses.add(cclMock);
        codeClasses.add(cclMock);

    }

    @Test
    public void cangetRepo() {
        assertThat(service.getRepo()).isEqualTo(repoMock);
    }

    @Test
    void finding_delegatesToRepo() {
        when(repoMock.findCodesOfClass(CC_ID, LANG_CODE)).thenReturn(codeClasses);
        assertThat(service.findCodesOfClass(CC_ID, LANG_CODE)).containsExactly(cclMock, cclMock);
        verify(repoMock).findCodesOfClass(CC_ID, LANG_CODE);
    }

}
