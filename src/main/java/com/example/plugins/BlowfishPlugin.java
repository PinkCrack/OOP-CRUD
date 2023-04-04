package com.example.plugins;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class BlowfishPlugin implements EncryptionPlugin {
    private final String name = "Blowfish";
    private final Cipher cipher;
    private final byte[] byteKey = new byte[]{-15, -121, 97, 65, -98, -105, -76, -22, -76, -43, -6, -114,
            102, 2, -66, -88};
    private final Key key;
    public BlowfishPlugin() throws NoSuchPaddingException, NoSuchAlgorithmException {
        String transformation = "Blowfish/ECB/PKCS5Padding";
        cipher = Cipher.getInstance(transformation);
        key = new SecretKeySpec(byteKey, name);
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
