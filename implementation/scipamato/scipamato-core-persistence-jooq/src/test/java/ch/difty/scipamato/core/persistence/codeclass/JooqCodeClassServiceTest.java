package ch.difty.scipamato.core.persistence.codeclass;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.scipamato.core.entity.CodeClass;

@RunWith(MockitoJUnitRunner.class)
public class JooqCodeClassServiceTest {

    private JooqCodeClassService service;

    @Mock
    private CodeClassRepository repoMock;

    @Test
    public void findingCodeClasss_delegatesToRepo() {
        service = new JooqCodeClassService(repoMock);

        String languageCodeClass = "de";

        List<CodeClass> ccs = new ArrayList<>();
        ccs.add(new CodeClass(1, "cc1", ""));
        ccs.add(new CodeClass(2, "cc2", ""));
        when(repoMock.find(languageCodeClass)).thenReturn(ccs);

        assertThat(extractProperty(CodeClass.NAME).from(service.find(languageCodeClass))).containsOnly("cc1", "cc2");

        verify(repoMock).find(languageCodeClass);

        verifyNoMoreInteractions(repoMock);
    }
}
