package ch.difty.scipamato.core.persistence;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OptimisticLockingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String tableName;
    private final String record;

    public OptimisticLockingException(@NotNull final String tableName, @NotNull final Type type) {
        this(tableName, null, type);
    }

    public OptimisticLockingException(@NotNull final String tableName, @Nullable final String record,
        @NotNull final Type type) {
        super(makeMessage(tableName, record, type));
        this.tableName = tableName;
        this.record = record;
    }

    private static String makeMessage(final String tableName, final String record, final Type type) {
        final StringBuilder sb = new StringBuilder();
        sb
            .append("Record in table '")
            .append(tableName)
            .append("' has been modified prior to the ")
            .append(type.description)
            .append(" attempt. Aborting....");
        if (record != null)
            sb
                .append(" [")
                .append(record)
                .append("]");
        return sb.toString();
    }

    @NotNull
    public String getTableName() {
        return tableName;
    }

    @Nullable
    public String getRecord() {
        return record;
    }

    public enum Type {
        UPDATE("update"),
        DELETE("delete");

        private final String description;

        Type(@NotNull final String description) {
            this.description = description;
        }
    }
}
