package com.passwordcracker.strategies.bruteforce;

import com.passwordcracker.core.HashCracker;
import com.passwordcracker.core.Md5Util;

public class BruteForceHashCracker implements HashCracker {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final int MAX_LENGTH = 4;

    private volatile long attempts;
    private final long totalCombinations;

    public BruteForceHashCracker() {
        this.totalCombinations = computeTotalCombinations();
    }

    @Override
    public String crack(String hash) {
        if (hash == null || hash.isBlank()) {
            return null;
        }

        String target = hash.trim().toLowerCase();
        attempts = 0;
        char[] alphabet = ALPHABET.toCharArray();

        for (int len = 1; len <= MAX_LENGTH; len++) {
            int[] indices = new int[len];

            while (true) {
                StringBuilder candidate = new StringBuilder(len);
                for (int i = 0; i < len; i++) {
                    candidate.append(alphabet[indices[i]]);
                }

                attempts++;
                if (Md5Util.hash(candidate.toString()).equals(target)) {
                    return candidate.toString();
                }

                int pos = len - 1;
                while (pos >= 0 && indices[pos] == alphabet.length - 1) {
                    indices[pos] = 0;
                    pos--;
                }
                if (pos < 0) {
                    break;
                }
                indices[pos]++;
            }
        }

        return null;
    }

    public long getAttempts() {
        return attempts;
    }

    public long getTotalCombinations() {
        return totalCombinations;
    }

    private long computeTotalCombinations() {
        long total = 0;
        long power = 1;
        for (int len = 1; len <= MAX_LENGTH; len++) {
            power *= ALPHABET.length();
            total += power;
        }
        return total;
    }
}
