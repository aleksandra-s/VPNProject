
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aleks_uuia3ly
 */
public class VerifyCertificate {
    public static void main(String[] args) {
        X509Certificate caCertificate = null;
        X509Certificate userCertificate = null;
        FileInputStream fisCa = null;
        FileInputStream fisUser = null;
        try {
            fisCa = new FileInputStream(args[0]);
            BufferedInputStream bisCa = new BufferedInputStream(fisCa);
            CertificateFactory cfCa = CertificateFactory.getInstance("X.509");
            while (bisCa.available() > 0) {
                caCertificate = (X509Certificate) cfCa.generateCertificate(bisCa);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VerifyCertificate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException | IOException ex) {
            Logger.getLogger(VerifyCertificate.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fisCa.close();
            } catch (IOException ex) {
                Logger.getLogger(VerifyCertificate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try {
            fisUser = new FileInputStream(args[1]);
            BufferedInputStream bisUser = new BufferedInputStream(fisUser);
            CertificateFactory cfUser = CertificateFactory.getInstance("X.509");
            while (bisUser.available() > 0) {
                userCertificate = (X509Certificate) cfUser.generateCertificate(bisUser);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VerifyCertificate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException | IOException ex) {
            Logger.getLogger(VerifyCertificate.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fisUser.close();
            } catch (IOException ex) {
                Logger.getLogger(VerifyCertificate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       
        String caDN = getDnForCertificate((X509Certificate) caCertificate);
        System.out.println(caDN);
        String userDN = getDnForCertificate((X509Certificate) userCertificate);
        System.out.println(userDN);
        
        PublicKey caPublicKey = caCertificate.getPublicKey();
        
        int i = 0;
        
        try {
            caCertificate.verify(caPublicKey);
        } catch (CertificateException ex) {
            i++;
            System.out.println("Fail, " + ex + " verifying CA certificate because of encoding errors.");
        } catch (NoSuchAlgorithmException ex) {
            i++;
            System.out.println("Fail, " + ex + " verifying CA certificate because of unsupported signature algorithms.");
        } catch (InvalidKeyException ex) {
            i++;
            System.out.println("Fail, " + ex + " verifying CA certificate because of incorrect key.");
        } catch (NoSuchProviderException ex) {
            i++;
            System.out.println("Fail, " + ex + " verifying CA certificate because of no default provider.");
        } catch (SignatureException ex) {
            i++;
            System.out.println("Fail, " + ex + " verifying CA certificate because of signature errors unsupported signature.");
        }
        
        try {
            userCertificate.verify(caPublicKey);
              } catch (CertificateException ex) {
            i++;
            System.out.println("Fail, " + ex + " verifying CA certificate because of encoding errors.");
        } catch (NoSuchAlgorithmException ex) {
            i++;
            System.out.println("Fail, " + ex + " verifying CA certificate because of unsupported signature algorithms.");
        } catch (InvalidKeyException ex) {
            i++;
            System.out.println("Fail, " + ex + " verifying CA certificate because of incorrect key.");
        } catch (NoSuchProviderException ex) {
            i++;
            System.out.println("Fail, " + ex + " verifying CA certificate because of no default provider.");
        } catch (SignatureException ex) {
            i++;
            System.out.println("Fail, " + ex + " verifying CA certificate because of signature errors unsupported signature.");
        }
        
        try {
            caCertificate.checkValidity();
        } catch (CertificateExpiredException ex) {
            i++;
            System.out.println("Fail, " + ex + " verifying CA certificate because it has expired.");
        } catch (CertificateNotYetValidException ex) {
            i++;
            System.out.println("Fail, " + ex + " verifying CA certificate because it is not yet valid.");
        }
        
        try {
            userCertificate.checkValidity();
        } catch (CertificateExpiredException ex) {
            i++;
            System.out.println("Fail, " + ex + " verifying user certificate because it has expired.");
        } catch (CertificateNotYetValidException ex) {
            i++;
            System.out.println("Fail, " + ex + " verifying usrr certificate because it is not yet valid.");
        }
        
        if(i == 0){
            System.out.println("Pass");
        }
        
    }
    
    //**Method below based on Example 2 from https://www.programcreek.com/java-api-examples/?class=java.security.cert.X509Certificate&method=getSubjectDN**
    private static String getDnForCertificate(X509Certificate certificate) {
        if (certificate != null && certificate.getSubjectDN() != null) {
            return certificate.getSubjectDN().getName();
        }
        return "Unable to get DN";
    }
}
