package com.passwordcracker.cli;

import com.passwordcracker.core.HashCracker;
import com.passwordcracker.factory.HashCrackerFactory;

import java.time.Duration;
import java.time.Instant;

public class Main {

    public static void main(String[] args) {
        if (args == null || args.length < 4) {
            printUsage();
            return;
        }

        String method = null;
        String hash = null;
        String dictionaryPath = null;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("-m".equalsIgnoreCase(arg) && i + 1 < args.length) {
                method = args[++i];
            } else if ("-h".equalsIgnoreCase(arg) && i + 1 < args.length) {
                hash = args[++i];
            } else if ("-d".equalsIgnoreCase(arg) && i + 1 < args.length) {
                dictionaryPath = args[++i];
            } else {
                System.out.printf("Unknown option: %s%n", arg);
                printUsage();
                return;
            }
        }

        if (method == null || method.isBlank() || hash == null || hash.isBlank()) {
            printUsage();
            return;
        }

        try {
            HashCracker cracker = HashCrackerFactory.create(method, dictionaryPath);
            Instant start = Instant.now();
            String password = cracker.crack(hash);
            Instant end = Instant.now();
            Duration elapsed = Duration.between(start, end);

            if (password != null) {
                System.out.println("Password found: " + password);
            } else {
                System.out.println("Password not found");
            }
            System.out.printf("Elapsed time: %.3f seconds%n", elapsed.toMillis() / 1000.0);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            printUsage();
        }
    }

    private static void printUsage() {
        System.out.println("PasswordCracker v1 — Simple Factory Pattern");
        System.out.println("Usage:");
        System.out.println("  java -jar passwordcracker.jar -m <DICO|BRUTE> -h <md5hash> [-d <dictionaryFile>]");
        System.out.println("Examples:");
        System.out.println("  java -jar passwordcracker.jar -m DICO -h e7247759c1633c0f9f1485f3690294a9");
        System.out.println("  java -jar passwordcracker.jar -m BRUTE -h 098f6bcd4621d373cade4e832627b4f6");
    }
}
