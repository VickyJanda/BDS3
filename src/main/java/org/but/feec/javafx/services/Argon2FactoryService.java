package org.but.feec.javafx.services;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class Argon2FactoryService {

    public static final Argon2 ARGON2 = Argon2Factory.create(
            Argon2Factory.Argon2Types.ARGON2id,
            16,
            32
    );
}
