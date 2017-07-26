package com.datastech.webapp;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.junit.Test;
import sun.security.jca.Providers;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Base64;

/**
 * Created by Denys B on 19.01.2017.
 */

public class SslConnectionTest {

    public void testTwoWaySll() throws Exception {
        URL trustKeystoreUrl = this.getClass().getResource("clientTrustedKeystore.jks");
        File trustKeystoreFile = new File(trustKeystoreUrl.getPath());
        URL certUrl = this.getClass().getResource("datas.client.p12");
        File certFile = new File(certUrl.getPath());
        FileInputStream is;
        KeyStore clientKeyStore = null;
        try {
            is = new FileInputStream(certFile);
            clientKeyStore = KeyStore.getInstance("jks");
            clientKeyStore.load(is,"qw123456".toCharArray());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpGet httpGet = new HttpGet("https://dbabich:7002/webApp/");
        SSLContext sslContext = SSLContexts.custom()
                //.useProtocol("TLSv1.2")
                .useProtocol("SSLv3")
                .loadKeyMaterial(clientKeyStore,"qw123456".toCharArray())
                .loadTrustMaterial(trustKeystoreFile,"qw123456".toCharArray())
                .build();


        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext))
                .build();
        try (CloseableHttpResponse resp = httpClient.execute(httpGet)) {
            if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new Exception(resp.getStatusLine().toString());
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent(), "UTF-8"));
            StringBuffer responseStr = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                responseStr.append(line);
            }
            System.out.println(responseStr);
            //return parseAndLogSOAPString(responseStr.toString(), responseMsgId);
        } finally {
            httpGet.releaseConnection();
        }
    }

    @Test
    public void testSslConnection(){
        try {
            URL myKeystoreUrl = this.getClass().getResource("clientTrustedKeystore.jks");
            File myKeystoreFile = new File(myKeystoreUrl.getPath());

            HttpGet httpGet = new HttpGet("https://dbabich:7002/webApp/");

            SSLContext sslContext = SSLContexts.custom()
                    .useProtocol("SSLv3")
                    .loadTrustMaterial(myKeystoreFile,"qw123456".toCharArray())
                    .build();

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext))
                    .build();

            try (CloseableHttpResponse resp = httpClient.execute(httpGet)) {
                if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    throw new Exception(resp.getStatusLine().toString());
                }
                BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent(), "UTF-8"));
                StringBuffer responseStr = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    responseStr.append(line);
                }
                System.out.println(responseStr);
            } finally {
                httpGet.releaseConnection();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public byte[] deriveKey(String password, byte[] salt, int iterationsCount, int keyLength) throws Exception {
        PBEKeySpec ks = new PBEKeySpec(password.toCharArray(), salt, iterationsCount, keyLength);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return skf.generateSecret(ks).getEncoded();
    }

    public String encrypt(String s, String p) throws Exception {
        SecureRandom r = SecureRandom.getInstance("SHA1PRNG");

        // Generate 160 bit Salt for Encryption Key
        byte[] esalt = new byte[32];
        r.nextBytes(esalt);
        // Generate 128 bit Encryption Key
        byte[] dek = deriveKey(p, esalt, 100, 128);

        // Perform Encryption
        SecretKeySpec eks = new SecretKeySpec(dek, "AES");
        Cipher c = Cipher.getInstance("AES/CTR/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, eks, new IvParameterSpec(new byte[16]));
        byte[] es = c.doFinal(s.getBytes(StandardCharsets.UTF_8));

        // Generate 160 bit Salt for HMAC Key
        byte[] hsalt = new byte[32];
        r.nextBytes(hsalt);
        // Generate 160 bit HMAC Key
        byte[] dhk = deriveKey(p, hsalt, 100, 256);

        // Perform HMAC using SHA-256
        SecretKeySpec hks = new SecretKeySpec(dhk, "HmacSHA256");
        Mac m = Mac.getInstance("HmacSHA256");
        m.init(hks);
        byte[] hmac = m.doFinal(es);

        // Construct Output as "ESALT + HSALT + CIPHERTEXT + HMAC"
        byte[] os = new byte[64 + es.length + 32];
        System.arraycopy(esalt, 0, os, 0, 32);
        System.arraycopy(hsalt, 0, os, 32, 32);
        System.arraycopy(es, 0, os, 64, es.length);
        System.arraycopy(hmac, 0, os, 64 + es.length, 32);

        // Return a Base64 Encoded String
        return new String(Base64.getEncoder().encode(os), StandardCharsets.UTF_8);//new String(encodeBase64(os));
        //return os;
    }

    public String decrypt(String eos, String p) throws Exception {
        // Recover our Byte Array by Base64 Decoding
        byte[] os = Base64.getDecoder().decode(eos.getBytes(StandardCharsets.UTF_8));

        // Check Minimum Length (ESALT (32) + HSALT (32) + HMAC (32))
        if (os.length > 96) {
            // Recover Elements from String
            byte[] esalt = Arrays.copyOfRange(os, 0, 32);
            byte[] hsalt = Arrays.copyOfRange(os, 32, 64);
            byte[] es = Arrays.copyOfRange(os, 64, os.length - 32);
            byte[] hmac = Arrays.copyOfRange(os, os.length - 32, os.length);

            // Regenerate HMAC key using Recovered Salt (hsalt)
            byte[] dhk = deriveKey(p, hsalt, 100, 256);

            // Perform HMAC using SHA-256
            SecretKeySpec hks = new SecretKeySpec(dhk, "HmacSHA256");
            Mac m = Mac.getInstance("HmacSHA256");
            m.init(hks);
            byte[] chmac = m.doFinal(es);

            // Compare Computed HMAC vs Recovered HMAC
            if (MessageDigest.isEqual(hmac, chmac)) {
                // HMAC Verification Passed
                // Regenerate Encryption Key using Recovered Salt (esalt)
                byte[] dek = deriveKey(p, esalt, 100, 128);

                // Perform Decryption
                SecretKeySpec eks = new SecretKeySpec(dek, "AES");
                Cipher c = Cipher.getInstance("AES/CTR/NoPadding");
                c.init(Cipher.DECRYPT_MODE, eks, new IvParameterSpec(new byte[16]));
                byte[] s = c.doFinal(es);

                // Return our Decrypted String
                return new String(s, StandardCharsets.UTF_8);
            }
        }
        throw new Exception();
    }

    @Test
    public void testCrypto() throws Exception {
        Providers.getFullProviderList().providers().forEach(
                p-> {
                    System.out.println(p.getName() + " : " + p.getInfo());
                    p.getServices().forEach(s->{
                        System.out.println("\t" + s.getAlgorithm() + " : " + s.getType());
                    });
                }
            );

        String encrypted = encrypt("YOBAYOBAYOqwewtfsdgdsgsgs", "azaza");
        String decrypted = decrypt(encrypted, "azaza");
        System.out.println(encrypted);
        System.out.println(decrypted);

        //KeyGenerator aaa = KeyGenerator.getInstance("RSA");
       // SecretKey secretKey = aaa.generateKey();
       // SecretKeySpec sks = new SecretKeySpec("azaza".getBytes(),"RSA/EBC/PKCS8");
       // SecretKeySpec sks1 = new SecretKeySpec("azaza".getBytes(),"RSA/EBC/PKCS8");
/*
        Cipher c = Cipher.getInstance("RSA");
        c.init(Cipher.ENCRYPT_MODE,secretKey);
        byte[] encrypted = c.doFinal("YOBA".getBytes());
        c.init(Cipher.DECRYPT_MODE,secretKey);
        byte[] decryptedYOBA = c.doFinal(encrypted);
        String res = new String(decryptedYOBA);
        System.out.println(res);
        */
        /*
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        byte[] input = "YOBA".getBytes();
        byte[] keyBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
                0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 };

        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

        System.out.println(new String(input));

        // encryption pass
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
        int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
        ctLength += cipher.doFinal(cipherText, ctLength);
        System.out.println(new String(cipherText));
        System.out.println(ctLength);

        // decryption pass
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainText = new byte[cipher.getOutputSize(ctLength)];
        int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);
        ptLength += cipher.doFinal(plainText, ptLength);
        System.out.println(new String(plainText));
        System.out.println(ptLength);
*/
    }

    @Test
    public void azaza() throws NoSuchAlgorithmException {
        System.out.println(new String(Base64.getEncoder().encode("YOBAA456456456456456456456".getBytes())));
    }

}
