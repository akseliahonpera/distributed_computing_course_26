package com.group_13;

import com.password4j.Hash;
import com.password4j.Password;

public class PasswordUtil {
    private static final String PEPPER = "asd123"; //Should be secure random generated and stored somewhere else (enviroment variables)

    public static String hashPassword(String plainPassword) {

        if (PEPPER == null || PEPPER.isEmpty()) {
            throw new IllegalStateException("Pepper not configured!");
        }

        String passwordWithPepper = plainPassword + PEPPER;

        Hash hash = Password.hash(passwordWithPepper)
                            .addRandomSalt(16)
                            .withArgon2();

        return hash.getResult();
    }

    public static boolean verifyPassword(String plainPassword, String storedHash) {

        String passwordWithPepper = plainPassword + PEPPER;

        return Password.check(passwordWithPepper, storedHash)
                       .withArgon2();
    }
}