package ch.difty.scipamato.publ.persistence.code;

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
import org.mockito.junit.MockitoJUnitRunner;

import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.publ.entity.Code;

@RunWith(MockitoJUnitRunner.class)
public class JooqCodeServiceTest {

    private JooqCodeService service;

    @Mock
    private CodeRepository repoMock;

    @Test
    public void findingCodes_delegatesToRepo() {
        service = new JooqCodeService(repoMock);

        CodeClassId ccId = CodeClassId.CC1;
        String languageCode = "de";

        List<Code> codes = new ArrayList<>();
        codes.add(new Code(1, "c1", "en", "Code1", null, 1));
        codes.add(new Code(1, "c2", "en", "Code2", null, 2));
        when(repoMock.findCodesOfClass(ccId, languageCode)).thenReturn(codes);

        assertThat(extractProperty(Code.CodeFields.CODE.getName()).from(service.findCodesOfClass(ccId, languageCode)))
            .containsOnly("c1", "c2");

        verify(repoMock).findCodesOfClass(ccId, languageCode);

        verifyNoMoreInteractions(repoMock);
    }
}
