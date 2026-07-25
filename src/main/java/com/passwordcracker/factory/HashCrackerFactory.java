package com.passwordcracker.factory;

import com.passwordcracker.core.HashCracker;
import com.passwordcracker.strategies.bruteforce.BruteForceHashCracker;
import com.passwordcracker.strategies.dictionary.DictionaryHashCracker;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class HashCrackerFactory {

    public static HashCracker create(String method) {
        return create(method, null);
    }

    public static HashCracker create(String method, String dictionaryPath) {
        if (method == null || method.isBlank()) {
            throw new IllegalArgumentException("Method must be provided");
        }

        String normalized = method.trim().toUpperCase(Locale.ROOT);
        if ("DICO".equals(normalized) || "DICTIONARY".equals(normalized)) {
            if (dictionaryPath != null && !dictionaryPath.isBlank()) {
                Path path = Paths.get(dictionaryPath.trim());
                if (!Files.exists(path) || !Files.isReadable(path)) {
                    throw new IllegalArgumentException("Dictionary file not found or not readable: " + dictionaryPath);
                }
                return new DictionaryHashCracker(path);
            }
            return new DictionaryHashCracker();
        }

        if ("BRUTE".equals(normalized) || "BRUTEFORCE".equals(normalized)) {
            return new BruteForceHashCracker();
        }

        throw new IllegalArgumentException("Unknown cracking method: " + method + ". Use DICO or BRUTE.");
    }
}
