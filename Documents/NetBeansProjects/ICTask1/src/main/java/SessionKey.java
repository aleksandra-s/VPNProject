
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.*;

/**
 *
 * @author aleksandra_soltan
 */
public class SessionKey {
    
    private SecretKey secretKey;
    
    SessionKey(Integer keyLength){
        try {
            /*Code in this method based on "Generating a Key" section of http://tutorials.jenkov.com/java-cryptography/index.html*/
            
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES"); //Instantiation with AES encryption algorithm
            
            SecureRandom secureRandom = new SecureRandom(); //Cryptographically strong random number generator
            keyGenerator.init(keyLength, secureRandom); 
            
            this.secretKey = keyGenerator.generateKey(); //Generate secret session key 
            
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Invalid algorithm");
        }
    }
    
    SessionKey(String encodedKey){
        /*Code based off StackOverflow answer https://stackoverflow.com/questions/5355466/converting-secret-key-into-a-string-and-vice-versa/12039611*/

        byte[] decodedKey = Base64.getDecoder().decode(encodedKey); //Decodes all bytes using Base64 encoding method
        this.secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"); //Constructs a secret key from given byte array
    }
    
    SecretKey getSecretKey(){
        return this.secretKey;
    }
    
    String encodeKey(){
        String encodedKey = Base64.getEncoder().encodeToString(this.secretKey.getEncoded());
        return encodedKey;
    }
    
}
