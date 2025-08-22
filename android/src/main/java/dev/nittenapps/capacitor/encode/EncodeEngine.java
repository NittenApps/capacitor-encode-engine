package dev.nittenapps.capacitor.encode;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncodeEngine {
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String CHARSET = "UTF-8";
    private static final String AES_MODE = "AES/CBC/PKCS7Padding";
    private static final String SHA_RSA = "SHA1withRSA";
    private static final String RSA = "RSA";
    private static final String AES = "AES";

    private static final String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final boolean DOUBLE_DIGIT = true;
    private static final int RADIX = 36;

    private static final String TAG = EncodeEngine.class.getSimpleName();

    private EncodeEngine() {
    }

    @NonNull
    public static String checkDigits(String input) {
        input = input.toUpperCase();

        int p = 0;
        for (int i = 0; i < input.length(); i++) {
            int val = CHARS.indexOf(input.charAt(i));
            if (val < 0) {
                throw new IllegalStateException("Found illegal character " + input.charAt(i));
            }
            p = ((p + val) * RADIX) % 1271;
        }

        if (DOUBLE_DIGIT) {
            p = (p * RADIX) % 1271;
        }

        int checksum = (1271 - p + 1) % 1271;

        if (DOUBLE_DIGIT) {
            int second = checksum % RADIX;
            int first = (checksum - second) / RADIX;
            return "" + CHARS.charAt(first) + CHARS.charAt(second);
        }

        return "" + CHARS.charAt(checksum);
    }

    // Decodes a ciphered text
    public static String decode(String key, String value) {
        String messageAfterDecrypt;

        try {
            final SecretKeySpec secretKeySpec = generateKey(key);
            byte[] decodedCipherText = Base64.decode(value, Base64.NO_WRAP);
            byte[] iv = new byte[16];

            for (int i = 0; i < 16; i++) {
                iv[i] = secretKeySpec.getEncoded()[i];
            }

            byte[] decryptedBytes = decrypt(secretKeySpec, iv, decodedCipherText);

            messageAfterDecrypt = new String(decryptedBytes, CHARSET);
        } catch (Exception ex) {
            Log.e(TAG, "Error decoding: " + value, ex);
            return "";
        }
        return messageAfterDecrypt;
    }

    // Encode a text
    public static String encode(final String key, String value) {
        String encryptedMsg = "";

        try {
            final SecretKeySpec secretKeySpec = generateKey(key);
            byte[] iv = new byte[16];
            for (int i = 0; i < 16; i++) {
                iv[i] = secretKeySpec.getEncoded()[i];
            }

            byte[] cipherText = encrypt(secretKeySpec, iv, value.getBytes(CHARSET));

            //NO_WRAP is important as was getting \n at the end
            encryptedMsg = Base64.encodeToString(cipherText, Base64.NO_WRAP);
        } catch (UnsupportedEncodingException | GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        }

        return encryptedMsg;
    }

    @Nullable
    public static String sign(@NonNull Context context, @NonNull String data) {
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.private_key);
            String keyContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            IOUtils.closeQuietly(inputStream);

            PrivateKey key = getPrivateKeyFromBase64(keyContent);
            return signInformation(key, data);
        } catch (IOException | GeneralSecurityException ex) {
            Log.e(TAG, "Error", ex);
            return null;
        }
    }

    /**
     * More flexible AES encrypt that doesn't encode
     *
     * @param key     AES key typically 128, 192 or 256 bit
     * @param iv      Initiation Vector
     * @param message in bytes (assumed it's already been decoded)
     * @return Encrypted cipher text (not encoded)
     * @throws GeneralSecurityException if something goes wrong during encryption
     */
    private static byte[] encrypt(final SecretKeySpec key, final byte[] iv, final byte[] message)
            throws GeneralSecurityException {
        final Cipher cipher = Cipher.getInstance(AES_MODE);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        return cipher.doFinal(message);
    }

    /**
     * More flexible AES decrypt that doesn't encode
     *
     * @param key               AES key typically 128, 192 or 256 bit
     * @param iv                Initiation Vector
     * @param decodedCipherText in bytes (assumed it's already been decoded)
     * @return Decrypted message cipher text (not encoded)
     * @throws GeneralSecurityException if something goes wrong during encryption
     */
    private static byte[] decrypt(final SecretKeySpec key, final byte[] iv, final byte[] decodedCipherText)
            throws GeneralSecurityException {
        final Cipher cipher = Cipher.getInstance(AES_MODE);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        return cipher.doFinal(decodedCipherText);
    }

    /**
     * Generates SHA256 hash of the password which is used as key
     *
     * @param password used to generated key
     * @return SHA256 of the password
     */
    @NonNull
    private static SecretKeySpec generateKey(@NonNull final String password) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        final MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] bytes = password.getBytes(CHARSET);
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        return new SecretKeySpec(key, AES);
    }

    @NonNull
    private static String getKey() {
        try {
            byte[] bytes = {76, 110, 107, 46, 73, 110, 110, 111, 118, 97, 99, 105, 111, 110, 46, 50, 48, 49, 56};

            Charset charset = Charset.forName(CHARSET);
            CharsetDecoder decoder = charset.newDecoder();
            ByteBuffer srcBuffer = ByteBuffer.wrap(bytes);
            CharBuffer resBuffer = decoder.decode(srcBuffer);
            return String.valueOf(resBuffer);
        } catch (Exception e) {
            return "";
        }
    }

    private static PrivateKey getPrivateKeyFromBase64(String base64) throws IOException, GeneralSecurityException {
        byte[] encoded = Base64.decode(base64, Base64.DEFAULT);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        return kf.generatePrivate(new PKCS8EncodedKeySpec(encoded));
    }

    @SuppressWarnings("unused")
    private static PublicKey getPublicKeyFromBase64(String base64) throws GeneralSecurityException {
        byte[] encoded = Base64.decode(base64, Base64.DEFAULT);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        return kf.generatePublic(new X509EncodedKeySpec(encoded));
    }

    @NonNull
    private static String signInformation(PrivateKey privateKey, @NonNull String message)
            throws NoSuchAlgorithmException,
            InvalidKeyException, SignatureException, UnsupportedEncodingException {
        Signature sign = Signature.getInstance(SHA_RSA);
        sign.initSign(privateKey);
        sign.update(message.getBytes(CHARSET));
        return new String(Base64.encode(sign.sign(), Base64.DEFAULT), CHARSET).replace("\n", "");
    }

    @SuppressWarnings("unused")
    private static boolean verifySignature(PublicKey publicKey, @NonNull String information, @NonNull String signature)
            throws SignatureException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Signature sign = Signature.getInstance(SHA_RSA);
        sign.initVerify(publicKey);
        sign.update(information.getBytes(CHARSET));
        return sign.verify(Base64.decode(signature.getBytes(CHARSET), Base64.DEFAULT));
    }
}
