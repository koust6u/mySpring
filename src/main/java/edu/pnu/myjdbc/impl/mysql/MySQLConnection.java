package edu.pnu.myjdbc.impl.mysql;

import edu.pnu.myjdbc.spec.Connection;
import edu.pnu.myjdbc.spec.Statement;

public class MySQLConnection implements Connection {


    private final String username;
    private final String password;

    private final String databaseUrl;

    public MySQLConnection(String username, String password, String databaseUrl) {
        this.username = username;
        this.password = password;
        this.databaseUrl = databaseUrl;
    }
    @Override
    public Statement createStatement() {
        return new MySQLStatement();
    }
}
