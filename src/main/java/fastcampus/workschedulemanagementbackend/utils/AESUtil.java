package fastcampus.workschedulemanagementbackend.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class AESUtil {

    private final String ALGORITHM = "AES/CBC/PKCS5PADDING";
    private final String KEY = "example";
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
    //매 요청마다 숫자와 영문을 조합한 랜덤 16바이트 값을 생성하여 멤버 변수에 저장
    private IvParameterSpec createIvSpec() {
        try {
            String iv = StringUtil.randomStr(16);
            this.iv = iv;
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
