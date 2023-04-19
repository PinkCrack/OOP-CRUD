package com.example.plugins;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;

public interface EncryptionPlugin {
    String getExtension();
    String getName();
    byte[] encrypt(byte[] bytes) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException;
    byte[] decrypt(byte[] bytes) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException;
}
