import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;

public class SessionIV {
    private IvParameterSpec ivParameterSpec;
    private byte[] iv;

    public SessionIV() throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        SecureRandom randomSecureRandom = new SecureRandom();
        iv = new byte[cipher.getBlockSize()];
        randomSecureRandom.nextBytes(iv);
        //ivParameterSpec = new IvParameterSpec(iv);
    }
    
    public SessionIV (byte[] iv){
        this.iv = iv;
    }



    public SessionIV(String stringIV) throws Exception {
        iv = decodeIV(stringIV);
        ivParameterSpec = new IvParameterSpec(iv);

    }

    public IvParameterSpec getSessionIVSpec() {
        return new IvParameterSpec(iv);
    }

    public static byte[] encryptSessionIV(SessionIV sessionIV, PublicKey publicKey) throws Exception {
        //String sessionIvString = sessionIV.encodeIV();
        //byte[] sessionIvBytes = sessionIvString.getBytes("utf-8");
        byte[] encryptedBytes = HandshakeCrypto.encrypt(sessionIV.getSessionIVSpec().getIV(), publicKey);

        return encryptedBytes;
    }

    public String encodeIV() {
        return Base64.getEncoder().encodeToString(iv);
    }

    private byte[] decodeIV(String stringIV) {
        return Base64.getDecoder().decode(stringIV);
    }
}
