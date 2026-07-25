package com.passwordcracker.strategies.dictionary;

import com.passwordcracker.core.HashCracker;
import com.passwordcracker.core.Md5Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class DictionaryHashCracker implements HashCracker {

    private static final String DEFAULT_DICTIONARY_RESOURCE = "/dictionary.txt";

    private final Path dictionaryPath;

    public DictionaryHashCracker() {
        this.dictionaryPath = null;
    }

    public DictionaryHashCracker(Path dictionaryPath) {
        this.dictionaryPath = dictionaryPath;
    }

    @Override
    public String crack(String hash) {
        if (hash == null || hash.isBlank()) {
            return null;
        }

        String targetHash = hash.toLowerCase(Locale.ROOT).trim();

        try (BufferedReader reader = openDictionary()) {
            if (reader == null) {
                return null;
            }

            String word;
            while ((word = reader.readLine()) != null) {
                word = word.trim();
                if (word.isEmpty()) {
                    continue;
                }
                String candidateHash = Md5Util.hash(word);
                if (candidateHash.equals(targetHash)) {
                    return word;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to read dictionary file", e);
        }

        return null;
    }

    private BufferedReader openDictionary() throws IOException {
        if (dictionaryPath != null) {
            return Files.newBufferedReader(dictionaryPath, StandardCharsets.UTF_8);
        }

        InputStream resourceStream = DictionaryHashCracker.class.getResourceAsStream(DEFAULT_DICTIONARY_RESOURCE);
        if (resourceStream == null) {
            throw new IOException("Default dictionary resource not found: " + DEFAULT_DICTIONARY_RESOURCE);
        }
        return new BufferedReader(new InputStreamReader(resourceStream, StandardCharsets.UTF_8));
    }
}
