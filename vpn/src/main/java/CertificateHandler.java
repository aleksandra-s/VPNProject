import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class CertificateHandler {

    // Gets the X509 certificate from the input parameter
    public static X509Certificate getCertificate(String input) throws IOException, CertificateException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        FileInputStream fisIn = new FileInputStream(input);
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(fisIn);
        //fisIn.close();
        return certificate;
    }

    // Generates certificate from byte []
    public static X509Certificate generateCertificate(String preCert) throws CertificateException {
        byte[] DERCert = Base64.getDecoder().decode(preCert);
        //CertificateFactory fact = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream byteIn = new ByteArrayInputStream(DERCert);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        return (X509Certificate) certificateFactory.generateCertificate(byteIn);
    }

    // Encodes the certificate to send to the server
    public static String encodeCertificate(X509Certificate certificate) throws CertificateEncodingException {
      
       /* String CERTIFICATE_BEGIN = "-----BEGIN CERTIFICATE-----";
        String CERTIFICATE_END = "-----END CERTIFICATE-----";
        String LINE_SEPARATOR = System.getProperty("line.separator");

        Base64.Encoder encoder = Base64.getMimeEncoder(64, LINE_SEPARATOR.getBytes());

        byte[] rawCrtText = certificate.getEncoded();
        String encodedCertText = new String(encoder.encode(rawCrtText));
        return CERTIFICATE_BEGIN + LINE_SEPARATOR + encodedCertText + LINE_SEPARATOR + CERTIFICATE_END;
        */
       
        return Base64.getEncoder().encodeToString(certificate.getEncoded());
    }

    // Verifies certificate with CAs public key and user certificate
    public static void verifyCertificate(X509Certificate caCert, X509Certificate clientCert) throws NoSuchProviderException, CertificateException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        PublicKey publicKey = caCert.getPublicKey();
        clientCert.checkValidity();
        clientCert.verify(publicKey);
    }
}
