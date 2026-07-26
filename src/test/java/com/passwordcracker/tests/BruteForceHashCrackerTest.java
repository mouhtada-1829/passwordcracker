package com.passwordcracker.tests;

import com.passwordcracker.strategies.bruteforce.BruteForceHashCracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class BruteForceHashCrackerTest {

    private final BruteForceHashCracker cracker = new BruteForceHashCracker();

    @Test
    public void testTrouveMotUneLettre() {
        String hash = "0cc175b9c0f1b6a831c399e269772661";
        assertEquals("a", cracker.crack(hash));
    }

    @Test
    public void testTrouveMotDeuxLettres() {
        String hash = "4124bc0a9335c27f086f24ba207a4912";
        assertEquals("aa", cracker.crack(hash));
    }

    @Test
    public void testTrouveMotQuatreLettres() {
        String hash = "098f6bcd4621d373cade4e832627b4f6";
        assertEquals("test", cracker.crack(hash));
    }

    @Test
    public void testHashInconnuRetourneNull() {
        String hash = "ffffffffffffffffffffffffffffffff";
        assertNull(cracker.crack(hash));
    }

    @Test
    public void testTotalCombinaisonsCorrect() {
        long attendu = 26 + 26*26 + 26*26*26 + 26*26*26*26;
        assertEquals(attendu, cracker.getTotalCombinations());
    }

    @Test
    public void testCompteurTentativesIncremente() {
        cracker.crack("ffffffffffffffffffffffffffffffff");
        assertTrue(cracker.getAttempts() > 0);
    }
}
