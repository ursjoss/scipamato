package ch.difty.scipamato.core.sync.jobs;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public abstract class PublicEntityTest {

    protected static final Timestamp CREATED = Timestamp.valueOf(LocalDateTime.now().minusDays(2));
    protected static final Timestamp MODIFIED = Timestamp.valueOf(LocalDateTime.now().minusDays(1));
    protected static final Timestamp SYNCHED = Timestamp.valueOf(LocalDateTime.now());

}
