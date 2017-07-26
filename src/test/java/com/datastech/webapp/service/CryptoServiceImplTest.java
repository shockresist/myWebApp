package com.datastech.webapp.service;

/**
 * Created by Denys B on 14.03.2017.
 */


import com.datastech.webapp.service.impl.CryptoServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.security.GeneralSecurityException;
import java.util.Base64;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class CryptoServiceImplTest {

    private CryptoService cryptoService;

    private final String MESSAGE_TO_ENCODE = "My Secret Data";
    private final String PASSWORD ="88388";

    @Before
    public void setUp(){
        cryptoService = new CryptoServiceImpl();
    }

    @Test
    public void encryptDecryptTest() throws GeneralSecurityException {
        String encryptedValue = cryptoService.encrypt(MESSAGE_TO_ENCODE, PASSWORD);
        assertThat((Base64.getDecoder().decode(encryptedValue.getBytes()).length > (32+32+32) ),is(true));
        String decryptedValue = cryptoService.decrypt(encryptedValue,PASSWORD);
        assertThat(decryptedValue.equals(MESSAGE_TO_ENCODE),is(true));
    }

}
