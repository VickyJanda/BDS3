package org.but.feec.javafx;

import org.but.feec.javafx.config.DataSourceConfig;

public class Main {
    public static void main(String[] args) {

        DataSourceConfig.initializeDataSource(args);


        try {
            PasswordUpdater.updatePasswords();
        } catch (Exception e) {
            e.printStackTrace();

        }


        App.main(args);
    }
}
