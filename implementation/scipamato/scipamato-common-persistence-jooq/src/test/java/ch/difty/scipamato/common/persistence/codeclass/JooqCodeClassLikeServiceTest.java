package ch.difty.scipamato.common.persistence.codeclass;

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

@RunWith(MockitoJUnitRunner.class)
public class JooqCodeClassLikeServiceTest {

    private static final String LANG_CODE = "en";

    private JooqCodeClassLikeService<CodeClassLike, CodeClassLikeRepository<CodeClassLike>> service;

    private final List<CodeClassLike> codeClasses = new ArrayList<>();

    @Mock
    private CodeClassLike cclMock;

    @Mock
    private CodeClassLikeRepository<CodeClassLike> repoMock;

    @Before
    public void setUp() {
        service = new JooqCodeClassLikeService<CodeClassLike, CodeClassLikeRepository<CodeClassLike>>(repoMock) {
            private static final long serialVersionUID = 1L;
        };

        codeClasses.add(cclMock);
        codeClasses.add(cclMock);

        when(repoMock.find(LANG_CODE)).thenReturn(codeClasses);
    }

    @Test
    public void finding_delegatesToRepo() {
        assertThat(service.find(LANG_CODE)).containsExactly(cclMock, cclMock);
        verify(repoMock).find(LANG_CODE);
    }
}
