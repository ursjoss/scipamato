package ch.difty.scipamato.core.sync.jobs

import java.sql.Timestamp
import java.time.LocalDateTime

val CREATED: Timestamp = Timestamp.valueOf(LocalDateTime.now().minusDays(2))
val MODIFIED: Timestamp = Timestamp.valueOf(LocalDateTime.now().minusDays(1))
val SYNCHED: Timestamp = Timestamp.valueOf(LocalDateTime.now())
