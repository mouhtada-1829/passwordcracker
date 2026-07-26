package com.passwordcracker.factory;

import com.passwordcracker.core.HashCracker;
import com.passwordcracker.strategies.bruteforce.BruteForceHashCracker;
import com.passwordcracker.strategies.dictionary.DictionaryHashCracker;

public class HashCrackerFactory {

    public static final String DICO = "DICO";
    public static final String BRUTE = "BRUTE";

    private HashCrackerFactory() {
    }

    public static HashCracker create(String method) {
        if (method == null) {
            throw new IllegalArgumentException("Method must not be null. Use -m DICO or -m BRUTE.");
        }
        return switch (method.trim().toUpperCase()) {
            case DICO -> new DictionaryHashCracker();
            case BRUTE -> new BruteForceHashCracker();
            default -> throw new IllegalArgumentException(
                    "Unknown method: '" + method + "'. Expected DICO or BRUTE.");
        };
    }
}
