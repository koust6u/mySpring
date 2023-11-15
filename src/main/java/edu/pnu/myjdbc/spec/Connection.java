package edu.pnu.myjdbc.spec;

import edu.pnu.myjdbc.spec.Statement;

public interface Connection {
    Statement createStatement();
}
