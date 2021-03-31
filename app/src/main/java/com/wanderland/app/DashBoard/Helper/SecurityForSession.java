package com.wanderland.app.DashBoard.Helper;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;

//The following example encrypts a given data block using AES.
// The encryption key is derived in a secure way (random salt, 1000 rounds of SHA-256).
// The encryption uses AES in CBC mode with random IV.
public class SecurityForSession {
    private static final int SALT_BYTES = 8;
    private static final int PBK_ITERATIONS = 1000;
    private static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String PBE_ALGORITHM = "PBEwithSHA256and128BITAES-CBC-BC";

    public static EncryptedData encrypt(String password, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException
            , InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException, InvalidAlgorithmParameterException {

        EncryptedData encData = new EncryptedData();
        SecureRandom rnd = new SecureRandom();
        encData.salt = new byte[SALT_BYTES];
        encData.iv = new byte[16]; // AES block size
        rnd.nextBytes(encData.salt);
        rnd.nextBytes(encData.iv);
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), encData.salt, PBK_ITERATIONS);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PBE_ALGORITHM);
        Key key = secretKeyFactory.generateSecret(keySpec);
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(encData.iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        encData.encryptedData = cipher.doFinal(data);
        return encData;
    }

    public static byte[] decrypt(String password, byte[] salt, byte[] iv, byte[] encryptedData) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, PBK_ITERATIONS);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PBE_ALGORITHM);
        Key key = secretKeyFactory.generateSecret(keySpec);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        return cipher.doFinal(encryptedData);
    }

    public static class EncryptedData {
        public byte[] salt;
        public byte[] iv;
        public byte[] encryptedData;
    }
}
