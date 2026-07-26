package com.passwordcracker.cli;

import com.passwordcracker.core.HashCracker;
import com.passwordcracker.factory.HashCrackerFactory;

public class Main {

    private static final String HASH_REGEX = "[0-9a-fA-F]{32}";

    public static void main(String[] args) {
        String method = null;
        String hash = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-m" -> {
                    if (i + 1 < args.length) {
                        method = args[++i];
                    }
                }
                case "-h" -> {
                    if (i + 1 < args.length) {
                        hash = args[++i];
                    }
                }
            }
        }

        if (method == null || hash == null) {
            afficherUsage();
            return;
        }

        if (!hash.matches(HASH_REGEX)) {
            System.out.println("Erreur : le hash doit etre un MD5 valide (32 caracteres hexadecimaux).");
            afficherUsage();
            return;
        }

        HashCracker cracker;
        try {
            cracker = HashCrackerFactory.create(method);
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur : " + e.getMessage());
            afficherUsage();
            return;
        }

        Thread progres = demarrerProgres(cracker);

        long debut = System.nanoTime();
        String motDePasse = cracker.crack(hash);
        long tempsMs = (System.nanoTime() - debut) / 1_000_000;

        arreterProgres(progres);

        long tentatives = cracker.getAttempts();
        double vitesse = tentatives / (tempsMs / 1000.0);

        if (motDePasse != null) {
            System.out.println("Mot de passe trouve : " + motDePasse);
        } else {
            System.out.println("Mot de passe non trouve");
        }

        System.out.println("Tentatives : " + tentatives);
        System.out.printf("Vitesse : %.1f M hachages/sec%n", vitesse / 1_000_000);
        System.out.println("Temps : " + tempsMs + " ms");
    }

    private static Thread demarrerProgres(HashCracker cracker) {
        long total = cracker.getTotalCombinations();
        if (total <= 0) {
            return null;
        }

        Thread t = new Thread(() -> {
            try {
                while (true) {
                    long fait = cracker.getAttempts();
                    int pourcentage = (int) (fait * 100 / total);
                    int barres = pourcentage / 5;
                    System.out.print("\rProgres : [" + "#".repeat(barres) + " ".repeat(20 - barres) + "] " + pourcentage + "%");
                    if (fait >= total) {
                        break;
                    }
                    Thread.sleep(80);
                }
            } catch (InterruptedException ignore) {
            }
        });
        t.setDaemon(true);
        t.start();
        return t;
    }

    private static void arreterProgres(Thread t) {
        if (t == null) {
            return;
        }
        try {
            t.join(200);
        } catch (InterruptedException ignore) {
        }
        System.out.println();
    }

    private static void afficherUsage() {
        System.out.println("Usage : passwordCracker -m <DICO|BRUTE> -h <hashMD5>");
        System.out.println("Exemple : passwordCracker -m DICO -h 098f6bcd4621d373cade4e832627b4f6");
        System.out.println("          passwordCracker -m BRUTE -h 098f6bcd4621d373cade4e832627b4f6");
    }
}
