package com.example.plugins;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class AESPlugin implements EncryptionPlugin {
    private final String name = "AES";
    private final String extension = ".aes";
    private final Cipher cipher;
    private final byte[] byteKey = new byte[]{-6, 66, -27, -24, -96, -39, 11, -94, 117, 98, -105, 112,
            -72, 7, -25, 72, 23, 74, 45, -112, 38, 69, 54, -52, 10, -18, 100, -125, -67, 34, -82, 20};
    private final Key key;
    public AESPlugin() {
        String transformation = "AES/ECB/PKCS5Padding";
        try {
            cipher = Cipher.getInstance(transformation);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
        key = new SecretKeySpec(byteKey, name);
    }

    @Override
    public String getExtension() {
        return extension;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public byte[] encrypt(byte[] bytes) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(bytes);
    }

    @Override
    public byte[] decrypt(byte[] bytes) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(bytes);
    }
}
