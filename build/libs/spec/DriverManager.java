package edu.pnu.myjdbc.spec;

import java.util.ServiceLoader;

public class DriverManager {
    private static ServiceLoader<Driver> driverLoader;

    static{
        //initialize the driverLoader to load the Driver services.
        driverLoader = ServiceLoader.load(Driver.class);
    }

    public static Connection getConnection(String url, String user, String password) {

        for (Driver driver : driverLoader) {

            if (driver.acceptsURL(url)) {

                return driver.connect(url, user, password);

            }

        }

        throw new RuntimeException("No suitable driver found for " + url);

    }
}

