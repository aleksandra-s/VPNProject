
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
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
public class SessionDecrypter {
    private SessionKey sessionKey;
    private byte[] iv;
    
    SessionDecrypter(String key, String iv){
        this.sessionKey = new SessionKey(key);
        this.iv = Base64.getDecoder().decode(iv);
    }
    
    CipherInputStream openCipherInputStream(InputStream input){
        /** Code in this method partially based off examples on https://www.programcreek.com/java-api-examples/?api=javax.crypto.CipherOutputStream **/
        try {
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, this.sessionKey.getSecretKey(), new IvParameterSpec(this.iv));
            CipherInputStream cipherInputStream = new CipherInputStream(input, cipher);
            return cipherInputStream; 
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException ex) {
            System.out.println(ex);
        }
        return null;
    }
}
