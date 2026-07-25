package com.passwordcracker.strategies.bruteforce;

import com.passwordcracker.core.HashCracker;
import com.passwordcracker.core.Md5Util;

import java.util.Locale;

public class BruteForceHashCracker implements HashCracker {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final int MAX_LENGTH = 4;

    @Override
    public String crack(String hash) {
        if (hash == null || hash.isBlank()) {
            return null;
        }

        String targetHash = hash.toLowerCase(Locale.ROOT).trim();

        for (int length = 1; length <= MAX_LENGTH; length++) {
            String found = search(new char[length], 0, length, targetHash);
            if (found != null) {
                return found;
            }
        }

        return null;
    }

    private String search(char[] buffer, int position, int length, String targetHash) {
        if (position == length) {
            String candidate = new String(buffer);
            return Md5Util.hash(candidate).equals(targetHash) ? candidate : null;
        }

        for (int i = 0; i < ALPHABET.length(); i++) {
            buffer[position] = ALPHABET.charAt(i);
            String found = search(buffer, position + 1, length, targetHash);
            if (found != null) {
                return found;
            }
        }

        return null;
    }
}
