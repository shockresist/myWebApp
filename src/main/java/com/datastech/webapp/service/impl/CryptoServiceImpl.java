package com.datastech.webapp.service.impl;


import com.datastech.webapp.service.CryptoService;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * Created by Denys B on 14.03.2017.
 */

@Service
public class CryptoServiceImpl implements CryptoService {

    private static final String KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private static final int SALT_SIZE = 32;
    private static final int HASH_ITERATION_COUNT = 100;
    private static final int ENC_KEY_SIZE = 128;
    private static final int HMAC_SIZE = 32;
    private static final int MIN_ECN_DATA_LENGTH = SALT_SIZE + SALT_SIZE + HMAC_SIZE;
    private static final String CIPHER_TRANSFORMATION = "AES/CTR/NoPadding";
    private static final String CRYPTO_ALGORITHM = "AES";

    @Override
    public String encrypt(String valueToEncrypt, String password) throws GeneralSecurityException {
        SecureRandom r = SecureRandom.getInstance("SHA1PRNG");

        // Generate 160 bit Salt for Encryption Key
        byte[] eSalt = new byte[SALT_SIZE];
        r.nextBytes(eSalt);
        // Generate 128 bit Encryption Key
        byte[] encryptionKey = deriveKey(password, eSalt, HASH_ITERATION_COUNT, ENC_KEY_SIZE);

        // Perform Encryption
        SecretKeySpec encryptionKeySpec = new SecretKeySpec(encryptionKey, CRYPTO_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, encryptionKeySpec, new IvParameterSpec(new byte[16]));
        byte[] encryptedVal = cipher.doFinal(valueToEncrypt.getBytes(StandardCharsets.UTF_8));

        // Generate 256 bit Salt for HMAC Key
        byte[] hSalt = new byte[SALT_SIZE];
        r.nextBytes(hSalt);
        // Generate 256 bit HMAC Key
        byte[] hmacKey = deriveKey(password, hSalt, HASH_ITERATION_COUNT, ENC_KEY_SIZE);

        // Perform HMAC using SHA-256
        SecretKeySpec hmacKeySpec = new SecretKeySpec(hmacKey, HMAC_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(hmacKeySpec);
        byte[] hmac = mac.doFinal(encryptedVal);

        // Construct Output as "ESALT + HSALT + CIPHERTEXT + HMAC"
        byte[] encryptedBytes = new byte[SALT_SIZE * 2 + encryptedVal.length + HMAC_SIZE];
        System.arraycopy(eSalt, 0, encryptedBytes, 0, SALT_SIZE);
        System.arraycopy(hSalt, 0, encryptedBytes, SALT_SIZE, SALT_SIZE);
        System.arraycopy(encryptedVal, 0, encryptedBytes, SALT_SIZE * 2, encryptedVal.length);
        System.arraycopy(hmac, 0, encryptedBytes, SALT_SIZE * 2 + encryptedVal.length, HMAC_SIZE);

        // Return a Base64 Encoded String
        return new String(Base64.getEncoder().encode(encryptedBytes), StandardCharsets.UTF_8);
    }

    @Override
    public String decrypt(String encryptedValue, String password) throws GeneralSecurityException {
        // Recover our Byte Array by Base64 Decoding
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedValue.getBytes(StandardCharsets.UTF_8));

        // Check Minimum Length (ESALT  + HSALT  + HMAC )
        if (encryptedBytes.length > (SALT_SIZE * 2 + HMAC_SIZE)) {
            // Recover Elements from String
            byte[] eSalt = Arrays.copyOfRange(encryptedBytes, 0, SALT_SIZE);
            byte[] hSalt = Arrays.copyOfRange(encryptedBytes, SALT_SIZE, SALT_SIZE * 2);
            byte[] encryptedVal = Arrays.copyOfRange(encryptedBytes, SALT_SIZE * 2, encryptedBytes.length - HMAC_SIZE);
            byte[] hmac = Arrays.copyOfRange(encryptedBytes, encryptedBytes.length - HMAC_SIZE, encryptedBytes.length);

            // Regenerate HMAC key using Recovered Salt (hsalt)
            byte[] derivedHmacKey = deriveKey(password, hSalt, HASH_ITERATION_COUNT, ENC_KEY_SIZE);

            // Perform HMAC using SHA-256
            SecretKeySpec hmacKeySpec = new SecretKeySpec(derivedHmacKey, HMAC_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(hmacKeySpec);
            byte[] chmac = mac.doFinal(encryptedVal);

            // Compare Computed HMAC vs Recovered HMAC
            if (MessageDigest.isEqual(hmac, chmac)) {
                // HMAC Verification Passed
                // Regenerate Encryption Key using Recovered Salt (esalt)
                byte[] decryptionKey = deriveKey(password, eSalt, HASH_ITERATION_COUNT, ENC_KEY_SIZE);

                // Perform Decryption
                SecretKeySpec encryptionKeySpec = new SecretKeySpec(decryptionKey, CRYPTO_ALGORITHM);
                Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
                cipher.init(Cipher.DECRYPT_MODE, encryptionKeySpec, new IvParameterSpec(new byte[16]));
                byte[] decryptedBytes = cipher.doFinal(encryptedVal);

                // Return our Decrypted String
                return new String(decryptedBytes, StandardCharsets.UTF_8);
            }
        }
        throw new GeneralSecurityException("Encrypted value too short ( <= " + MIN_ECN_DATA_LENGTH + " bytes)");
    }

    private byte[] deriveKey(String password, byte[] salt, int iterationsCount, int keyLength) throws GeneralSecurityException{
        PBEKeySpec ks = new PBEKeySpec(password.toCharArray(), salt, iterationsCount, keyLength);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        return skf.generateSecret(ks).getEncoded();
    }

}
