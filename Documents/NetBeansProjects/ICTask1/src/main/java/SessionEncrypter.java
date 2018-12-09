
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aleks_uuia3ly
 */
public class SessionEncrypter {
    private SessionKey sessionKey;
    private byte[] iv;
    
    SessionEncrypter(Integer keylength){
        this.sessionKey = new SessionKey(keylength);
        SecureRandom secureRandom = new SecureRandom();
        byte[] ivGenerate = new byte[16]; //NEVER REUSE THIS IV WITH SAME KEY
        secureRandom.nextBytes(ivGenerate);
        this.iv = ivGenerate;
    }
    
    String encodeKey(){
        String encodedKey = this.sessionKey.encodeKey();
        return encodedKey;  
    }
    
    String encodeIV(){
        String encodedIV = Base64.getEncoder().encodeToString(this.iv);
        return encodedIV;
    }
    
    CipherOutputStream openCipherOutputStream(OutputStream output){
        /** Code in this method partially based off examples on https://www.programcreek.com/java-api-examples/?api=javax.crypto.CipherOutputStream **/
        try {
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, this.sessionKey.getSecretKey(), new IvParameterSpec(iv));
            CipherOutputStream cipherOutputStream = new CipherOutputStream(output, cipher);
            return cipherOutputStream;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException ex) {
            System.out.println(ex);
        }
        return null;
    }
}
