package com.passwordcracker.strategies.bruteforce;

import com.passwordcracker.core.HashCracker;
import com.passwordcracker.core.Md5Util;

/**
 * Stratégie de cassage de mot de passe par force brute.
 * Génère toutes les combinaisons possibles sur l'alphabet a-z,
 * de 1 à 4 caractères, et compare leur hash MD5 au hash recherché.
 */
public class BruteForceHashCracker implements HashCracker {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final int MAX_LENGTH = 4;

    /**
     * Tente de retrouver le mot de passe correspondant au hash MD5 donné
     * en générant toutes les combinaisons possibles sur l'alphabet a-z
     * jusqu'à MAX_LENGTH caractères.
     *
     * @param hash le hash MD5 recherché
     * @return le mot de passe trouvé, ou null si aucune correspondance
     */
    @Override
    public String crack(String hash) {
        for (int length = 1; length <= MAX_LENGTH; length++) {
            String result = tryLength(hash, length);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * Essaie toutes les combinaisons d'une longueur donnée.
     *
     * @param hash   le hash MD5 recherché
     * @param length la longueur des combinaisons à tester
     * @return le mot trouvé, ou null si aucune correspondance
     */
    private String tryLength(String hash, int length) {
        char[] combo = new char[length];
        return generate(hash, combo, 0, length);
    }

    /**
     * Génère récursivement toutes les combinaisons possibles et les compare au hash.
     *
     * @param hash   le hash MD5 recherché
     * @param combo  le tableau de caractères en cours de construction
     * @param index  la position courante dans le tableau
     * @param length la longueur totale de la combinaison
     * @return le mot trouvé, ou null si aucune correspondance
     */
    private String generate(String hash, char[] combo, int index, int length) {
        if (index == length) {
            String candidate = new String(combo);
            if (Md5Util.hash(candidate).equals(hash)) {
                return candidate;
            }
            return null;
        }
        for (char c : ALPHABET.toCharArray()) {
            combo[index] = c;
            String result = generate(hash, combo, index + 1, length);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
