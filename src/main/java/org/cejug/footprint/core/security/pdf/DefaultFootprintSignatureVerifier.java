/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 Copyright (C) 2007 Felipe Gaúcho
 
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 
 This file is part of the FOOTPRINT Project (a generator of signed PDF
 documents, originally used as JUG events certificates), hosted at
 https://github.com/cejug/footprint
 
 
 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
package org.cejug.footprint.core.security.pdf;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.MissingResourceException;

import org.cejug.footprint.core.security.FootprintSignatureVerifier;
import org.cejug.footprint.core.util.AbstractConfigurableType;
import org.cejug.footprint.jaxb.ConfigSecurity;
import org.cejug.footprint.jaxb.FootprintConfig;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;

/**
 * Reads PDF files and check its digital certificates against the local key
 * store.
 * 
 * @author Felipe Gaucho
 */
public class DefaultFootprintSignatureVerifier extends AbstractConfigurableType implements FootprintSignatureVerifier {

    /** The key store. */
    private transient KeyStore keystore = null;

    /**
     * 
     * @param config
     *            the configuration object.
     * @throws KeyStoreException
     * @throws KeyStoreException
     * @throws NoSuchProviderException
     * @throws NoSuchProviderException
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws IOException
     * @throws InvalidKeyException
     * @throws MissingResourceException
     */
    public DefaultFootprintSignatureVerifier(FootprintConfig config) throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, IOException {
        super(config);

        ConfigSecurity security = config.getSecurity();
        String keystoreFilename = security.getKeystoreFilename();
        String keystoreProvider = security.getKeystorePkProvider();
        String keystoreType = security.getKeystoreType();
        if (keystoreProvider == null) {
            this.keystore = KeyStore.getInstance(keystoreType);
        } else {
            this.keystore = KeyStore.getInstance(keystoreType, keystoreProvider);
        }
        InputStream keystoreInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(keystoreFilename);
        this.keystore.load(keystoreInputStream, security.getKeystorePassword().toCharArray());
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean verify(String filename, KeyStore keystore) throws IOException, SignatureException, InvalidKeyException, KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException,
            CertificateException, MissingResourceException {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
        if (stream == null) {
            stream = new FileInputStream(filename);
        }
        PdfReader reader = new PdfReader(stream);
        AcroFields af = reader.getAcroFields();
        ArrayList<String> names = af.getSignatureNames();
        for (String name : names) {
            System.out.println("Signature name: " + name);
            System.out.println("Signature covers whole document: " + af.signatureCoversWholeDocument(name));
            System.out.println("Document revision: " + af.getRevision(name) + " of " + af.getTotalRevisions());
            /*
             * Start revision extraction FileOutputStream out = new
             * FileOutputStream("revision_" + af.getRevision(name) + ".pdf");
             * byte bb[] = new byte[8192]; InputStream ip =
             * af.extractRevision(name); int n = 0; while ((n = ip.read(bb)) >
             * 0) out.write(bb, 0, n); out.close(); ip.close(); // End revision
             * extraction
             */
            PdfPKCS7 pk = af.verifySignature(name);

            Calendar cal = pk.getSignDate();
            Certificate[] pkc = pk.getCertificates();
            System.out.println("Subject: " + PdfPKCS7.getSubjectFields(pk.getSigningCertificate()));
            System.out.println("Document modified: " + !pk.verify());

            Object fails[] = PdfPKCS7.verifyCertificates(pkc, keystore, null, cal);
            if (fails == null) {
                return true;
            } else {
                for (Object fail : fails) {
                    if (fail != null) {
                        try {
                            System.out.println(fail.getClass());
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(e.getMessage());
                        }
                    }
                }
                System.out.println("Certificate failed: " + fails[1]);
            }
        }

        return false;
    }
}
