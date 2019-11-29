package com.ajay.encryptdecryptstring;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.security.auth.x500.X500Principal;

class EnCryptor {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
 //  private static final String TRANSFORMATION = "RSA/ECB/NoPadding";
//   private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

    public static SecretKey ss = null;

    private byte[] encryption;
    private byte[] iv;

    Context mContext;



    EnCryptor(Context context) {
        mContext = context;
    }

    byte[] encryptText(final String alias, final String textToEncrypt)
            throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException,
            NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException,
            InvalidAlgorithmParameterException, SignatureException, BadPaddingException,
            IllegalBlockSizeException {

        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias));
       // Log.d("secretkey", " encrypt key "+getSecretKey(alias));

        iv = cipher.getIV();

        return (encryption = cipher.doFinal(textToEncrypt.getBytes("UTF-8")));
    }

    @NonNull
    private SecretKey getSecretKey(final String alias) throws NoSuchAlgorithmException,
            NoSuchProviderException, InvalidAlgorithmParameterException {

        KeyGenerator keyGenerator;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

             keyGenerator = KeyGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);

            keyGenerator.init(new KeyGenParameterSpec.Builder(alias,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build());

            ss = keyGenerator.generateKey();

        } else {

              keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, ANDROID_KEY_STORE);
            keyGenerator.init(512 , new SecureRandom());

          /*  KeyGenerator keyGenerator = KeyGenerator
                    .getInstance(TRANSFORMATION, ANDROID_KEY_STORE);


            // use the supported init method here such as this one
       //    keyGenerator.init(size, secureRandom);
           keyGenerator.init(512, new SecureRandom());*/



        }

        return keyGenerator.generateKey();
    }


    byte[] getEncryption() {
        return encryption;
    }

    byte[] getIv() {
        return iv;
    }

}
