package org.cejug.footprint.core.security.pdf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Calendar;
import java.util.MissingResourceException;

import org.cejug.footprint.core.security.FootprintSigner;
import org.cejug.footprint.core.util.AbstractConfigurableType;
import org.cejug.footprint.jaxb.ConfigSecurity;
import org.cejug.footprint.jaxb.FootprintConfig;
import org.cejug.footprint.jaxb.SignatureStamp;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * 
 * @author Felipe Gaucho
 */
public class DefaultFootprintSigner extends AbstractConfigurableType implements FootprintSigner {
    /** The private key alias in the keystore. */
    private transient String alias;
    /** The key store. */
    private transient final KeyStore keystore;
    /** default PdfName is Windows Certificate. */
    private transient PdfName pdfName = PdfSignatureAppearance.WINCER_SIGNED;

    /**
     * 
     * @param config
     *            the configuration object.
     * @throws KeyStoreException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws IOException
     * @throws InvalidKeyException
     * @throws MissingResourceException
     */
    public DefaultFootprintSigner(FootprintConfig config) throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, IOException, InvalidKeyException,
            MissingResourceException {
        super(config);
        this.keystore = FootprintSecurityFactory.getInstance().loadKeystore(config);
        ConfigSecurity security = config.getSecurity();
        this.alias = security.getKeystorePkAlias();
        if (alias == null || alias.length() == 0) {
            this.alias = this.keystore.aliases().nextElement();
        }
        String cryptoPdfName = security.getSignatureCryptoName();
        if (cryptoPdfName != null && cryptoPdfName.length() != 0) {
            this.pdfName = new PdfName(cryptoPdfName);
        }
    }

    /**
     * 
     * @param input
     * @param output
     * @throws DocumentException
     * @throws IOException
     * @throws UnrecoverableKeyException
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     */
    private void printSignature(InputStream input, OutputStream output) throws DocumentException, IOException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
        PrivateKey privateKey = (PrivateKey) keystore.getKey(alias, config.getSecurity().getKeystorePkPassword().toCharArray());
        Certificate[] chain = keystore.getCertificateChain(alias);

        PdfStamper signerStamper = PdfStamper.createSignature(new PdfReader(input), output, '\0');

        PdfSignatureAppearance sap = signerStamper.getSignatureAppearance();
        sap.setCrypto(privateKey, chain, null, pdfName);
        sap.setReason(config.getSecurity().getSignatureReason());
        sap.setLocation(config.getSecurity().getSignatureLocation());
        sap.setContact(config.getSecurity().getSignatureContactMail());
        sap.setSignDate(Calendar.getInstance());

        SignatureStamp stamp = config.getSecurity().getSignatureStamp();
        if (stamp != null) {
            // comment next line to have an invisible signature
            Image img = Image.getInstance(Thread.currentThread().getContextClassLoader().getResource(stamp.getSignatureStampImageFilename()));
            sap.setImage(img);
            // sap.setAcro6Layers(true);
            // sap.getLayer(3).addImage(img);
            Rectangle stampPosition = new Rectangle(stamp.getSignatureStampX1(), stamp.getSignatureStampY1(), stamp.getSignatureStampX2(), stamp.getSignatureStampY2());
            // Rectangle stampPosition = new Rectangle(100, 80, 125, 104);
            sap.setVisibleSignature(stampPosition, 1, stamp.getSignatureFieldName());
            sap.setSignatureGraphic(img);
            signerStamper.setThumbnail(img, 1);
        }
        signerStamper.close();
    }

    @Override
    public void sign(InputStream input, OutputStream output) throws IOException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, DocumentException {
        String inputCharset = config.getTemplate().getPdfTemplateCharset();

        if (inputCharset == null) {
            InputStreamReader reader = new InputStreamReader(input);
            inputCharset = reader.getEncoding();
        }

        String outputCharset = config.getTemplate().getPdfGeneratedCharset();

        if (inputCharset.equals(outputCharset)) {
            printSignature(input, output);
        } else {
            // The input charset and output charset are different. Converting.
            Charset inCharset = Charset.forName(inputCharset);
            CharsetDecoder decoder = inCharset.newDecoder();
            byte[] bytes = new byte[input.available()];
            if (input.read(bytes) > 0) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
                CharBuffer charBuffer = decoder.decode(byteBuffer);
                InputStream convertedStream = new ByteArrayInputStream(charBuffer.toString().getBytes());
                printSignature(convertedStream, output);
            } else {

                throw new DocumentException();
            }

        }
    }
}
