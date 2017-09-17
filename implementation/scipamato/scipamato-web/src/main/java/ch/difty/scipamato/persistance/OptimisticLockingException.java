package ch.difty.scipamato.persistance;

public class OptimisticLockingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String tableName;
    private final String record;

    public OptimisticLockingException(final String tableName, Type type) {
        this(tableName, null, type);
    }

    public OptimisticLockingException(final String tableName, final String record, Type type) {
        super(makeMessage(tableName, record, type));
        this.tableName = tableName;
        this.record = record;
    }

    private static String makeMessage(final String tableName, final String record, Type type) {
        StringBuilder sb = new StringBuilder();
        sb.append("Record in table '").append(tableName).append("' has been modified prior to the ").append(type.descr).append(" attempt. Aborting....");
        if (record != null)
            sb.append(" [").append(record).append("]");
        return sb.toString();
    }

    public String getTableName() {
        return tableName;
    }

    public String getRecord() {
        return record;
    }

    public enum Type {
        UPDATE("update"),
        DELETE("delete");

        private final String descr;

        Type(String descr) {
            this.descr = descr;
        }
    }
}
