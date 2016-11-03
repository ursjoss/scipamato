package ch.difty.sipamato.persistance.jooq.code;

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

import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.CodeClassId;

@RunWith(MockitoJUnitRunner.class)
public class JooqCodeServiceTest {

    private final JooqCodeService service = new JooqCodeService();

    @Mock
    private CodeRepository repoMock;

    @Test
    public void findingCodes_delegatesToRepo() {
        service.setRepository(repoMock);

        CodeClassId ccId = CodeClassId.CC1;
        String languageCode = "de";

        List<Code> codes = new ArrayList<>();
        codes.add(new Code("c1", "Code1", null, 1, "cc1", "", 1));
        codes.add(new Code("c2", "Code2", null, 1, "cc1", "", 2));
        when(repoMock.findCodesOfClass(ccId, languageCode)).thenReturn(codes);

        assertThat(extractProperty(Code.CODE).from(service.findCodesOfClass(ccId, languageCode))).containsOnly("c1", "c2");

        verify(repoMock).findCodesOfClass(ccId, languageCode);

        verifyNoMoreInteractions(repoMock);
    }
}
