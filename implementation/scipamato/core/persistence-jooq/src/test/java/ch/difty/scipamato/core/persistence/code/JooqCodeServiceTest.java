package ch.difty.scipamato.core.persistence.code;

import static ch.difty.scipamato.core.entity.Code.CodeFields.CODE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.Code;
import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.code.CodeDefinition;
import ch.difty.scipamato.core.entity.code.CodeFilter;

@ExtendWith(MockitoExtension.class)
class JooqCodeServiceTest {

    private JooqCodeService service;

    @Mock
    private CodeRepository repoMock;

    @Mock
    private CodeFilter filterMock;

    @Mock
    private PaginationContext paginationContextMock;

    @Mock
    private Code codeMock;

    protected CodeRepository getRepo() {
        return repoMock;
    }

    protected Code getEntity() {
        return codeMock;
    }

    private final List<CodeDefinition> codeDefinitions = new ArrayList<>();

    @Mock
    private CodeDefinition codeDefinitionMock, persistedCodeDefinitionMock;

    @BeforeEach
    void setUp() {
        service = new JooqCodeService(repoMock);

        codeDefinitions.add(codeDefinitionMock);
        codeDefinitions.add(codeDefinitionMock);
    }

    @AfterEach
    void specificTearDown() {
        verifyNoMoreInteractions(repoMock, filterMock, paginationContextMock, codeMock, codeDefinitionMock);
    }

    @Test
    void findingCodes_delegatesToRepo() {
        CodeClassId ccId = CodeClassId.CC1;
        String languageCode = "de";

        List<Code> codes = new ArrayList<>();
        codes.add(new Code("c1", "Code1", null, false, 1, "cc1", "", 1));
        codes.add(new Code("c2", "Code2", null, false, 1, "cc1", "", 2));
        when(repoMock.findCodesOfClass(ccId, languageCode)).thenReturn(codes);

        assertThat(extractProperty(CODE.getFieldName()).from(service.findCodesOfClass(ccId, languageCode))).containsOnly(
            "c1", "c2");

        verify(repoMock).findCodesOfClass(ccId, languageCode);

        verifyNoMoreInteractions(repoMock);
    }

    @Test
    void newUnpersistedCodeDefinition_delegatesToRepo() {
        when(repoMock.newUnpersistedCodeDefinition()).thenReturn(codeDefinitionMock);
        assertThat(service.newUnpersistedCodeDefinition()).isEqualTo(codeDefinitionMock);
        verify(repoMock).newUnpersistedCodeDefinition();
    }

    @Test
    void findingPageOfCodeDefinitions_delegatesToRepo() {
        when(repoMock.findPageOfCodeDefinitions(filterMock, paginationContextMock)).thenReturn(codeDefinitions);
        assertThat(service.findPageOfCodeDefinitions(filterMock, paginationContextMock)).isEqualTo(codeDefinitions);
        verify(repoMock).findPageOfCodeDefinitions(filterMock, paginationContextMock);
    }

    @Test
    void gettingPageOfEntityDefinitions_delegatesToRepo() {
        when(repoMock.findPageOfCodeDefinitions(filterMock, paginationContextMock)).thenReturn(codeDefinitions);
        assertThat(service.findPageOfEntityDefinitions(filterMock, paginationContextMock)).hasSameElementsAs(
            codeDefinitions);
        verify(repoMock).findPageOfCodeDefinitions(filterMock, paginationContextMock);
    }

    @Test
    void countingCodes_delegatesToRepo() {
        when(repoMock.countByFilter(filterMock)).thenReturn(3);
        assertThat(service.countByFilter(filterMock)).isEqualTo(3);
        verify(repoMock).countByFilter(filterMock);
    }

    @Test
    void insertingCodeDefinition_delegatesToRepo() {
        when(repoMock.saveOrUpdate(codeDefinitionMock)).thenReturn(persistedCodeDefinitionMock);
        assertThat(service.saveOrUpdate(codeDefinitionMock)).isEqualTo(persistedCodeDefinitionMock);
        verify(repoMock).saveOrUpdate(codeDefinitionMock);
    }

    @Test
    void deletingCodeDefinition_delegatesToRepo() {
        final String code = "1A";
        final int version = 12;
        when(repoMock.delete(code, version)).thenReturn(persistedCodeDefinitionMock);
        assertThat(service.delete(code, version)).isEqualTo(persistedCodeDefinitionMock);
        verify(repoMock).delete(code, version);
    }

    @Test
    void gettingCodeClass1_delegatesToRepo() {
        CodeClass cc1 = new CodeClass(1, "cc1", null);
        when(repoMock.getCodeClass1("en")).thenReturn(cc1);

        assertThat(service.getCodeClass1("en")).isEqualTo(cc1);

        verify(repoMock).getCodeClass1("en");
    }

}
