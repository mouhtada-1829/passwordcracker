package com.passwordcracker.tests;

import com.passwordcracker.core.Md5Util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

public class Md5UtilTest {

    @Test
    public void testHashConnu() {
        assertEquals("098f6bcd4621d373cade4e832627b4f6", Md5Util.hash("test"));
    }

    @Test
    public void testHashChaineVide() {
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", Md5Util.hash(""));
    }

    @Test
    public void testHashProduitToujours32Caracteres() {
        String hash = Md5Util.hash("nimportequoi");
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    @Test
    public void testHashMinuscules() {
        String hash = Md5Util.hash("HELLO");
        assertEquals(hash.toLowerCase(), hash);
    }
}
