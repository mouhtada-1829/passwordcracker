package com.passwordcracker.tests;

import com.passwordcracker.strategies.dictionary.DictionaryHashCracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

public class DictionaryHashCrackerTest {

    private final DictionaryHashCracker cracker = new DictionaryHashCracker();

    @Test
    public void testTrouveMotConnu() {
        String hash = "098f6bcd4621d373cade4e832627b4f6";
        assertEquals("test", cracker.crack(hash));
    }

    @Test
    public void testMotConnuEnMajuscule() {
        String hash = "098f6bcd4621d373cade4e832627b4f6";
        assertEquals("test", cracker.crack(hash.toUpperCase()));
    }

    @Test
    public void testHashInconnuRetourneNull() {
        String hash = "00000000000000000000000000000000";
        assertNull(cracker.crack(hash));
    }

    @Test
    public void testHashNullRetourneNull() {
        assertNull(cracker.crack(null));
    }

    @Test
    public void testHashVideRetourneNull() {
        assertNull(cracker.crack(""));
    }
}
