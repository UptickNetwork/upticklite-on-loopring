package org.uptick.market.util;


import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
public class RSAHelper {
    public static final int KEY_SIZE = 2048;

    public static KeyPairInfo getKeyPair() {
        return getKeyPair(KEY_SIZE);
    }

    public static KeyPairInfo getKeyPair(int keySize) {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(keySize);

            KeyPair keyPair = keyPairGen.generateKeyPair();

            RSAPrivateKey oraprivateKey = (RSAPrivateKey) keyPair.getPrivate();

            RSAPublicKey orapublicKey = (RSAPublicKey) keyPair.getPublic();

            KeyPairInfo pairInfo = new KeyPairInfo(keySize);

            byte[] publicKeybyte = orapublicKey.getEncoded();
            String publicKeyString = Base64.getEncoder().encodeToString(publicKeybyte);
            pairInfo.setPublicKey(publicKeyString);

            byte[] privateKeybyte = oraprivateKey.getEncoded();
            String privateKeyString = Base64.getEncoder().encodeToString(privateKeybyte);
            pairInfo.setPrivateKey(privateKeyString);
            System.out.println(privateKeyString);
            return pairInfo;
        } catch (Exception e) {
            log.error("error: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * @param publicKeyBase64
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static PublicKey getPublicKey(String publicKeyBase64)
            throws InvalidKeySpecException, NoSuchAlgorithmException {

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicpkcs8KeySpec =
                new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyBase64));
        PublicKey publicKey = keyFactory.generatePublic(publicpkcs8KeySpec);
        return publicKey;
    }

    /**
     * @param privateKeyBase64
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey getPrivateKey(String privateKeyBase64)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privatekcs8KeySpec =
                new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyBase64));
        PrivateKey privateKey = keyFactory.generatePrivate(privatekcs8KeySpec);
        return privateKey;
    }


    /**
     * @param cipher
     * @param srcBytes
     * @param segmentSize
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     */
    public static byte[] cipherDoFinal(Cipher cipher, byte[] srcBytes, int segmentSize)
            throws IllegalBlockSizeException, BadPaddingException, IOException {
        if (segmentSize <= 0)
            throw new RuntimeException("Must be greater than zero");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int inputLen = srcBytes.length;
        int offSet = 0;
        byte[] cache;
        int i = 0;

        while (inputLen - offSet > 0) {
            if (inputLen - offSet > segmentSize) {
                cache = cipher.doFinal(srcBytes, offSet, segmentSize);
            } else {
                cache = cipher.doFinal(srcBytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * segmentSize;
        }
        byte[] data = out.toByteArray();
        out.close();
        return data;
    }

    /**
     * @param contentBase64
     * @param privateKeyBase64
     * @return
     * @segmentSize
     */
    public static String decipher(String contentBase64, String privateKeyBase64) {
        return decipher(contentBase64, privateKeyBase64, 128);
    }

    /**
     * @param contentBase64
     * @param privateKeyBase64
     * @return
     * @segmentSize
     */
    public static String decipher(String contentBase64, String privateKeyBase64, int segmentSize) {
        try {
            PrivateKey privateKey = getPrivateKey(privateKeyBase64);
            return decipher(contentBase64, privateKey, segmentSize);
        } catch (Exception e) {
            log.error("error: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * @param contentBase64
     * @param key
     * @param segmentSize
     * @return
     */
    public static String decipher(String contentBase64, Key key, int segmentSize) {
        try {

            byte[] srcBytes = Base64.getDecoder().decode(contentBase64);

            Cipher deCipher = Cipher.getInstance("RSA");

            deCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decBytes = null;//deCipher.doFinal(srcBytes);
            if (segmentSize > 0)
                decBytes = cipherDoFinal(deCipher, srcBytes, segmentSize);
            else
                decBytes = deCipher.doFinal(srcBytes);

            String decrytStr = new String(decBytes);
            return decrytStr;
        } catch (Exception e) {
            log.error("error: {}", e.getMessage(), e);
            return null;
        }
    }

    public static class KeyPairInfo {
        String privateKey;
        String publicKey;
        int keySize = 0;

        public KeyPairInfo(int keySize) {
            setKeySize(keySize);
        }

        public KeyPairInfo(String publicKey, String privateKey) {
            setPrivateKey(privateKey);
            setPublicKey(publicKey);
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public int getKeySize() {
            return keySize;
        }

        public void setKeySize(int keySize) {
            this.keySize = keySize;
        }
    }
}
