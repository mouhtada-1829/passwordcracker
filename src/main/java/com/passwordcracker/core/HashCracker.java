package com.passwordcracker.core;

public interface HashCracker {
    String crack(String hash);

    default long getAttempts() {
        return 0;
    }

    default long getTotalCombinations() {
        return 0;
    }
}
