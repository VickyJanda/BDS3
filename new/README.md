# BDS - JavaFX Demo Template
This project is a template project to train basics of JavaFX (Java Desktop GUI).

## To build&run the project
Enter the following command in the project root directory to build the project.
```shell
$ mvn clean install
```

Run the project:
```shell
$ java -jar target/bds-javafx-training-1.0.0.jar
```

Run the project with externalized `application.properties`:
```shell
$ java -jar target/bds-javafx-training-1.0.0.jar "./etc/application.properties"
```

The `application.properties` file can be located in any place on the system that the application can access.

Sign-in with the following credentials:
- Username: `radek.kruta@seznam.cz`
- Password: `batman`

## To generate the project and external libraries licenses
Enter the following command in the project root directory
```shell
$ mvn project-info-reports:dependencies
```

The licenses info and documentation will be located in the `target/site` folder.
