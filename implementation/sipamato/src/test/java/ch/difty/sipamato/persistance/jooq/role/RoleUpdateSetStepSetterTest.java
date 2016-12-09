package ch.difty.sipamato.persistance.jooq.role;

import static ch.difty.sipamato.db.tables.Role.ROLE;
import static ch.difty.sipamato.persistance.jooq.role.RoleRecordMapperTest.COMMENT;
import static ch.difty.sipamato.persistance.jooq.role.RoleRecordMapperTest.ID;
import static ch.difty.sipamato.persistance.jooq.role.RoleRecordMapperTest.NAME;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.mockito.Mock;

import ch.difty.sipamato.db.tables.records.RoleRecord;
import ch.difty.sipamato.entity.Role;
import ch.difty.sipamato.persistance.jooq.UpdateSetStepSetter;
import ch.difty.sipamato.persistance.jooq.UpdateSetStepSetterTest;

public class RoleUpdateSetStepSetterTest extends UpdateSetStepSetterTest<RoleRecord, Role> {

    private final UpdateSetStepSetter<RoleRecord, Role> setter = new RoleUpdateSetStepSetter();

    @Mock
    private Role entityMock;

    @Override
    protected UpdateSetStepSetter<RoleRecord, Role> getSetter() {
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
        when(entityMock.getId()).thenReturn(ID);
        RoleRecordMapperTest.entityFixtureWithoutIdFields(entityMock);
    }

    @Override
    protected void stepSetFixture() {
        when(getStep().set(ROLE.NAME, NAME)).thenReturn(getMoreStep());
        when(getMoreStep().set(ROLE.COMMENT, COMMENT)).thenReturn(getMoreStep());
    }

    @Override
    protected void verifyCallToAllFields() {
        verify(entityMock).getName();
        verify(entityMock).getComment();
    }

    @Override
    protected void verifySetting() {
        verify(getStep()).set(ROLE.NAME, NAME);
        verify(getMoreStep()).set(ROLE.COMMENT, COMMENT);
    }

}
