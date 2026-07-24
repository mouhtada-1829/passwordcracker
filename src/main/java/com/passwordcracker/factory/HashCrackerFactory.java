package com.passwordcracker.factory;

import com.passwordcracker.core.HashCracker;

public class HashCrackerFactory {

    public static HashCracker create(String method) {
        throw new UnsupportedOperationException(
            "Factory not yet implemented. Merge dictionary and bruteforce strategies first."
        );
    }
}
