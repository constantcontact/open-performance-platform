package com.opp.dao.util;

import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by ctobe on 7/13/16.
 */
public class UpdateBuilder {

    protected final String table;
    protected final Map<String,Object> columns = new LinkedHashMap<>();
    protected final Map<String,Object> whereClause = new LinkedHashMap<>();

    // Whether or not to insert values if they are null
    private boolean excludeNullValues = true;

    public static UpdateBuilder updateInto(String table) {
        return new UpdateBuilder(table);
    }

    protected UpdateBuilder(String table) {
        this.table = table;
    }

    public UpdateBuilder whereColumnEqualsValue(String column, Object value) {
        checkArgument(StringUtils.isNotBlank(column));
        checkNotNull(value);
        whereClause.put(column, value);
        return this;
    }

    /**
     * Insert the provided value into the provided column.
     * @param column
     * @param value
     * @return
     */
    public UpdateBuilder value(String column, Object value) {
        if (shouldInsertValue(value, excludeNullValues)) {
            columns.put(column, value);
        }
        return this;
    }

    /**
     * Insert the provided value into the provided column. Value will not be included in the insert if
     * <code>excludeIfNull</code> is set to <code>true</code> and <code>value</code> is <code>null</code>.
     * @param column
     * @param value
     * @param excludeIfNull
     * @return
     */
    public UpdateBuilder value(String column, Object value, boolean excludeIfNull) {
        if (shouldInsertValue(value, excludeIfNull)) {
            columns.put(column, value);
        }
        return this;
    }

    /**
     * Set whether or not to insert null values by default. The default value for this is <code>true</code>.
     * @param excludeNullValues true to exclude inserting null values by default, false otherwise
     * @returng
     */
    public UpdateBuilder excludeNullValues(boolean excludeNullValues) {
        this.excludeNullValues = excludeNullValues;
        return this;
    }

    public PreparedStatement build(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(generateSQL());
        int index = 1;
        for (String column : columns.keySet()) {
            ps.setObject(index++, columns.get(column));
        }
        for (Object whereClauseValue : whereClause.values()) {
            ps.setObject(index++, whereClauseValue);
        }
        return ps;
    }

    private boolean shouldInsertValue(Object value, boolean shouldExcludeNullValues) {
        if (value != null) { // always insert non-null values
            return true;
        } else { // only insert the null value if we are not excluding null values
            return !shouldExcludeNullValues;
        }
    }

    protected String generateSQL() {
        checkArgument(!whereClause.isEmpty(), "must specify a where clause");
        StringBuilder sb = new StringBuilder("UPDATE " + table + " SET ");
        sb.append(columns.keySet().stream()
                .map(column -> column + " = ?")
                .collect(Collectors.joining(", ")));
        sb.append(" WHERE ");
        sb.append(whereClause.keySet().stream()
                .map(column -> column + " = ?")
                .collect(Collectors.joining(" and ")));
        return sb.toString();
    }
}
