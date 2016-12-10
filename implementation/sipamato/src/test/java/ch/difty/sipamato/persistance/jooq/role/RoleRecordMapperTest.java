package ch.difty.sipamato.persistance.jooq.role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.jooq.RecordMapper;

import ch.difty.sipamato.db.tables.records.RoleRecord;
import ch.difty.sipamato.entity.Role;
import ch.difty.sipamato.entity.User;
import ch.difty.sipamato.persistance.jooq.RecordMapperTest;

public class RoleRecordMapperTest extends RecordMapperTest<RoleRecord, Role> {

    public static final int ID = 3;
    public static final String NAME = "name";
    public static final String COMMENT = "comment";

    public final List<User> users = new ArrayList<>();

    public static void entityFixtureWithoutIdFields(Role entityMock) {
        when(entityMock.getName()).thenReturn(NAME);
        when(entityMock.getComment()).thenReturn(COMMENT);
    }

    @Override
    protected RecordMapper<RoleRecord, Role> getMapper() {
        return new RoleRecordMapper();
    }

    @Override
    protected RoleRecord makeRecord() {
        return new RoleRecord(ID, NAME, COMMENT, VERSION, CREATED, CREATED_BY, LAST_MOD, LAST_MOD_BY);
    }

    @Override
    protected void assertEntity(final Role role) {
        assertThat(role.getId()).isEqualTo(ID);
        assertThat(role.getName()).isEqualTo(NAME);
        assertThat(role.getComment()).isEqualTo(COMMENT);

        assertThat(role.getUsers()).isEmpty();
    }

}
