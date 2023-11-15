package edu.pnu.myjdbc.impl.mysql;

import static javax.swing.UIManager.put;

import edu.pnu.myjdbc.spec.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class MySQLResultSet implements ResultSet {

    private Integer index = 0;
    private final Map<String, Object>[] data = new Map[] {

            new HashMap<String, Object>() {{

                put("id", 1);

                put("age", 25);

                put("first", "Edward");

                put("last", "Kim");

            }},

    };

    @Override
    public boolean next() {
        return ++index< data.length;
    }

    @Override
    public int getInt(String columLabel) {
        return (Integer)data[index].get(columLabel);
    }

    @Override
    public String getString(String columnLabel) {
        return (String) data[index].get(columnLabel);
    }
}
