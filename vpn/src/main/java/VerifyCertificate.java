import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.*;

public class VerifyCertificate {
    private static CertificateFactory certificateFactory;
    private static FileInputStream caInputStream;
    private static X509Certificate caCertificate;
    private static FileInputStream userInputStream;
    private static X509Certificate userCertificate;
    public final X509Certificate certificate;

    VerifyCertificate(){
        this.certificate = userCertificate;
    }

    public static void main(String args[]) {


        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }

        try {
            caInputStream = new FileInputStream(args[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            caCertificate = (X509Certificate) certificateFactory.generateCertificate(caInputStream);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        System.out.println(caCertificate);

        try {
            caInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            userInputStream = new FileInputStream(args[1]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            userCertificate = (X509Certificate) certificateFactory.generateCertificate(userInputStream);
        } catch (CertificateException e) {
            e.printStackTrace();
        }

        System.out.println(userCertificate);

        try {
            userInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String caDN = getDnForCertificate(caCertificate);
        String userDN = getDnForCertificate(userCertificate);

        PublicKey publicKey = caCertificate.getPublicKey();

        int i = 0;

        try {
            caCertificate.verify(publicKey);
        } catch (InvalidKeyException ex) {
            //ex.printStackTrace();
            System.out.println("Fail, " + ex + " verifying CA certificate because of incorrect key.");
            i++;
        } catch (CertificateException ex) {
            //e.printStackTrace();
            System.out.println("Fail, " + ex + " verifying CA certificate because of encoding errors.");
            i++;
        } catch (NoSuchAlgorithmException ex) {
            //e.printStackTrace();
            System.out.println("Fail, " + ex + " verifying CA certificate because of unsupported signature algorithms.");
            i++;
        } catch (SignatureException ex) {
            //e.printStackTrace();
            System.out.println("Fail, " + ex + " verifying CA certificate because of signature errors unsupported signature.");
            i++;
        } catch (NoSuchProviderException ex) {
            //e.printStackTrace();
            System.out.println("Fail, " + ex + " verifying CA certificate because of no default provider.");
            i++;
        }

        try {
            userCertificate.verify(publicKey);
        } catch (InvalidKeyException ex) {
            //ex.printStackTrace();
            System.out.println("Fail, " + ex + " verifying CA certificate because of incorrect key.");
            i++;
        } catch (CertificateException ex) {
            //e.printStackTrace();
            System.out.println("Fail, " + ex + " verifying CA certificate because of encoding errors.");
            i++;
        } catch (NoSuchAlgorithmException ex) {
            //e.printStackTrace();
            System.out.println("Fail, " + ex + " verifying CA certificate because of unsupported signature algorithms.");
            i++;
        } catch (SignatureException ex) {
            //e.printStackTrace();
            System.out.println("Fail, " + ex + " verifying CA certificate because of signature errors unsupported signature.");
            i++;
        } catch (NoSuchProviderException ex) {
            //e.printStackTrace();
            System.out.println("Fail, " + ex + " verifying CA certificate because of no default provider.");
            i++;
        }

        try {
            caCertificate.checkValidity();
        } catch (CertificateExpiredException e) {
            e.printStackTrace();
            System.out.println("Certificate has expired");
            System.out.println("Fail");
            i++;
        } catch (CertificateNotYetValidException e) {
            e.printStackTrace();
            System.out.println("Certificate not yet vaild");
            System.out.println("Fail");
            i++;
        }
        try {
            userCertificate.checkValidity();
        } catch (CertificateExpiredException e) {
            e.printStackTrace();
            System.out.println("Certificate has expired");
            System.out.println("Fail");
            i++;
        } catch (CertificateNotYetValidException e) {
            e.printStackTrace();
            System.out.println("Certificate not yet vaild");
            System.out.println("Fail");
            i++;
        }

        System.out.println(caDN);
        System.out.println(userDN);

        if (i == 0) {
            System.out.println("Pass");
        }
    }
    //method from https://www.programcreek.com/java-api-examples/?class=java.security.cert.X509Certificate&method=getSubjectDN, Example 2
    private static String getDnForCertificate(X509Certificate certificate) {
        if (certificate != null && certificate.getSubjectDN() != null) {
            return certificate.getSubjectDN().getName();
        }
        return "Unable to get DN";
    }
}
