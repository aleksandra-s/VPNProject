
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aleks_uuia3ly
 */
public class HandshakeCrypto {
    //The encrypt method takes a plaintext as a byte array, and returns the corresponding cipher text as a byte array. 
    //The key argument specifies the key – it can be a public key or private key. 
    public static byte[] encrypt(byte[] plaintext, Key key){
        Cipher cipher = null;
        byte[] cipherText = null;
        
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(plaintext);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(HandshakeCrypto.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return cipherText;
    }
    
    //The decrypt method does the decryption.
    public static byte[] decrypt(byte[] ciphertext, Key key){
        Cipher cipher = null;
        byte[] plainText = null;
        
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            plainText = cipher.doFinal(ciphertext);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(HandshakeCrypto.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return plainText;
    }
    
    //The getPublicKeyFromCertFile method extracts a public key from a certificate file (in PKCS#1 PEM format).
    public static PublicKey getPublicKeyFromCertFile(String certfile){
        CertificateFactory certificateFactory = null;
        X509Certificate certificate = null;
        FileInputStream certInputStream = null;
        try {
            //FileInputStream keyInputStrean;
            certificateFactory = CertificateFactory.getInstance("X.509");
            certInputStream = new FileInputStream(certfile);
            certificate = (X509Certificate) certificateFactory.generateCertificate(certInputStream);
            //return certificate.getPublicKey();
        } catch (CertificateException | FileNotFoundException ex) {
            Logger.getLogger(HandshakeCrypto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return certificate.getPublicKey();
    }
    
    //The getPrivateKeyFromKeyFile method extracts a private key from a key file (the file is in PKCS#1 PEM format or PKCS#8 DER format).
    public static PrivateKey getPrivateKeyFromKeyFile(String keyfile){
        PrivateKey privateKey = null;
        try {
            Path path = Paths.get(keyfile);
            byte[] privKeyBytes = Files.readAllBytes(path);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(HandshakeCrypto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return privateKey;
    }
}
