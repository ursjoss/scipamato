package ch.difty.scipamato.public_.persistence.codeclass;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.scipamato.public_.entity.CodeClass;

@RunWith(MockitoJUnitRunner.class)
public class JooqCodeClassServiceTest {

    private final JooqCodeClassService service = new JooqCodeClassService();

    @Mock
    private CodeClassRepository repoMock;

    @Test
    public void findingCodeClasss_delegatesToRepo() {
        service.setRepository(repoMock);

        String languageCodeClass = "de";

        List<CodeClass> ccs = new ArrayList<>();
        ccs.add(new CodeClass(1, "en", "cc1", ""));
        ccs.add(new CodeClass(2, "en", "cc2", ""));
        when(repoMock.find(languageCodeClass)).thenReturn(ccs);

        assertThat(extractProperty(CodeClass.NAME).from(service.find(languageCodeClass))).containsOnly("cc1", "cc2");

        verify(repoMock).find(languageCodeClass);

        verifyNoMoreInteractions(repoMock);
    }

}
