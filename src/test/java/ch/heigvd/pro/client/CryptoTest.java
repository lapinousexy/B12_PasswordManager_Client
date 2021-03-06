package ch.heigvd.pro.client;

import ch.heigvd.pro.client.crypto.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CryptoTest {
    @Test
    public void kAnonimityTest() {
        // SHA1 hash of the string "Test"
        String hashTest = "640ab2bae07bedc4c163f679a746f7ab7fb5d1fa".toUpperCase();
        String hashCompared = Crypto.fiveAnonimitySHA1("Test".toCharArray());

        assertEquals(hashTest.substring(0, Crypto.K_ANONIMITY_CONSTANT), hashCompared.substring(0, Crypto.K_ANONIMITY_CONSTANT));
    }

}