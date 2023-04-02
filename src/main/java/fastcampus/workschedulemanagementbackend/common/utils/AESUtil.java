package fastcampus.workschedulemanagementbackend.common.utils;

import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

@Component
public class AESUtil {

    private final String ALGORITHM = "AES/CBC/PKCS5PADDING";
    private final String KEY = "01234567890123456789012345678901";
    private String iv;

    //암호화
    //생성한 iv 값과 Base64로 인코딩한 값을 합친 문자열을 리턴한다.
    public String encrypt(String data) {

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, createKeySpec(), createIvSpec());
            byte[] encryptData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return iv + Base64.getEncoder().encodeToString(encryptData);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("encrypt fail : " + e.getMessage());
        }
    }

    //복호화
//    데이터의 첫 16자리는 iv값, 이후의 데이터는 암호문이다.
//    암호문은 Base64로 인코딩 되어 있으므로 디코딩 한다.
//    복호화된 평문을 리턴한다.
    public String decrypt(String data) {
        String ivStr = data.substring(0,16);
        String content = data.substring(16);
        byte[] dataBytes = Base64.getDecoder().decode(content);

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, createKeySpec(), new IvParameterSpec(ivStr.getBytes(StandardCharsets.UTF_8)));
            byte[] original = cipher.doFinal(dataBytes);
            return new String(original, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("decrypt fail : " + e.getMessage());
        }
    }

    private IvParameterSpec createIvSpec() {
        try {
            this.iv = KEY.substring(0, 16);
            return new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("createIvSpec fail : " +  e.getMessage());
        }

    }

    //키 값은 임의의 문자열의 SHA-256 해시값을 사용했다.
    private Key createKeySpec() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(KEY.getBytes(StandardCharsets.UTF_8));
            return new SecretKeySpec(hashBytes, "AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("createKeySpec fail : " + e.getMessage());
        }
    }

}
