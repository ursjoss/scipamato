package ch.difty.sipamato.persistance.jooq.role;

import static ch.difty.sipamato.db.tables.Role.ROLE;
import static ch.difty.sipamato.persistance.jooq.role.RoleRecordMapperTest.COMMENT;
import static ch.difty.sipamato.persistance.jooq.role.RoleRecordMapperTest.ID;
import static ch.difty.sipamato.persistance.jooq.role.RoleRecordMapperTest.NAME;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mock;

import ch.difty.sipamato.db.tables.records.RoleRecord;
import ch.difty.sipamato.entity.Role;
import ch.difty.sipamato.persistance.jooq.InsertSetStepSetter;
import ch.difty.sipamato.persistance.jooq.InsertSetStepSetterTest;

public class RoleInsertSetStepSetterTest extends InsertSetStepSetterTest<RoleRecord, Role> {

    private final InsertSetStepSetter<RoleRecord, Role> setter = new RoleInsertSetStepSetter();

    @Mock
    private Role entityMock;

    @Override
    protected InsertSetStepSetter<RoleRecord, Role> getSetter() {
        return setter;
    }

    @Override
    protected Role getEntity() {
        return entityMock;
    }

    @Override
    protected void specificTearDown() {
        verifyNoMoreInteractions(entityMock);
    }

    @Override
    protected void entityFixture() {
        RoleRecordMapperTest.entityFixtureWithoutIdFields(entityMock);
    }

    @Override
    protected void stepSetFixture() {
        when(getStep().set(ROLE.NAME, NAME)).thenReturn(getMoreStep());
        when(getMoreStep().set(ROLE.COMMENT, COMMENT)).thenReturn(getMoreStep());
    }

    @Override
    protected void verifyCallToAllNonKeyFields() {
        verify(entityMock).getName();
        verify(entityMock).getComment();
    }

    @Override
    protected void verifySetting() {
        verify(getStep()).set(ROLE.NAME, NAME);
        verify(getMoreStep()).set(ROLE.COMMENT, COMMENT);
    }

    @Test
    public void consideringSettingKeyOf_withNullId_doesNotSetId() {
        when(getEntity().getId()).thenReturn(null);
        getSetter().considerSettingKeyOf(getMoreStep(), getEntity());
        verify(getEntity()).getId();
    }

    @Test
    public void consideringSettingKeyOf_withNonNullId_doesSetId() {
        when(getEntity().getId()).thenReturn(ID);

        getSetter().considerSettingKeyOf(getMoreStep(), getEntity());

        verify(getEntity()).getId();
        verify(getMoreStep()).set(ROLE.ID, ID);
    }

}
