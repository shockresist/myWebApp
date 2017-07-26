package com.datastech.webapp.service;

import java.security.GeneralSecurityException;

/**
 * Created by Denys B on 14.03.2017.
 */
public interface CryptoService {
    String encrypt(String valueToEncrypt, String password) throws GeneralSecurityException;
    String decrypt(String encryptedValue, String password) throws GeneralSecurityException;
}
