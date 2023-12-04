package edu.pnu.myjdbc.spec;

public interface ResultSet {
    boolean next();
    int getInt(String columLabel);
    String getString(String columnLabel);
}
