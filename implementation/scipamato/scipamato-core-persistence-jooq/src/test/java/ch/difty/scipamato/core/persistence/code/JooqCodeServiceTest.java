package ch.difty.scipamato.core.persistence.code;

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
import ch.difty.scipamato.core.entity.Code;

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
        codes.add(new Code("c1", "Code1", null, false, 1, "cc1", "", 1));
        codes.add(new Code("c2", "Code2", null, false, 1, "cc1", "", 2));
        when(repoMock.findCodesOfClass(ccId, languageCode)).thenReturn(codes);

        assertThat(extractProperty(Code.CODE).from(service.findCodesOfClass(ccId, languageCode))).containsOnly("c1",
            "c2");

        verify(repoMock).findCodesOfClass(ccId, languageCode);

        verifyNoMoreInteractions(repoMock);
    }
}
