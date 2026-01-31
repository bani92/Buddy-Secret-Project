package io.bani.buddy_secret.global.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AES256Util {

    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    // 실무 코드의 키를 그대로 사용하거나 관리용 키로 교체하세요.
    private static final String SYMMETRIC_KEY = "ioxBaED3ksy9T5SBtj/wlg==";
    private static final String AES_ALGORITHM = "AES/GCM/NoPadding";

    public String encrypt(String plaintext) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(SYMMETRIC_KEY);
            SecretKeySpec key = new SecretKeySpec(decodedKey, "AES");

            SecureRandom secureRandom = new SecureRandom();
            byte[] iv = new byte[GCM_IV_LENGTH];
            secureRandom.nextBytes(iv); // 실행 시마다 다른 IV 생성

            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);

            byte[] encryptedText = cipher.doFinal(plaintext.getBytes());

            ByteBuffer byteBuffer = ByteBuffer.allocate(GCM_IV_LENGTH + encryptedText.length);
            byteBuffer.put(iv);
            byteBuffer.put(encryptedText);

            return Base64.getEncoder().encodeToString(byteBuffer.array());
        } catch (Exception e) {
            throw new RuntimeException("암호화 실패", e);
        }
    }

    public String decrypt(String targetStr) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(SYMMETRIC_KEY);
            SecretKeySpec key = new SecretKeySpec(decodedKey, "AES");

            ByteBuffer byteBuffer = ByteBuffer.wrap(Base64.getDecoder().decode(targetStr));
            byte[] iv = new byte[GCM_IV_LENGTH];
            byteBuffer.get(iv);

            byte[] cipherText = new byte[byteBuffer.remaining()];
            byteBuffer.get(cipherText);

            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);

            byte[] decryptedText = cipher.doFinal(cipherText);
            return new String(decryptedText);
        } catch (Exception e) {
            throw new RuntimeException("복호화 실패", e);
        }
    }
}