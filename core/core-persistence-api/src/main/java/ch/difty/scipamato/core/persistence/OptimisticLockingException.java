package ch.difty.scipamato.core.persistence;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OptimisticLockingException extends RuntimeException {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    private final String tableName;
    private final String rcd;

    public OptimisticLockingException(@NotNull final String tableName, @NotNull final Type type) {
        this(tableName, null, type);
    }

    public OptimisticLockingException(@NotNull final String tableName, @Nullable final String rcd, @NotNull final Type type) {
        super(makeMessage(tableName, rcd, type));
        this.tableName = tableName;
        this.rcd = rcd;
    }

    private static String makeMessage(@NotNull final String tableName, @Nullable final String rcd, @NotNull final Type type) {
        final StringBuilder sb = new StringBuilder();
        sb
            .append("Record in table '")
            .append(tableName)
            .append("' has been modified prior to the ")
            .append(type.description)
            .append(" attempt. Aborting....");
        if (rcd != null)
            sb
                .append(" [")
                .append(rcd)
                .append("]");
        return sb.toString();
    }

    @NotNull
    public String getTableName() {
        return tableName;
    }

    @Nullable
    public String getRcd() {
        return rcd;
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
